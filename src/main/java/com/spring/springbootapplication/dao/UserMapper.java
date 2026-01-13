package com.spring.springbootapplication.dao;

import org.apache.ibatis.annotations.Mapper;
import com.spring.springbootapplication.entity.User;

@Mapper
public interface UserMapper {
    // ユーザー登録
    void insert(User user);

    // メールアドレス検索
    User findByEmail(String email);
}