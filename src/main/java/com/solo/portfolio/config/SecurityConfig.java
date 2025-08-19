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
import org.springframework.web.filter.ForwardedHeaderFilter;

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
        // 1) 預檢
        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

        // 2) Swagger（務必放行）
        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()

        // 3) OAuth2
        .requestMatchers("/login/oauth2/**", "/oauth2/**").permitAll()

        // 4) 公開 GET 端點 —— 統一含 /api 前綴（依你的實際路由調整）
        .requestMatchers(HttpMethod.GET, "/api/posts/**", "/api/portfolio/**", "/api/comments/post/**").permitAll()
        .requestMatchers("/auth/**").permitAll()

        // 5) 寫操作需認證
        .requestMatchers(HttpMethod.POST, "/api/comments/**").authenticated()
        .requestMatchers(HttpMethod.PUT, "/api/comments/**").authenticated()
        .requestMatchers(HttpMethod.DELETE, "/api/comments/**").authenticated()

        // 其他預設需認證
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

    configuration.setAllowedOrigins(List.of(
      "https://solo-react-frontend.vercel.app",
      "http://localhost:5173",
      "https://solo-springboot-backend-production.up.railway.app"
    ));
    configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("Content-Type","Authorization","Accept","Origin","X-Requested-With"));
    configuration.setExposedHeaders(Arrays.asList("Location","Link"));
    configuration.setAllowCredentials(false); // 與前端對齊，無 cookie

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  // 讓 Spring 依據代理標頭正確推斷外部 URL scheme/host（修正 Swagger 生成 http 的問題）
  @Bean
  public ForwardedHeaderFilter forwardedHeaderFilter() {
    return new ForwardedHeaderFilter();
  }
}
