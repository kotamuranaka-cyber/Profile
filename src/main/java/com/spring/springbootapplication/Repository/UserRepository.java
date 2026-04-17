package com.spring.springbootapplication.Repository;

import com.spring.springbootapplication.Entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //メールアドレスでユーザーを検索する
    Optional<User> findByEmail(String email);
}