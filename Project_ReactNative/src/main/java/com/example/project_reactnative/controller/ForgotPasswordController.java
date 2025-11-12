package com.example.project_reactnative.controller;

import com.example.project_reactnative.model.dto.request.ResetPasswordRequest;
import com.example.project_reactnative.model.dto.response.APIResponse;
import com.example.project_reactnative.service.ForgotPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @PostMapping("/forgot-password")
    public ResponseEntity<APIResponse<String>> forgotPassword(@RequestParam String email) {
        forgotPasswordService.sendOtp(email);
        return ResponseEntity.ok(new APIResponse<>(true, "Mã OTP đã được gửi đến email của bạn!", null));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<APIResponse<String>> verifyOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String otp = body.get("otp");

        forgotPasswordService.verifyOtp(email, otp);

        return ResponseEntity.ok(new APIResponse<>(true, "Xác thực OTP thành công!", null));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<APIResponse<String>> changePassword(@Valid @RequestBody ResetPasswordRequest request) {
        forgotPasswordService.changePassword(request);
        return ResponseEntity.ok(new APIResponse<>(true, "Đặt lại mật khẩu thành công!", null));
    }
}
