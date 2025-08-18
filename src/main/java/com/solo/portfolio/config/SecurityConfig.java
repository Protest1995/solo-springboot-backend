package com.solo.portfolio.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import com.solo.portfolio.security.oauth.CustomOAuth2UserService;
import com.solo.portfolio.security.oauth.OAuth2AuthenticationFailureHandler;
import com.solo.portfolio.security.oauth.OAuth2AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * 安全性配置類
 * 負責配置整個應用程式的安全性設定
 * 包括認證、授權、CORS、CSRF等安全機制
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    /**
     * 自定義OAuth2使用者服務
     * 處理第三方登入的使用者資訊
     */
    private final CustomOAuth2UserService customOAuth2UserService;

    /**
     * OAuth2認證成功處理器
     */
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    /**
     * OAuth2認證失敗處理器
     */
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    /**
     * 配置安全性過濾鏈
     * 定義各種HTTP請求的安全性規則
     *
     * @param http HttpSecurity配置物件
     * @return 配置完成的SecurityFilterChain
     * @throws Exception 配置過程中可能發生的異常
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 啟用CORS跨域支援
            .cors().and()
            // 關閉CSRF防護，因為使用JWT進行認證
            .csrf().disable()
            // 配置session管理，OAuth2需要session來儲存授權請求
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .and()
            .authorizeHttpRequests(authz -> authz
                // 允許所有人訪問認證相關端點
                .requestMatchers("/auth/**").permitAll()
                // 允許所有人訪問內容相關端點
                .requestMatchers("/content/**").permitAll()
                // 允許所有人查看文章評論
                .requestMatchers("/comments/post/**").permitAll()
                // 允許訪問Swagger/OpenAPI文件
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**"
                ).permitAll()
                // 評論操作需要認證
                .requestMatchers("/comments/**").authenticated()
                // 允許所有人訪問OAuth2相關端點
                .requestMatchers("/login/oauth2/**", "/oauth2/**").permitAll()
                // 其他所有請求都需要認證
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
            );
        
        return http.build();
    }
    
    /**
     * 配置認證管理器
     * 用於處理使用者認證流程
     *
     * @param authConfig 認證配置
     * @return 認證管理器實例
     * @throws Exception 配置過程中可能發生的異常
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    // PasswordEncoder 由 PasswordConfig 提供，避免循環依賴
    
    /**
     * 配置CORS跨域資源共享
     * 允許前端應用程式從不同域名訪問API
     *
     * @return CORS配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允許所有來源
        configuration.setAllowedOriginPatterns(Arrays.asList("https://solo-react-frontend.vercel.app"));
        // 允許的HTTP方法
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 允許所有請求頭
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // 允許攜帶認證信息（cookies等）
        configuration.setAllowCredentials(true);
        
        // 註冊CORS配置到所有路徑
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
} 