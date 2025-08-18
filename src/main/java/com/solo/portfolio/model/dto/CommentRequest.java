package com.solo.portfolio.model.dto;

import lombok.Data;

/**
 * 評論請求資料傳輸物件
 * 用於接收前端發送的評論創建請求
 */
@Data
public class CommentRequest {
    /**
     * 文章ID
     * 標識評論所屬的文章
     */
    private String postId;

    /**
     * 評論內容
     * 使用者輸入的評論文字
     */
    private String text;

    /**
     * 父評論ID
     * 若為回覆其他評論則填寫，否則為null
     * 用於實現評論的巢狀結構
     */
    private String parentId;
}



