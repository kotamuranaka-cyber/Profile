package com.spring.springbootapplication.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.springbootapplication.Entity.User;
import com.spring.springbootapplication.Repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)   //DBからメールアドレスでユーザーを検索する
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません：" + email)); //ユーザーが見つからない場合エラーを表示

        //ユーザーが見つかったら、DBからメールアドレスとパスワードを取得する
        String userEmail = user.getEmail();
        String userPassword = user.getPassword();

        //ユーザーのメールアドレスとパスワードをSpring SecurityのUserDetailsオブジェクトに変換して返す
        return new org.springframework.security.core.userdetails.User(
                userEmail,
                userPassword,
                //
                AuthorityUtils.createAuthorityList("ROLE_USER")
        );
    }

}
