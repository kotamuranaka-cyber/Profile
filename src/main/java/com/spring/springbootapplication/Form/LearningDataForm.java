package com.spring.springbootapplication.Form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LearningDataForm {


    //画面裏で、skillEdit.htmlから渡されるデータ
    private Long categoryId;
    private String studyMonth;


    //ユーザーが入力する項目
    @NotBlank(message = "項目名は必ず入力してください")
    @Size(max = 50, message = "項目名は50文字以内で入力してください")
    private  String subject;

    @NotNull(message = "学習時間は必ず入力してください")
    @Min(value = 0, message = "学習時間は0以上で入力してください")
    private Integer studyTime;

}
