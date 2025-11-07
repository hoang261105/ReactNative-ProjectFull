package com.example.project_reactnative.controller;

import com.example.project_reactnative.model.dto.response.APIResponse;
import com.example.project_reactnative.model.entity.User;
import com.example.project_reactnative.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
