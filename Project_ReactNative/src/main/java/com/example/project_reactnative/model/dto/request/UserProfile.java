package com.example.project_reactnative.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    @NotBlank(message = "Họ tên không được để trống!")
    private String fullName;

    @NotBlank(message = "Email không được để trống!")
    private String email;

    private boolean gender;

    private MultipartFile file;
    private String avatar;

    @NotBlank(message = "Số điện thoại không được để trống!")
    private String phoneNumber;

    @NotNull(message = "Vui lòng nhập ngày sinh!")
    private LocalDate dateOfBirth;
}
