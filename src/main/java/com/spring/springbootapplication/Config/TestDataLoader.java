package com.spring.springbootapplication.Config;

import com.spring.springbootapplication.Entity.User;
import com.spring.springbootapplication.Repository.UserRepository;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

//テストデータを作成する
@Configuration
public class TestDataLoader {

    //ApplicationRunnerは、アプリを起動した際に一度だけ実行される
    @Bean
    public ApplicationRunner dataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            User user = new User();
            user.setName("testuser");
            user.setEmail("test@example.com");
            user.setPassword(passwordEncoder.encode("pass1111")); //パスワードをハッシュ化しないとエラーになる
            userRepository.save(user);
        };
    }
}