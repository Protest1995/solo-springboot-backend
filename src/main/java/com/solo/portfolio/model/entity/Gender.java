package com.solo.portfolio.model.entity;

/**
 * 性別列舉
 * 定義系統中支援的性別選項
 * 包含多元性別選項以尊重多樣性
 */
public enum Gender {
    /**
     * 男性
     */
    MALE,
    
    /**
     * 女性
     */
    FEMALE,
    
    /**
     * 其他性別
     * 用於不屬於傳統二元性別的選項
     */
    OTHER,
    
    /**
     * 未指定
     * 使用者選擇不透露性別時使用
     */
    NOT_SPECIFIED
} 