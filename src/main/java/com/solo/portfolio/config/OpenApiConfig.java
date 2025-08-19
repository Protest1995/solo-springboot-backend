package com.solo.portfolio.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition(
  info = @Info(
    title = "作品集 API",
    version = "v1",
    description = "作品集後端API文件，支援CRUD操作和認證功能。",
    contact = @Contact(name = "Solo", email = "")
  )
)
@SecurityScheme(
  name = OpenApiConfig.BEARER_SCHEME_NAME,
  type = SecuritySchemeType.HTTP,
  scheme = "bearer",
  bearerFormat = "JWT"
)
public class OpenApiConfig {
  public static final String BEARER_SCHEME_NAME = "bearerAuth";

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI().servers(List.of(
      new Server().url("https://solo-springboot-backend-production.up.railway.app")
    ));
  }
}
