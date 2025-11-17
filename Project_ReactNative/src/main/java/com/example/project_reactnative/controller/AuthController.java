package com.example.project_reactnative.controller;

import com.example.project_reactnative.model.dto.request.RefreshTokenRequest;
import com.example.project_reactnative.model.dto.request.UserLogin;
import com.example.project_reactnative.model.dto.request.UserRequest;
import com.example.project_reactnative.model.dto.response.APIResponse;
import com.example.project_reactnative.model.dto.response.JWTResponse;
import com.example.project_reactnative.model.entity.RefreshToken;
import com.example.project_reactnative.model.entity.User;
import com.example.project_reactnative.repository.AuthRepository;
import com.example.project_reactnative.security.jwt.JWTProvider;
import com.example.project_reactnative.service.AuthService;
import com.example.project_reactnative.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JWTProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthService authService, JWTProvider jwtProvider, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;
    }

//    @PostMapping("/register")
//    public ResponseEntity<APIResponse<User>> register(@Valid @RequestBody UserRequest userRequest) {}

    @PostMapping("/login")
    public ResponseEntity<APIResponse<JWTResponse>> login(@Valid @RequestBody UserLogin userLogin) {
        JWTResponse jwt = authService.login(userLogin);

        if (jwt == null) {
            Map<String, String> errors = new HashMap<>();
            errors.put("password", "Email hoặc mật khẩu không đúng!");
            APIResponse<JWTResponse> response = new APIResponse<>(
                    false, "Đăng nhập thất bại", null, errors, LocalDateTime.now()
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Tạo refresh token DB
        User user = authService.getUserByEmail(userLogin.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user, "unknown", 600_000);

        // Gắn refresh token vào JWTResponse
        jwt.setRefreshToken(refreshToken.getToken());

        return new ResponseEntity<>(new APIResponse<>(true, "Đăng nhập thành công!", jwt), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        String tokenStr = request.getRefreshToken();
        try {
            // 1️⃣ Lấy refresh token từ DB
            RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(tokenStr);
            User user = refreshToken.getUser();

            // 2️⃣ Tạo access token mới
            String newAccessToken = jwtProvider.generateToken(user.getEmail());

            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}

