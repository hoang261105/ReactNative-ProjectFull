package com.example.project_reactnative.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private LocalDate commentDate;
    private String comment;
    private String avatar;
    private String fullName;
    private int rating;
}
