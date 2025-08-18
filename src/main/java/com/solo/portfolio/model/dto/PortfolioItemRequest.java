package com.solo.portfolio.model.dto;

import lombok.Data;

/**
 * 作品集項目請求資料傳輸物件
 * 用於接收創建或更新作品集項目的請求資料
 * 支援中英文雙語標題
 */
@Data
public class PortfolioItemRequest {
    /**
     * 作品展示圖片URL
     */
    private String imageUrl;

    /**
     * 英文標題
     */
    private String title;

    /**
     * 中文標題
     */
    private String titleZh;

    /**
     * 作品分類鍵值
     */
    private String categoryKey;

    /**
     * 是否為精選作品
     * true表示作品會在首頁突出顯示
     */
    private Boolean isFeatured;
}
