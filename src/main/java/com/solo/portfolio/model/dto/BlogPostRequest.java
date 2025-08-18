package com.solo.portfolio.model.dto;

import lombok.Data;

/**
 * 部落格文章請求資料傳輸物件
 * 用於接收創建或更新部落格文章的請求資料
 * 支援中英文雙語內容
 */
@Data
public class BlogPostRequest {
    /**
     * 文章封面圖片URL
     */
    private String imageUrl;

    /**
     * 文章是否鎖定
     * true表示僅授權使用者可見
     */
    private Boolean isLocked;

    /**
     * 文章分類鍵值
     */
    private String categoryKey;

    /**
     * 是否為精選文章
     * true表示文章會在首頁突出顯示
     */
    private Boolean isFeatured;

    /**
     * 英文標題
     */
    private String title;

    /**
     * 中文標題
     */
    private String titleZh;

    /**
     * 英文摘要
     */
    private String excerpt;

    /**
     * 中文摘要
     */
    private String excerptZh;

    /**
     * 英文內容
     */
    private String content;

    /**
     * 中文內容
     */
    private String contentZh;
}
