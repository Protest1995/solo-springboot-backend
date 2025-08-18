package com.solo.portfolio.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 認證回應資料傳輸物件
 * 用於向前端返回認證結果和相關資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    /**
     * 認證是否成功
     */
    private boolean success;

    /**
     * 回應訊息
     * 可能包含成功訊息或錯誤描述
     */
    private String message;

    /**
     * JWT存取權杖
     * 用於後續請求的認證
     */
    private String token;

    /**
     * JWT重整權杖
     * 用於獲取新的存取權杖
     */
    private String refreshToken;

    /**
     * 使用者資訊
     * 包含使用者基本資料
     */
    private UserDto user;
} 