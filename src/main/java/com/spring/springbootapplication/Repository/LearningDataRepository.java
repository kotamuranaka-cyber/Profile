package com.spring.springbootapplication.Repository;

import com.spring.springbootapplication.Entity.User;
import com.spring.springbootapplication.Entity.LearningData;

import java.util.List;
import java.time.LocalDate;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningDataRepository extends JpaRepository<LearningData, Long> {
        //特定のユーザーの、特定の月の学習データを取得するメソッド
        List<LearningData> findByUserAndStudyMonth(User user, LocalDate studyMonth);

}
