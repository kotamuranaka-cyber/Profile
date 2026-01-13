package com.spring.springbootapplication.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String introduction;
    private String avatarImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
