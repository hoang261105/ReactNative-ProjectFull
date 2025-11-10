package com.example.project_reactnative.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private Long hotelId;
    private Long roomId;
    private Long userId;

    @NotBlank(message = "Vui lòng chọn số sao!")
    private int rating;

    @NotBlank(message = "Vui lòng nhập đánh giá!")
    private String comment;
}
