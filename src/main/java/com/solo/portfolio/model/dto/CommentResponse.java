package com.solo.portfolio.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 評論回應資料傳輸物件
 * 用於向前端返回評論的完整資訊
 */
@Data
@AllArgsConstructor
public class CommentResponse {
    /**
     * 評論唯一識別碼
     */
    private String id;

    /**
     * 文章ID
     * 標識評論所屬的文章
     */
    private String postId;

    /**
     * 評論作者的使用者ID
     */
    private String userId;

    /**
     * 評論作者的使用者名稱
     */
    private String username;

    /**
     * 評論作者的頭像URL
     */
    private String avatarUrl;

    /**
     * 評論發布時間
     * 以ISO格式字串表示，方便前端處理
     */
    private String date;

    /**
     * 評論內容
     */
    private String text;

    /**
     * 父評論ID
     * 若為回覆則有值，否則為null
     */
    private String parentId;
}



