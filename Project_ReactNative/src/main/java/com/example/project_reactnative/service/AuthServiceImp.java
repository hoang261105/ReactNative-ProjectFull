package com.example.project_reactnative.service;

import com.example.project_reactnative.security.exception.CustomValidationException;
import com.example.project_reactnative.model.dto.request.UserLogin;
import com.example.project_reactnative.model.dto.request.UserRequest;
import com.example.project_reactnative.model.dto.response.JWTResponse;
import com.example.project_reactnative.model.entity.User;
import com.example.project_reactnative.repository.AuthRepository;
import com.example.project_reactnative.security.jwt.JWTProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImp implements AuthService {
    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTProvider jwtProvider;

    @Override
    public User register(UserRequest userRequest) {
        Map<String, String> errors = new HashMap<>();
        String regexEmail = "^[a-zA-Z0-9._]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,6}$";
        String regexPhone = "^(0(3|5|7|8|9)\\d{8}|\\+84(3|5|7|8|9)\\d{8})$";
        if (userRequest.getEmail() == null || !userRequest.getEmail().matches(regexEmail)) {
            errors.put("email", "Email không đúng định dạng!");
        } else if(authRepository.existsByEmail(userRequest.getEmail())) {
            errors.put("email", "Email đã tồn tại!");
        }

        if (userRequest.getPhoneNumber() == null || !userRequest.getPhoneNumber().matches(regexPhone)) {
            errors.put("phoneNumber", "Số điện thoại không đúng định dạng!");
        } else if(authRepository.existsByPhoneNumber(userRequest.getPhoneNumber())) {
            errors.put("phoneNumber", "Số điện thoại đã tồn tại!");
        }

        if (!errors.isEmpty()) {
            throw new CustomValidationException(errors);
        }

        User user = User.builder()
                .fullName(userRequest.getFullName())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .avatar("https://aic.com.vn/wp-content/uploads/2024/10/avatar-fb-mac-dinh-1.jpg")
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .gender(userRequest.isGender())
                .dateOfBirth(userRequest.getDateOfBirth())
                .createdAt(LocalDateTime.now())
                .build();
        return authRepository.save(user);
    }

    @Override
    public JWTResponse login(UserLogin userLogin) {
        User user = authRepository.findByEmail(userLogin.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user!"));

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLogin.getEmail(),
                            userLogin.getPassword()
                    )
            );

            String accessToken = jwtProvider.generateToken(authentication.getName());

            return new JWTResponse(
                    user.getEmail(),
                    user.getFullName(),
                    user.getPhoneNumber(),
                    authentication.getAuthorities(),
                    accessToken
            );
        } catch (BadCredentialsException e) {
            Map<String, String> errors = new HashMap<>();
            errors.put("password", "Email hoặc mật khẩu không đúng!");
            throw new CustomValidationException(errors);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return authRepository.findAll();
    }
}
