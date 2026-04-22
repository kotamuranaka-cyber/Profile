package com.spring.springbootapplication.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration //Beanをまとめて置いておくクラス
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean //アクセスに認証が必要か不要か
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            /*.csrf(csrf -> csrf.disable()) // CSRF保護を無効化 */
            .authorizeHttpRequests((authorize) -> {
                authorize
                    .requestMatchers("/", "/register", "/login", "/css/**", "/js/**", "/error").permitAll() // 登録ページと静的リソースは全員アクセスできる
                    .anyRequest().authenticated(); // その他のリクエストは認証が必要
            })
            .formLogin((login) -> login
                .usernameParameter("email")
                .passwordParameter("password")
                // ログインを実行するページ
                .loginProcessingUrl("/login")
                // ログイン画面
                .loginPage("/login")
                // ログイン失敗時のURL
                .failureUrl("/login?error")
                // ログインに成功した場合の遷移先（ここでURLを指定しているので、POSTリクエストはControllerに書かなくていい）
                .defaultSuccessUrl("/topLoggedIn", true)
                // アクセス権
                .permitAll()

            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
            );
        return http.build();
    }

    @Bean // パスワードのハッシュ化
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
