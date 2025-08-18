package com.solo.portfolio.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 部落格文章實體類
 * 用於儲存部落格文章的所有相關資訊
 * 支援中英文雙語內容
 */
@Entity
@Table(name = "blog_posts", schema = "public")
@Data
@NoArgsConstructor
public class BlogPost {
    /**
     * 文章唯一識別碼
     */
    @Id
    private String id;

    /**
     * 文章封面圖片URL
     * 最大長度1000個字元
     */
    @Column(length = 1000)
    private String imageUrl;
    
    /**
     * 文章是否被鎖定
     * true表示僅授權用戶可見
     */
    private Boolean isLocked;

    /**
     * 文章建立時間戳
     * 以毫秒為單位
     */
    private Long createdAt;
    
    /**
     * 文章分類鍵值
     * 最大長度100個字元
     */
    @Column(length = 100)
    private String categoryKey;
    
    /**
     * 文章獲得的讚數
     */
    private Integer likes;

    /**
     * 文章的評論數量
     */
    private Integer commentsCount;

    /**
     * 文章的瀏覽次數
     */
    private Integer views;

    /**
     * 是否為精選文章
     * true表示文章會在首頁突出顯示
     */
    private Boolean isFeatured;

    /**
     * 英文標題
     * 最大長度500個字元
     */
    @Column(length = 500)
    private String title;
    
    /**
     * 中文標題
     * 最大長度500個字元
     */
    @Column(length = 500)
    private String titleZh;
    
    /**
     * 英文摘要
     * 使用TEXT類型以支援長文本
     */
    @Column(columnDefinition = "TEXT")
    private String excerpt;
    
    /**
     * 中文摘要
     * 使用TEXT類型以支援長文本
     */
    @Column(columnDefinition = "TEXT")
    private String excerptZh;
    
    /**
     * 英文內容
     * 使用TEXT類型以支援長文本
     */
    @Column(columnDefinition = "TEXT")
    private String content;
    
    /**
     * 中文內容
     * 使用TEXT類型以支援長文本
     */
    @Column(columnDefinition = "TEXT")
    private String contentZh;

    private LocalDateTime date;

    @Column(name = "created_at_ts")
    private LocalDateTime createdAtTs;

    @PrePersist
    protected void onCreate() {
        createdAtTs = LocalDateTime.now();
    }
}


