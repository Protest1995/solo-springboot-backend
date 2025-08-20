package com.solo.portfolio.security.oauth;

import com.solo.portfolio.model.entity.RefreshToken;
import com.solo.portfolio.model.entity.User;
import com.solo.portfolio.model.entity.UserRole;
import com.solo.portfolio.repository.RefreshTokenRepository;
import com.solo.portfolio.repository.UserRepository;
import com.solo.portfolio.security.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * OAuth2認證成功處理器
 * 處理第三方登入（如Google、Facebook等）成功後的邏輯
 * 包括使用者資料的儲存和JWT權杖的生成
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    /**
     * JWT權杖提供者，用於生成存取權杖
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 使用者資料存儲庫
     */
    private final UserRepository userRepository;

    /**
     * 重整權杖存儲庫
     */
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 密碼加密工具
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * OAuth2認證成功後的前端重導向URL
     */
    @Value("${oauth2.frontend-success-url:http://localhost:5173/login}")
    private String frontendSuccessUrl;

    /**
     * 處理OAuth2認證成功的方法
     * 
     * @param request HTTP請求
     * @param response HTTP回應
     * @param authentication 認證資訊
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = String.valueOf(oAuth2User.getAttributes().getOrDefault("email", ""));
        String sub = String.valueOf(oAuth2User.getAttributes().getOrDefault("sub", "oauthUser"));
        String registrationId = String.valueOf(oAuth2User.getAttributes().getOrDefault("iss", ""));
        String login = String.valueOf(oAuth2User.getAttributes().getOrDefault("login", ""));
        String provider = "";
        if (oAuth2User.getAttributes().containsKey("iss") && oAuth2User.getAttributes().get("iss") != null) {
            String iss = oAuth2User.getAttributes().get("iss").toString();
            if (iss.contains("github")) provider = "github";
            else if (iss.contains("google")) provider = "google";
            else if (iss.contains("facebook")) provider = "facebook";
        }
        // fallback: try to detect github by login field
        if (provider.isEmpty() && oAuth2User.getAttributes().containsKey("login")) provider = "github";

        String preferredUsername = !email.isEmpty() ? email : sub;
        if (provider.equals("github")) {
            // github: login is username, email may be null
            if ((login != null && !login.equals("null") && !login.isEmpty())) {
                preferredUsername = login;
                if (email == null || email.isEmpty() || email.equals("null")) {
                    email = login + "@github.oauth";
                }
            }
        }

        // 檢查使用者是否已存在
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null && preferredUsername != null && !preferredUsername.equals("null") && !preferredUsername.isEmpty() && email != null && !email.equals("null") && !email.isEmpty()) {
            String baseUsername = preferredUsername.contains("@") ? preferredUsername.substring(0, preferredUsername.indexOf('@')) : preferredUsername;
            String candidate = baseUsername;
            int suffix = 1;
            while (userRepository.existsByUsername(candidate)) {
                candidate = baseUsername + suffix;
                suffix++;
            }
            user = new User();
            user.setUsername(candidate);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            user.setRole(UserRole.USER);
            Object picture = oAuth2User.getAttributes().get("picture");
            if (picture != null) user.setAvatarUrl(String.valueOf(picture));
            user = userRepository.save(user);
            log.info("Created user in success handler: {} ({})", user.getUsername(), user.getEmail());
        }

        String subject = user.getUsername();
        String accessToken = jwtTokenProvider.generateAccessToken(subject);
        String refreshToken = jwtTokenProvider.generateRefreshToken(subject);

        // 保存刷新令牌
        try { refreshTokenRepository.deleteByUserId(user.getId()); } catch (Exception ignored) {}
        RefreshToken rt = new RefreshToken();
        rt.setUserId(user.getId());
        rt.setToken(refreshToken);
        rt.setExpiresAt(LocalDateTime.now().plusSeconds(7 * 24 * 60 * 60));
        refreshTokenRepository.save(rt);

        // 前端為 SPA，將 token 以 URL fragment/hash 返回
        String redirectUrl = frontendSuccessUrl
                + "#token=" + urlEncode(accessToken)
                + "&refreshToken=" + urlEncode(refreshToken);

        log.info("OAuth2 success for user={} redirecting to frontend.", subject);
        response.sendRedirect(redirectUrl);
    }

    private static String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}


