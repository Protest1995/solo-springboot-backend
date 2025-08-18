package com.solo.portfolio.model.dto;

import lombok.Data;

/**
 * 使用者資料更新請求資料傳輸物件
 * 用於接收使用者資料的更新請求
 * 所有欄位皆為可選，未提供的欄位將保持原值不變
 */
@Data
public class UpdateUserRequest {
    /**
     * 新的使用者名稱
     */
    private String username;

    /**
     * 新的電子郵件地址
     */
    private String email;

    /**
     * 新的頭像URL
     */
    private String avatarUrl;

    /**
     * 性別
     * 可接受的值：
     * - MALE（男性）
     * - FEMALE（女性）
     * - OTHER（其他）
     * - NOT_SPECIFIED（未指定）
     * 可使用大寫或小寫
     */
    private String gender;

    /**
     * 生日
     * 格式：yyyy-MM-dd 或 yyyy/MM/dd
     * 例如：2000-01-01 或 2000/01/01
     */
    private String birthday;

    /**
     * 新的地址
     */
    private String address;

    /**
     * 新的電話號碼
     */
    private String phone;

    /**
     * 新的密碼
     * 若提供則會更新密碼
     * 若為空則保持原密碼不變
     */
    private String password;
}


