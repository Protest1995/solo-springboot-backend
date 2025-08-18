package com.solo.portfolio.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JWT權杖提供者
 * 負責處理所有與JWT（JSON Web Token）相關的操作，包括：
 * - 生成存取權杖和重整權杖
 * - 驗證權杖的有效性
 * - 從權杖中提取使用者資訊
 */
@Component
@Slf4j
public class JwtTokenProvider {
    
    /**
     * JWT的加密密鑰
     * 從配置文件中讀取
     */
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    /**
     * 存取權杖的有效期限（毫秒）
     * 從配置文件中讀取
     */
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    
    /**
     * 重整權杖的有效期限（毫秒）
     * 從配置文件中讀取
     */
    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;
    
    /**
     * 根據Spring Security的Authentication物件生成存取權杖
     * 
     * @param authentication Spring Security的認證物件
     * @return 生成的JWT存取權杖
     */
    public String generateAccessToken(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String username = (principal instanceof UserDetails)
                ? ((UserDetails) principal).getUsername()
                : String.valueOf(principal);
        return generateToken(username, jwtExpiration);
    }

    /**
     * 根據使用者名稱直接生成存取權杖
     * 
     * @param username 使用者名稱
     * @return 生成的JWT存取權杖
     */
    public String generateAccessToken(String username) {
        return generateToken(username, jwtExpiration);
    }
    
    /**
     * 生成重整權杖
     * 用於在存取權杖過期後獲取新的存取權杖
     * 
     * @param username 使用者名稱
     * @return 生成的JWT重整權杖
     */
    public String generateRefreshToken(String username) {
        return generateToken(username, refreshExpiration);
    }
    
    /**
     * 生成令牌
     */
    private String generateToken(String username, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    
    /**
     * 從令牌中獲取用戶名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return claims.getSubject();
    }
    
    /**
     * 驗證令牌
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 獲取簽名密鑰
     */
    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
} 