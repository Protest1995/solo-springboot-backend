package com.solo.portfolio.model.entity;

/**
 * 使用者角色列舉
 * 定義系統中的不同使用者權限等級
 */
public enum UserRole {
    /**
     * 一般使用者
     * 具有基本的系統存取權限
     */
    USER,
    
    /**
     * 管理員
     * 具有管理內容和一般使用者的權限
     */
    ADMIN,
    
    /**
     * 超級使用者
     * 具有最高權限，可以管理所有系統功能
     */
    SUPER_USER
} 