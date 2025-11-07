package com.example.project_reactnative.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRequest {
    @NotBlank(message = "Họ tên không được để trống!")
    private String fullName;

    @NotBlank(message = "Email không được để trống!")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống!")
    private String password;

    private boolean gender;

    private String avatar;

    @NotBlank(message = "Số điện thoại không được để trống!")
    private String phoneNumber;

    @NotNull(message = "Vui lòng nhập ngày sinh!")
    private LocalDate dateOfBirth;
}
