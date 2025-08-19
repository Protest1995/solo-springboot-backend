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
        // 放行預檢請求，避免 CORS 在 Security 層被擋
        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

        // 公開端點（依你的實際路由調整）
        .requestMatchers("/auth/**").permitAll()
        .requestMatchers("/content/**").permitAll()
        .requestMatchers("/comments/post/**").permitAll()
        // 若你的前端會讀取 /api/posts、/api/portfolio，放行 GET
        .requestMatchers(HttpMethod.GET, "/api/posts/**", "/api/portfolio/**").permitAll()

        // Swagger
        .requestMatchers(
          "/v3/api-docs/**",
          "/swagger-ui.html",
          "/swagger-ui/**"
        ).permitAll()

        // OAuth2
        .requestMatchers("/login/oauth2/**", "/oauth2/**").permitAll()

        // 其他需要認證
        .requestMatchers("/comments/**").authenticated()
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

    // 加入本機與正式網域
    List<String> allowedOrigins = Arrays.asList(
      "https://solo-react-frontend.vercel.app", // Production
      "http://localhost:5173"                    // 本機開發（Vite）      
    );
    configuration.setAllowedOrigins(allowedOrigins);

    configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("Content-Type","Authorization","Accept","Origin","X-Requested-With"));

    // 若前端沒有要帶 cookie/session，建議先關閉（降低 CORS 難度）
    configuration.setAllowCredentials(false);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
