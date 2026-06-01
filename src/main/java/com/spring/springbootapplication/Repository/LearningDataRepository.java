package com.spring.springbootapplication.Repository;

import com.spring.springbootapplication.Entity.User;
import com.spring.springbootapplication.Entity.Category;
import com.spring.springbootapplication.Entity.LearningData;

import java.util.List;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningDataRepository extends JpaRepository<LearningData, Long> {

        //特定のユーザーの、特定の月の学習データを取得するメソッド
        List<LearningData> findByUserAndStudyMonth(User user, LocalDate studyMonth);

        //特定のユーザーのデータがすでに存在するか確認するメソッド(ユーザー、カテゴリ、月、科目)。重複確認に使用する
        boolean existsByUserAndCategoryAndStudyMonthAndSubject(User user, Category category, LocalDate studyMonth, String subject);

        //テーブル内の行の並びを固定するメソッド
        List<LearningData> findByUserAndStudyMonthOrderByIdAsc(User user, LocalDate studyMonth);

}
