package com.solo.portfolio.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 作品集項目實體類
 * 用於儲存作品集中的各個項目資訊
 * 支援中英文雙語標題
 */
@Entity
@Table(name = "portfolio_items", schema = "public")
@Data
@NoArgsConstructor
public class PortfolioItem implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 作品唯一識別碼
     */
    @Id
    private String id;

    /**
     * 作品展示圖片URL
     * 最大長度1000個字元
     */
    @Column(length = 1000)
    private String imageUrl;
    
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
     * 作品分類鍵值
     * 最大長度100個字元
     */
    @Column(length = 100)
    private String categoryKey;
    
    /**
     * 作品瀏覽次數
     */
    private Integer views;

    /**
     * 是否為精選作品
     * true表示作品會在首頁突出顯示
     */
    private Boolean isFeatured;

    /**
     * 作品發表日期
     */
    private LocalDateTime date;

    /**
     * 記錄建立時間
     */
    private LocalDateTime createdAt;

    /**
     * 記錄最後更新時間
     */
    private LocalDateTime updatedAt;

    /**
     * 資料建立時的自動處理方法
     * 設定建立時間和更新時間為當前時間
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * 資料更新時的自動處理方法
     * 更新最後修改時間為當前時間
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}


