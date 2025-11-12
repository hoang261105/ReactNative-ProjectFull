package com.example.project_reactnative.model.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@NoArgsConstructor
public class JWTResponse {
    private Long id;
    private String email;
    private String fullName;
    private String avatar;
    private String phoneNumber;
    private Collection<? extends GrantedAuthority> authorities;
    private String accessToken;
    private String refreshToken;

    public JWTResponse(Long id, String email, String fullName, String avatar, String phoneNumber, Collection<? extends GrantedAuthority> authorities, String accessToken, String refreshToken) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.avatar = avatar;
        this.phoneNumber = phoneNumber;
        this.authorities = authorities;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}