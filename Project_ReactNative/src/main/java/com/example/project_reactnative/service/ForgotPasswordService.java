package com.example.project_reactnative.service;

import com.example.project_reactnative.model.dto.request.ResetPasswordRequest;
import com.example.project_reactnative.model.entity.User;
import com.example.project_reactnative.repository.AuthRepository;
import com.example.project_reactnative.security.exception.CustomValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {
    private final AuthRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    /**
     * Gửi OTP qua email
     */
    public void sendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy tài khoản với email này!"));

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        user.setOtp(otp);
        user.setOtpExpiredAt(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Mã OTP khôi phục mật khẩu");
        message.setText("""
                Xin chào %s,
                Mã OTP khôi phục mật khẩu của bạn là: %s
                Mã có hiệu lực trong 5 phút.
                Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.
                """.formatted(user.getFullName(), otp));
        mailSender.send(message);
    }

    public User verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy tài khoản với email này!"));

        if (user.getOtp() == null || !user.getOtp().equals(otp)) {
            throw new CustomValidationException(Map.of("errorOtp", "Mã OTP không chính xác!"));
        }

        if (user.getOtpExpiredAt() == null || user.getOtpExpiredAt().isBefore(LocalDateTime.now())) {
            throw new CustomValidationException(Map.of("errorOtp", "Mã OTP đã hết hạn! Vui lòng yêu cầu mã mới."));
        }

        return user;
    }

    // Chỉ đổi mật khẩu
    public void changePassword(ResetPasswordRequest resetPasswordRequest) {
        User user = userRepository.findByEmail(resetPasswordRequest.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy tài khoản với email này!"));

        if (!resetPasswordRequest.getNewPasswordConfirm().equals(resetPasswordRequest.getNewPassword())) {
            throw new CustomValidationException(Map.of("newPasswordConfirm", "Xác nhận mật khẩu không khớp!"));
        }

        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        user.setOtp(null);
        user.setOtpExpiredAt(null);
        userRepository.save(user);
    }
}
