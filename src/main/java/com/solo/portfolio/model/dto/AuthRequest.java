package com.solo.portfolio.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 認證請求資料傳輸物件
 * 用於接收使用者登入時提交的認證資訊
 */
@Data
public class AuthRequest {
    
    /**
     * 使用者名稱
     * 不能為空，長度必須在3-50個字元之間
     */
    @NotBlank(message = "使用者名稱不能為空")
    @Size(min = 3, max = 50, message = "使用者名稱長度必須在3-50個字元之間")
    private String username;
    
    /**
     * 使用者密碼
     * 不能為空，長度至少6個字元
     */
    @NotBlank(message = "密碼不能為空")
    @Size(min = 6, message = "密碼長度至少需要6個字元")
    private String password;
} 