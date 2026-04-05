package com.spring.springbootapplication.Entity;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="users")
@Data
public class User {

@Id
@GeneratedValue(strategy = GenerationType.AUTO)
@NotNull
@Column
private long id;

@Column(length = 100, nullable = false)
@NotBlank(message = "氏名は必ず入力してください")
@Size(max = 100, message = "氏名は100文字以内で入力してください")
private String name;

@Column(length = 100, nullable = false)
@Email(message = "メールアドレスが正しい形式ではありません")
@NotBlank(message = "メールアドレスは必ず入力してください")
private String email;

@Column(length = 255, nullable = false)
@NotBlank(message = "パスワードは必ず入力してください")
@Pattern(regexp="^(?=.*[A-Z])[a-zA-Z0-9_]{8,24}+$", message = "英数字8文字以上で入力してください")
private String password;

@Column(length = 255, nullable = true)
private String introduction;

@Column(name = "avatar_image", length = 255, nullable = true)
private String avatarImage;

}
