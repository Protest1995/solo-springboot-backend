package com.solo.portfolio.security.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * OAuth2認證失敗處理器
 * 處理第三方登入失敗時的情況
 * 將使用者重導向到前端的錯誤頁面，並附帶錯誤訊息
 */
@Component
@Slf4j
public class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {
    
    /**
     * 認證失敗後重導向的前端URL
     * 從配置文件讀取，預設為本地開發環境URL
     */
    @Value("${oauth2.frontend-failure-url:http://localhost:5173/login}")
    private String frontendFailureUrl;

    /**
     * 處理認證失敗的方法
     * 
     * @param request HTTP請求
     * @param response HTTP回應
     * @param exception 認證失敗的異常
     * @throws IOException 重導向過程中可能發生的IO異常
     * @throws ServletException Servlet相關異常
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException exception) throws IOException, ServletException {
        // 記錄失敗原因
        log.error("OAuth2認證失敗: {}", exception.getMessage());
        // 重導向到前端錯誤頁面，並附帶編碼後的錯誤訊息
        response.sendRedirect(frontendFailureUrl + "#error=" + java.net.URLEncoder.encode(exception.getMessage(), java.nio.charset.StandardCharsets.UTF_8));
    }
}


