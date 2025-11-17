package com.example.project_reactnative.service;

import com.example.project_reactnative.model.entity.RefreshToken;
import com.example.project_reactnative.model.entity.User;
import com.example.project_reactnative.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // Tạo refresh token mới cho user
    public RefreshToken createRefreshToken(User user, String addressIp, long durationMs) {
        // 🔹 Nếu user đã có refresh token → cập nhật lại token và thời hạn
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(user);
        if (existingToken.isPresent()) {
            RefreshToken token = existingToken.get();
            token.setToken(UUID.randomUUID().toString());
            token.setAddressIp(addressIp);
            token.setExpiryDate(LocalDateTime.now().plusSeconds(durationMs / 1000));
            return refreshTokenRepository.save(token);
        }

        // 🔹 Nếu chưa có → tạo mới
        RefreshToken newToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .addressIp(addressIp)
                .expiryDate(LocalDateTime.now().plusSeconds(durationMs / 1000))
                .build();

        return refreshTokenRepository.save(newToken);
    }

    // Xác thực refresh token
    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token không hợp lệ!"));

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token đã hết hạn!");
        }

        return refreshToken;
    }

    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
