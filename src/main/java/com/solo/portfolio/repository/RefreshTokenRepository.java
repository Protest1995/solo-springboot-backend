package com.solo.portfolio.repository;

import com.solo.portfolio.model.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 重整權杖資料存儲庫介面
 * 提供對重整權杖的資料庫操作方法
 * 包含基本CRUD操作和特定的查詢方法
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    
    /**
     * 根據權杖字串查找重整權杖
     * @param token 權杖字串
     * @return 重整權杖的Optional包裝
     */
    Optional<RefreshToken> findByToken(String token);
    
    /**
     * 根據使用者ID查找重整權杖
     * 用於驗證使用者的重整權杖
     */
    Optional<RefreshToken> findByUserId(String userId);
    
    /**
     * 刪除過期的刷新令牌
     */
    void deleteByExpiresAtBefore(LocalDateTime now);
    
    /**
     * 根據用戶ID刪除刷新令牌
     */
    void deleteByUserId(String userId);
} 