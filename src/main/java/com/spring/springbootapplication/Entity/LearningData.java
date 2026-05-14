package com.spring.springbootapplication.Entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "learning_data")
@Data
@EqualsAndHashCode(callSuper = false)
public class LearningData extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    //外部キーのリレーション設定
    @ManyToOne //多対一、LearningDataの複数のエンティティ情報(多)に対してUserのidが対応している(一)。
    @JoinColumn(name = "user_id", nullable = false) //今回のように指定がなければ、対象のエンティティの主キーを参照する
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(length = 100, nullable = false)
    private String subject;

    @Column(name = "study_time", nullable = false)
    private Integer studyTime = 0;

    @Column(name = "study_month", nullable = false)
    private LocalDate studyMonth;

}
