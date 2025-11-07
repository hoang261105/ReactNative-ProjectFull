package com.example.project_reactnative.model.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@NoArgsConstructor
public class JWTResponse {
    private String email;
    private String fullName;
    private String phoneNumber;
    private Collection<? extends GrantedAuthority> authorities;
    private String accessToken;

    public JWTResponse(String email, String fullName, String phoneNumber, Collection<? extends GrantedAuthority> authorities, String accessToken) {
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.authorities = authorities;
        this.accessToken = accessToken;
    }
}