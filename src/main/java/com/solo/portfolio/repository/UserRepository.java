package com.solo.portfolio.repository;

import com.solo.portfolio.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 使用者資料存儲庫介面
 * 提供使用者實體的資料庫操作方法
 * 包含基本的CRUD操作和自定義查詢方法
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    /**
     * 根據使用者名稱查找使用者
     * 用於使用者登入和身份驗證
     *
     * @param username 使用者名稱
     * @return 包含使用者資訊的Optional物件
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 根據電子郵件查找使用者
     * 用於電子郵件驗證和OAuth2登入
     *
     * @param email 電子郵件地址
     * @return 包含使用者資訊的Optional物件
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 檢查使用者名稱是否已被使用
     * 用於使用者註冊時的唯一性驗證
     *
     * @param username 要檢查的使用者名稱
     * @return 如果使用者名稱已存在返回true，否則返回false
     */
    boolean existsByUsername(String username);
    
    /**
     * 檢查電子郵件是否已被使用
     * 用於使用者註冊時的唯一性驗證
     *
     * @param email 要檢查的電子郵件地址
     * @return 如果電子郵件已存在返回true，否則返回false
     */
    boolean existsByEmail(String email);
} 