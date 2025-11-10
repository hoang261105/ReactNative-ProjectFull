package com.example.project_reactnative.service;

import com.example.project_reactnative.model.dto.request.UserLogin;
import com.example.project_reactnative.model.dto.request.UserProfile;
import com.example.project_reactnative.model.dto.request.UserRequest;
import com.example.project_reactnative.model.dto.response.JWTResponse;
import com.example.project_reactnative.model.dto.response.UserResponse;
import com.example.project_reactnative.model.entity.User;

import java.io.IOException;
import java.util.List;

public interface AuthService {
    User register(UserRequest userRequest);

    JWTResponse login(UserLogin userLogin);

    List<User> getAllUsers();

    UserResponse getUserById(Long id);

    User updateUser(Long id, UserProfile userProfile);
}
