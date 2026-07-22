package com.spring.springbootapplication.Entity;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
/* import jakarta.persistence.Lob; */
import jakarta.persistence.Table;
/* import jakarta.validation.constraints.Email; */
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="users")
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends BaseTimeEntity {

@Id
@GeneratedValue(strategy = GenerationType.AUTO)
@NotNull
@Column
private long id;

@Column(length = 255, nullable = false)
@NotBlank(message = "氏名は必ず入力してください")
@Size(max = 255, message = "氏名は255文字以内で入力してください")
private String name;

@Column(length = 100, nullable = false)
@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "メールアドレスが正しい形式ではありません")
@NotBlank(message = "メールアドレスは必ず入力してください")
private String email;

@Column(length = 255, nullable = false)
@NotBlank(message = "パスワードは必ず入力してください")
@Pattern(regexp="^(?=.*?[a-zA-Z])(?=.*?\\d)[a-zA-Z\\d]{8,}+$", message = "英数字8文字以上で入力してください")
private String password;

@Column(length = 255, nullable = true)
private String introduction;

@Column(name = "avatar_image", nullable = true, columnDefinition = "bytea")
private byte[] avatarImage; // Stringをバイト配列に変換

}
