package com.example.project_reactnative.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JWTProvider {
    @Value("${jwt_secret}")
    private String jwtSecret;

    @Value("${jwt_expire}")
    private int jwtExpire;

    @Value("${jwt_refresh}")
    private int jwtRefresh;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email){
        Date now = new Date();
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtExpire))
                .signWith(getSigningKey())
                .compact();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token){
        try {
            parseClaims(token);
            return true;
        } catch (SignatureException e) {
            log.error("Chữ ký JWT không hợp lệ!");
        } catch (MalformedJwtException e) {
            log.error("Token JWT không đúng định dạng!");
        } catch (ExpiredJwtException e) {
            log.error("JWT đã hêt hạn!");
        } catch (UnsupportedJwtException e){
            log.error("JWT token không được hỗ trợ!");
        } catch (IllegalArgumentException e){
            log.error("Chuỗi JWT rỗng hoặc không hợp lệ!");
        }
        return false;
    }

    public String getEmailFromToken(String token){
        return parseClaims(token).getSubject();
    }

    public String generateRefreshToken(String email, int refreshDurationMs) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshDurationMs))
                .signWith(getSigningKey())
                .compact();
    }
}