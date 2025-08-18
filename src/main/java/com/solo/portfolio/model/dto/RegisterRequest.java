package com.solo.portfolio.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 使用者註冊請求資料傳輸物件
 * 用於接收新使用者的註冊資訊
 * 包含資料驗證規則
 */
@Data
public class RegisterRequest {
    
    /**
     * 使用者名稱
     * 不能為空，長度必須在3-50個字元之間
     */
    @NotBlank(message = "使用者名稱不能為空")
    @Size(min = 3, max = 50, message = "使用者名稱長度必須在3-50個字元之間")
    private String username;
    
    /**
     * 電子郵件
     * 不能為空，必須符合電子郵件格式
     */
    @NotBlank(message = "電子郵件不能為空")
    @Email(message = "電子郵件格式不正確")
    private String email;
    
    /**
     * 密碼
     * 不能為空，長度至少6個字元
     */
    @NotBlank(message = "密碼不能為空")
    @Size(min = 6, message = "密碼長度至少需要6個字元")
    private String password;
    
    /**
     * 確認密碼
     * 必須與密碼欄位相符
     */
    @NotBlank(message = "確認密碼不能為空")
    private String confirmPassword;
} 