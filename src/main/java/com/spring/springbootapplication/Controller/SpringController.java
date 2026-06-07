package com.spring.springbootapplication.Controller;


import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.springbootapplication.Entity.Category;
import com.spring.springbootapplication.Entity.LearningData;
import com.spring.springbootapplication.Entity.User;
import com.spring.springbootapplication.Form.LearningDataForm;
import com.spring.springbootapplication.Repository.CategoryRepository;
import com.spring.springbootapplication.Repository.LearningDataRepository;
import com.spring.springbootapplication.Repository.UserRepository;
import com.spring.springbootapplication.Config.SecurityConfig;

import jakarta.validation.Valid;



@Controller
public class SpringController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LearningDataRepository learningDataRepository;

    @Autowired
    private SecurityConfig securityConfig;

    @GetMapping("/")
    public String index() {
        return "redirect:/register"; // 新規登録ページにリダイレクト
    }

    // 登録ページ
    @GetMapping("/register")
    public String showRegistrationPage(@ModelAttribute("userModel") User user, Model model) {
        return "register";
    }
    // POSTリクエスト
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userModel") @Valid User user, BindingResult result, Model model) {

        // 入力エラーがある場合は元のページに戻る
        if (result.hasErrors()) {
            return "register";
        }

        // パスワードハッシュ化をConfigから呼び出す
        String hashedPassword = securityConfig.passwordEncoder().encode(user.getPassword());
        user.setPassword(hashedPassword);

        // ユーザーを登録する
        userRepository.saveAndFlush(user);

        return "redirect:/topLoggedIn"; // 登録ができたらトップページへ
    }

    @Transactional
    @GetMapping("/topLoggedIn") // ログインしたあとのhtmlを表示
    public String showTopLoggedInPage(Model model, @AuthenticationPrincipal UserDetails userDetails/* @AuthenticationPrincipalは、認証済みのユーザー情報を簡単に取得できるアノテーション */) {

        //データベースから、ログイン中のユーザーのアドレスで検索する
        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null); 

        // 画像データをBase64に変換して、HTMLで使える形にする
        if (user != null && user.getAvatarImage() != null) {

            // バイト配列をBase64文字列に変換
            String base64Image = Base64.getEncoder().encodeToString(user.getAvatarImage());

            // モデルに変換した画像データを渡す
            model.addAttribute("avatarBase64", base64Image);

        }

        model.addAttribute("user", user);

        /* チャート部分に各カテゴリの学習時間の合計を渡すための動作部分 */

        //バックエンド、フロントエンド、インフラの学習時間の合計を格納するリストを用意
        List<Integer> backendData = new ArrayList<>();
        List<Integer> frontendData = new ArrayList<>();
        List<Integer> infraData = new ArrayList<>();

        //先々月、先月、今月の順番で表示するために、ループで3ヶ月分のデータを取得する
        for (int i = 2; i >= 0; i--) {

            LocalDate month = LocalDate.now().minusMonths(i).withDayOfMonth(1);

            //ユーザーと月をもとに学習データを取得する
            List<LearningData> monthlyData = learningDataRepository.findByUserAndStudyMonth(user, month);

            //合計値を格納する変数を用意しておく
            int backendSum = 0;
            int frontendSum = 0;
            int infraSum = 0;

            //ループで学習データを1件ずつ見ていき、カテゴリごとに学習時間を合計していく
            for (LearningData data : monthlyData) {

                if ("バックエンド".equals(data.getCategory().getName())) {

                    backendSum += data.getStudyTime();

                } else if ("フロントエンド".equals(data.getCategory().getName())) {

                    frontendSum += data.getStudyTime();

                } else if ("インフラ".equals(data.getCategory().getName())) {

                    infraSum += data.getStudyTime();

                }

            }

            //ループを抜けたら、各カテゴリの合計をリストに追加する。これを3ヶ月分繰り返す。
            backendData.add(backendSum);
            frontendData.add(frontendSum);
            infraData.add(infraSum);

        }

            //HTMLに各
            model.addAttribute("backendData", backendData);
            model.addAttribute("frontendData", frontendData);
            model.addAttribute("infraData", infraData);

        return "topLoggedIn"; 
    }

    @GetMapping("/login") // ログインページを表示
    public String showLoginPage(){
        return "login";
    }

    @GetMapping(value = "/login", params = "error") // ログイン失敗時のURL
    public String loginError(RedirectAttributes redirectAttributes) {
        //ログイン失敗時のflashメッセージ
        redirectAttributes.addFlashAttribute("errorMessage", "メールアドレス、もしくはパスワードが間違っています");
        //もとのログインページに遷移
        return "redirect:/login";
    }
    

    /*@PostMapping("/login") // ログイン処理
    public String loginUser(@ModelAttribute("userModel") @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "login";
        }
    SecurityConfig側でPOSTリクエストを処理するのでここにPOSTMappingは書かなくてよい
    }*/

    //自己紹介編集ページGET
    @Transactional
    @GetMapping("/profile/edit")
    public String showEditProfilePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        model.addAttribute("user", user);
        return "profileEdit";
    }

    //自己紹介編集ページPOST
    @Transactional
    @PostMapping("/profile/edit")
    public String editProfile(@AuthenticationPrincipal UserDetails userDetails, 
        @RequestParam("introduction") String introduction, 
        @RequestParam("avatarImage") MultipartFile avatarImage, 
        Model model) throws IOException {
        
        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        if (introduction == null || introduction.length() < 50 || introduction.length() > 200) {
            model.addAttribute("errorMessage", "自己紹介は50文字以上200文字以下で入力してください");
            model.addAttribute("user", user);
            return "profileEdit";
        }
        user.setIntroduction(introduction);

        if(avatarImage != null && !avatarImage.isEmpty()) {
            user.setAvatarImage(avatarImage.getBytes());
        }
        userRepository.saveAndFlush(user);
        return "redirect:/topLoggedIn";
    }

    //学習時間一覧ページ
    @GetMapping("/skill/edit")
    public String showSkillEditPage(@RequestParam (name = "month", required = false) String monthString, 
        @AuthenticationPrincipal UserDetails userDetails, Model model ) {

            LocalDate displayMonth; //表示する月を宣言

            //URLに表示する日付を"yyyy-MM"のフォーマットに変換する。
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

            //日にちを1日に固定するのは、DBから学習データを取得する際に、月単位で検索するため。DBには"yyyy-MM-01"の形で保存されている。
            if (monthString != null && !monthString.isEmpty()) {

                displayMonth = YearMonth.parse(monthString, formatter).atDay(1); //パラメータがある場合はその月を表示

            } else {

                displayMonth = LocalDate.now().withDayOfMonth(1); //もしパラメータが無い場合は当月を表示
                
            }


            //User情報を取得
            User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

            //skillEdit.htmlに渡すモデルの属性をセットする共通メソッド
            setSkillEditModelAttributes(model, user, displayMonth);


        return "skillEdit";

    }
    
    //学習項目および学習時間追加ページ
    @GetMapping("/skill/add")
    public String showNewSkillPage (@RequestParam("categoryId") Long categoryId, @RequestParam("month") String monthString, @AuthenticationPrincipal UserDetails userDetails, Model model) {

            //画面にカテゴリー名を表示するために、渡されたIDからカテゴリーを取得する。
            Category category = categoryRepository.findById(categoryId).orElse(null);


            //            
            LearningDataForm form = new LearningDataForm();
            form.setCategoryId(categoryId);
            form.setStudyMonth(monthString);


            //HTMLにデータを渡す
            model.addAttribute("category", category);
            model.addAttribute("learningDataForm", form);   // th:object="${learningDataForm}"でフォームのデータを受け取るためのオブジェクト
        

            return "skillNew";

    }

    //学習項目および学習時間追加処理
    @PostMapping("/skill/add")
    public String addNewSkill(@ModelAttribute("learningDataForm") @Valid LearningDataForm form, BindingResult result, @AuthenticationPrincipal UserDetails userDetails, Model model) {


        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        Category category = categoryRepository.findById(form.getCategoryId()).orElse(null);

        // フォームから渡された月の文字列をLocalDate型に変換する。その際、yyyy-MMの形式で渡されるので、末尾に"-01"をつけてDBに保存されている形式と合わせる。
        LocalDate studyMonth = LocalDate.parse(form.getStudyMonth() + "-01");

        model.addAttribute("category", category);



        //------------------エラーチェック部分-----------------
        //バリデーションに引っかかった場合は同じページに返される
        if (result.hasErrors()) {

            return "skillNew";

        }

        //リポジトリの重複確認メソッドを呼び出す
        boolean isDuplicate = learningDataRepository.existsByUserAndCategoryAndStudyMonthAndSubject(user, category, studyMonth, form.getSubject());

        //重複チェック。ユーザー、カテゴリ、月、科目が同じデータがすでに存在する場合はエラーを表示する        
        if (isDuplicate) {

            model.addAttribute("errorMessage", form.getSubject() + "は既に登録されています");
            return "skillNew";

        }
        //------------------エラーチェック部分終了---------------

        //エラーが存在しなければ、DBに保存する。
        LearningData data = new LearningData();
        data.setUser(user);
        data.setCategory(category);
        data.setStudyMonth(studyMonth);
        data.setSubject(form.getSubject()); // 入力された項目名
        data.setStudyTime(form.getStudyTime()); // 入力された学習時間


        learningDataRepository.saveAndFlush(data);

        //モーダルを表示するため、成功フラグをモデルに渡す(リダイレクトしてしまうとモーダル表示せずにページが遷移してしまう。)
        model.addAttribute("isSuccess", true);

        return "skillNew";
    
    }

    //変更保存ボタン押下後のPOST処理
    @Transactional
    @PostMapping("/skill/update")
    public String updateStudyTime(@RequestParam("learningDataId") Long learningDataId, @RequestParam("studyTime") Integer studyTime, @RequestParam("month") String monthString, @AuthenticationPrincipal UserDetails userDetails, Model model) {

        //User情報を取得、表示する月を決める、どの学習データを更新するかを決める
        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        LocalDate studyMonth = LocalDate.parse(monthString + "-01");

        LearningData data = learningDataRepository.findById(learningDataId).orElse(null);

        //------------------エラーチェック部分-----------------
        //バリデーションに引っかかった場合は同じページに返される

        // データが存在しない、または現在のユーザーのデータでない場合はエラー
        if (data == null || data.getUser().getId() != user.getId()) {


            return "redirect:/skill/edit?month=" + monthString; // エラーがあった場合は元のページにリダイレクト

        }

        // 学習時間が負の数の場合はエラー
        if (studyTime == null || studyTime < 0) {

            return "redirect:/skill/edit?month=" + monthString; // エラーがあった場合は元のページにリダイレクト

        }
        //------------------エラーチェック部分終了---------------

        //エラーが存在しなければ、DBに保存する。
        data.setStudyTime(studyTime);
        learningDataRepository.saveAndFlush(data);

        //モーダル表示のための成功フラグと更新する項目名
        model.addAttribute("isSuccess", true); // 更新成功のフラグをモデルに追加
        model.addAttribute("saveSubject", data.getSubject()); // 更新した項目名をモデルに追加


        //skillEdit.htmlの表示に必要なデータを再度モデルに渡す。
        setSkillEditModelAttributes(model, user, studyMonth);

        return "skillEdit";

    }

    //削除ボタン押下後のPOST処理
    @Transactional
    @PostMapping("/skill/delete")
    public String deleteLearningData(@RequestParam("learningDataId") Long learningDataId, @RequestParam("month") String monthString, @AuthenticationPrincipal UserDetails userDetails, Model model) {

        //User情報を取得、表示されている月を決める、どの学習データを削除するかを決める
        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        LocalDate studyMonth = LocalDate.parse(monthString + "-01");

        LearningData data = learningDataRepository.findById(learningDataId).orElse(null);

        //------------------エラーチェック部分-----------------
        //バリデーションに引っかかった場合は同じページに返される

        // データが存在しない、または現在のユーザーのデータでない場合はエラー
        if (data == null || data.getUser().getId() != user.getId()) {


            return "redirect:/skill/edit?month=" + monthString; // エラーがあった場合は元のページにリダイレクト

        }

        //------------------エラーチェック部分終了---------------

        //エラーが存在しなければ、DBから削除する。
        learningDataRepository.delete(data);

        //モーダル表示のための成功フラグと更新する項目名
        model.addAttribute("isDeleteSuccess", true); // 削除成功のフラグをモデルに追加
        model.addAttribute("deletedSubject", data.getSubject()); // 削除した項目名をモデルに追加


        //skillEdit.htmlの表示に必要なデータを再度モデルに渡す。
        setSkillEditModelAttributes(model, user, studyMonth);

        return "skillEdit";

    }


    // skillEditに渡すモデルの属性をセットする共通メソッド(GETとPOSTで重複する処理をまとめる)
    private void setSkillEditModelAttributes(Model model, User user, LocalDate displayMonth) {

            //プルダウンのリストを作成(今の月、今の月マイナスひと月、マイナスふた月)
            List<String> monthList = new ArrayList<>();

            for (int i = 0 ; i < 3 ; i ++ ) {

                //フォーマットを"yyyy-MM"にしてリストに追加
                monthList.add(LocalDate.now().minusMonths(i).format(DateTimeFormatter.ofPattern("yyyy-MM"))); 

            }

            //カテゴリー一覧を取得
            List<Category> categories = categoryRepository.findAll();

            //取得したユーザーと表示する月をもとに学習データを取得、テーブル内の行はIDの昇順で固定
            List<LearningData> learningDataList = learningDataRepository.findByUserAndStudyMonthOrderByIdAsc(user, displayMonth);

            model.addAttribute("categories", categories);
            model.addAttribute("monthList", monthList);
            model.addAttribute("displayMonth", displayMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"))); //displayMonthを"yyyy-MM"のStringに変換してHTMLに渡す
            model.addAttribute("user", user);
            model.addAttribute("learningDataList", learningDataList);

    }


}
