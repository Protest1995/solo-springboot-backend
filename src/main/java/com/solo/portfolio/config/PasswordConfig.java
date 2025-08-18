package com.solo.portfolio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密碼編碼配置類
 * 負責配置系統中使用的密碼加密方式
 */
@Configuration
public class PasswordConfig {
    /**
     * 配置密碼編碼器
     * 使用BCrypt演算法進行密碼加密
     * BCrypt是一種自適應的加密函數，
     * 內建加鹽機制，並且可以調整加密強度
     *
     * @return BCryptPasswordEncoder實例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


