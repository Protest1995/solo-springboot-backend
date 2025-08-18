package com.solo.portfolio.service;

import com.solo.portfolio.model.entity.User;
import com.solo.portfolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 使用者詳細資訊服務實作類
 * 實現 Spring Security 的 UserDetailsService 介面
 * 用於處理使用者認證相關的邏輯
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    
    /**
     * 使用者資料存儲庫
     */
    private final UserRepository userRepository;
    
    /**
     * 根據使用者名稱載入使用者詳細資訊
     * @param username 使用者名稱
     * @return 使用者詳細資訊
     * @throws UsernameNotFoundException 當使用者不存在時拋出此異常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("使用者不存在: " + username));
        
        return user;
    }
} 