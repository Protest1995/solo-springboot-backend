package com.solo.portfolio.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomOAuth2UserService customOAuth2UserService;
  private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
  private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .cors().and()
      .csrf().disable()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
      .and()
      .authorizeHttpRequests(authz -> authz
        // 1) 放行預檢，避免在 Security 層攔下 CORS
        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

        // 2) 公開讀取端點（依你的實際後端路由調整）
        .requestMatchers("/auth/**").permitAll()
        
        .requestMatchers(HttpMethod.GET, "/posts/**", "/portfolio/**").permitAll()
        // 若你的留言查詢路徑是 /api/comments/post/{id}，請改成下一行：
        //.requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/comments/post/**").permitAll()

        // 3) Swagger
        .requestMatchers(
          "/v3/api-docs/**",
          "/swagger-ui.html",
          "/swagger-ui/**"
        ).permitAll()

        // 4) OAuth2
        .requestMatchers("/login/oauth2/**", "/oauth2/**").permitAll()

        // 5) 寫操作與其他需認證的端點
        .requestMatchers(HttpMethod.POST, "/comments/**").authenticated()
        .requestMatchers(HttpMethod.PUT, "/comments/**").authenticated()
        .requestMatchers(HttpMethod.DELETE, "/comments/**").authenticated()

        // 覆蓋不到的其它端點，預設需認證
        .anyRequest().authenticated()
      )
      .oauth2Login(oauth2 -> oauth2
        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
        .successHandler(oAuth2AuthenticationSuccessHandler)
        .failureHandler(oAuth2AuthenticationFailureHandler)
      );

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    // 若你想同時支援固定網域與未來的預覽域名，建議用 AllowedOriginPatterns。
    // 這裡保留明確白名單，避免 credentials 啟用時踩規則限制。
    List<String> allowedOrigins = Arrays.asList(
      "https://solo-react-frontend.vercel.app",
      "http://localhost:5173",
      "https://solo-react-frontend.vercel.app",
      // 如需 Vercel 預覽，可加：
      // "https://*.vercel.app"
    );
    // 二選一：建議優先用 setAllowedOrigins（更嚴格）
    configuration.setAllowedOrigins(allowedOrigins);
    // 若你確定需要萬用預覽域，才改用 Patterns（並請保持 allowCredentials=false）：
    // configuration.setAllowedOriginPatterns(Arrays.asList("https://solo-react-frontend.vercel.app", "http://localhost:5173", "https://*.vercel.app"));

    configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("Content-Type","Authorization","Accept","Origin","X-Requested-With"));

    // 若前端要讀自訂回應標頭（可選）
    configuration.setExposedHeaders(Arrays.asList("Location","Link"));

    // 與前端對齊：不帶 cookies/credentials，降低 CORS 複雜度
    configuration.setAllowCredentials(false);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
