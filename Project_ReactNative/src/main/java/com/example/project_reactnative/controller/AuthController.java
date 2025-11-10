package com.example.project_reactnative.controller;

import com.example.project_reactnative.model.dto.request.UserLogin;
import com.example.project_reactnative.model.dto.request.UserRequest;
import com.example.project_reactnative.model.dto.response.APIResponse;
import com.example.project_reactnative.model.dto.response.JWTResponse;
import com.example.project_reactnative.model.entity.User;
import com.example.project_reactnative.repository.AuthRepository;
import com.example.project_reactnative.service.AuthService;
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
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<APIResponse<User>> register(@Valid @RequestBody UserRequest userRequest){
        return new ResponseEntity<>(new APIResponse<>(true, "Đăng ký thành công!", authService.register(userRequest)), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<JWTResponse>> login(@Valid @RequestBody UserLogin userLogin){
        JWTResponse jwt = authService.login(userLogin);

        if (jwt == null) {
            Map<String, String> errors = new HashMap<>();
            errors.put("password", "Email hoặc mật khẩu không đúng!");
            APIResponse<JWTResponse> response = new APIResponse<>(
                    false,
                    "Đăng nhập thất bại",
                    null,
                    errors,
                    LocalDateTime.now()
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new APIResponse<>(true, "Đăng nhập thành công!", jwt), HttpStatus.OK);
    }
}
