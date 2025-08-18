package com.solo.portfolio.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 重整權杖實體類
 * 用於存儲和管理JWT重整權杖
 * 支援使用者的長期登入狀態維護
 */
@Entity
@Table(name = "refresh_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    
    /**
     * 重整權杖記錄的唯一識別碼
     * 使用UUID自動生成
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    /**
     * 關聯的使用者ID
     * 指向擁有此重整權杖的使用者
     */
    @Column(name = "user_id", nullable = false)
    private String userId;
    
    /**
     * 重整權杖字串
     * 必須是唯一的，用於驗證重整請求
     */
    @Column(nullable = false, unique = true)
    private String token;
    
    /**
     * 權杖過期時間
     * 用於判斷重整權杖是否仍然有效
     */
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    
    /**
     * 權杖建立時間
     * 記錄時自動設定，之後不可修改
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 