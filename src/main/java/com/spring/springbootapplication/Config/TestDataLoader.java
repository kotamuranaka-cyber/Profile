package com.spring.springbootapplication.Config;

import com.spring.springbootapplication.Entity.Category;
import com.spring.springbootapplication.Entity.LearningData;
import com.spring.springbootapplication.Entity.User;

import com.spring.springbootapplication.Repository.CategoryRepository;
import com.spring.springbootapplication.Repository.LearningDataRepository;
import com.spring.springbootapplication.Repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

//テストデータを作成する
@Configuration
public class TestDataLoader {

    //ApplicationRunnerは、アプリを起動した際に一度だけ実行される
    @Bean
    public ApplicationRunner dataLoader(UserRepository userRepository, CategoryRepository categoryRepository, LearningDataRepository learningDataRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            User user = new User();
            user.setName("testuser");
            user.setEmail("test@example.com");
            user.setPassword(passwordEncoder.encode("pass1111")); //パスワードをハッシュ化しないとエラーになる
            userRepository.save(user);

            if (categoryRepository.count() == 0) {
                Category backend = new Category();
                backend.setName("バックエンド");
                categoryRepository.save(backend);

                Category frontend = new Category();
                frontend.setName("フロントエンド");
                categoryRepository.save(frontend);

                Category infra = new Category();
                infra.setName("インフラ");
                categoryRepository.save(infra);
            }

            List<Category> categories = categoryRepository.findAll();
            Category backend = categories.stream().filter(c -> c.getName().equals("バックエンド")).findFirst().orElse(null);
            Category frontend = categories.stream().filter(c -> c.getName().equals("フロントエンド")).findFirst().orElse(null);
            Category infra = categories.stream().filter(c -> c.getName().equals("インフラ")).findFirst().orElse(null);


            LocalDate june = LocalDate.now().withMonth(6).withDayOfMonth(1);


                LearningData ruby = new LearningData();
                ruby.setUser(user);
                ruby.setCategory(backend);
                ruby.setSubject("Ruby");
                ruby.setStudyTime(40);
                ruby.setStudyMonth(june);
                learningDataRepository.save(ruby);

                LearningData rails = new LearningData();
                rails.setUser(user);
                rails.setCategory(backend);
                rails.setSubject("Rails");
                rails.setStudyTime(40);
                rails.setStudyMonth(june);
                learningDataRepository.save(rails);

                LearningData mysql = new LearningData();
                mysql.setUser(user);
                mysql.setCategory(backend);
                mysql.setSubject("MySQL");
                mysql.setStudyTime(40);
                mysql.setStudyMonth(june);
                learningDataRepository.save(mysql);

                LearningData python = new LearningData();
                python.setUser(user);
                python.setCategory(backend);
                python.setSubject("Python");
                python.setStudyTime(40);
                python.setStudyMonth(june);
                learningDataRepository.save(python);

                LearningData html = new LearningData();
                html.setUser(user);
                html.setCategory(frontend);
                html.setSubject("HTML");
                html.setStudyTime(40);
                html.setStudyMonth(june);
                learningDataRepository.save(html);

                LearningData css = new LearningData();
                css.setUser(user);
                css.setCategory(frontend);
                css.setSubject("CSS");
                css.setStudyTime(40);
                css.setStudyMonth(june);
                learningDataRepository.save(css);


                LearningData heroku = new LearningData();
                heroku.setUser(user);
                heroku.setCategory(infra);
                heroku.setSubject("Heroku");
                heroku.setStudyTime(40);
                heroku.setStudyMonth(june);
                learningDataRepository.save(heroku);

                LearningData aws = new LearningData();
                aws.setUser(user);
                aws.setCategory(infra);
                aws.setSubject("AWS");
                aws.setStudyTime(40);
                aws.setStudyMonth(june);
                learningDataRepository.save(aws);

                LearningData firebase = new LearningData();
                firebase.setUser(user);
                firebase.setCategory(infra);
                firebase.setSubject("Firebase");
                firebase.setStudyTime(40);
                firebase.setStudyMonth(june);
                learningDataRepository.save(firebase);

        };
    }
}