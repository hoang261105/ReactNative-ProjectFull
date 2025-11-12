package com.example.project_reactnative.controller;

import com.example.project_reactnative.model.dto.request.UserProfile;
import com.example.project_reactnative.model.dto.request.UserRequest;
import com.example.project_reactnative.model.dto.response.APIResponse;
import com.example.project_reactnative.model.dto.response.UserResponse;
import com.example.project_reactnative.model.entity.User;
import com.example.project_reactnative.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<APIResponse<List<User>>> getAllUsers() {
        return new ResponseEntity<>(new APIResponse<>(true, "Lấy danh sách thành công", authService.getAllUsers()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<UserResponse>> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(new APIResponse<>(true, "Lấy chi tiết user thành công", authService.getUserById(id)), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<APIResponse<User>> updateUser(@PathVariable Long id, @Valid @RequestBody UserProfile userProfile) {
        return new ResponseEntity<>(new APIResponse<>(true, "Cập nhật thành công!", authService.updateUser(id, userProfile)), HttpStatus.OK);
    }
}
