package com.spring.springbootapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.springbootapplication.dao.UserMapper;
import com.spring.springbootapplication.dto.UserAddRequest;
import com.spring.springbootapplication.entity.User;

@Service
public class UserInfoService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void save(UserAddRequest userAddRequest) {
        User user = new User();
        user.setName(userAddRequest.getName());
        user.setEmail(userAddRequest.getEmail());

        // パスワードハッシュ化
        user.setPassword(passwordEncoder.encode(userAddRequest.getPassword()));

        userMapper.insert(user);
    }
}