package com.solo.portfolio.model.dto;

import com.solo.portfolio.model.entity.Gender;
import com.solo.portfolio.model.entity.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 使用者資料傳輸物件
 * 用於向前端傳送使用者資訊
 * 不包含敏感資訊如密碼
 */
@Data
public class UserDto {
    /**
     * 使用者唯一識別碼
     */
    private String id;

    /**
     * 使用者名稱
     */
    private String username;

    /**
     * 電子郵件地址
     */
    private String email;

    /**
     * 頭像圖片URL
     */
    private String avatarUrl;

    /**
     * 使用者角色
     */
    private UserRole role;

    /**
     * 性別
     */
    private Gender gender;

    /**
     * 生日
     */
    private LocalDateTime birthday;

    /**
     * 地址
     */
    private String address;

    /**
     * 電話號碼
     */
    private String phone;

    /**
     * 帳號建立時間
     */
    private LocalDateTime createdAt;

    /**
     * 最後更新時間
     */
    private LocalDateTime updatedAt;
} 