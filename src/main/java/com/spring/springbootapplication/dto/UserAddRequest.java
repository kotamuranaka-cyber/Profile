package com.spring.springbootapplication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserAddRequest {

    @NotBlank(message = "氏名は必ず入力してください")
    @Size(max = 255, message = "氏名は255文字以内で入力してください")
    private String name;

    @NotBlank(message = "メールアドレスは必ず入力してください")
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "メールアドレスが正しい形式ではありません")
    private String email;

    @NotBlank(message = "パスワードは必ず入力してください")
    @Size(min = 8, message = "英数字8文字以上で入力してください")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$", message = "英数字8文字以上で入力してください")
    private String password;
}