package com.solo.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 作品集應用程式的主要入口點
 * 使用 Spring Boot 框架開發的後端應用程式
 */
@SpringBootApplication
@EnableCaching  // 啟用Spring緩存支持
public class PortfolioApplication {

    /**
     * 應用程式的主要執行方法
     * @param args 命令列參數
     */
    public static void main(String[] args) {
        SpringApplication.run(PortfolioApplication.class, args);
    }
} 