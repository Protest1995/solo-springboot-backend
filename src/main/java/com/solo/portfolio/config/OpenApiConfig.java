package com.solo.portfolio.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI (Swagger) 配置類
 * 提供API文件的自動生成配置
 * 包含API基本資訊和安全性設定
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "作品集 API",
        version = "v1",
        description = "作品集後端API文件，支援CRUD操作和認證功能。",
        contact = @Contact(name = "Solo", email = "")
    )
)
/**
 * JWT Bearer Token 認證配置
 * 設定API文件中的安全性驗證方式
 */
@SecurityScheme(
    name = OpenApiConfig.BEARER_SCHEME_NAME,
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class OpenApiConfig {
    /**
     * Bearer認證方案名稱
     * 用於在API端點上標註需要認證的部分
     */
    public static final String BEARER_SCHEME_NAME = "bearerAuth";
}



