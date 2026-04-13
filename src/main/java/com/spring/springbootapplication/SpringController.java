package com.spring.springbootapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.spring.springbootapplication.Entity.User;
import com.spring.springbootapplication.Repository.UserRepository;
import com.spring.springbootapplication.Config.SecurityConfig;

import jakarta.validation.Valid;


@Controller
public class SpringController {

    @Autowired
    private UserRepository userRepository;

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

    @GetMapping("/topLoggedIn") // ログインしたあとのhtmlを表示
    public String showTopLoggedInPage() {
        return "topLoggedIn"; 
    }

}
