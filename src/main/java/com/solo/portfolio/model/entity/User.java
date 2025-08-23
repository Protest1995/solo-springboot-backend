package com.solo.portfolio.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 使用者實體類
 * 實現Spring Security的UserDetails介面，用於認證和授權
 * 儲存使用者的基本資料和認證資訊
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class User implements UserDetails {
    
    /**
     * 使用者唯一識別碼
     * 使用UUID自動生成
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    /**
     * 使用者名稱
     * 唯一且必填，最大長度50個字元
     */
    @Column(unique = true, nullable = false, length = 50)
    private String username;
    
    /**
     * 電子郵件地址
     * 唯一且必填，最大長度100個字元
     */
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    
    /**
     * 使用者密碼
     * 必填，已加密儲存
     */
    @Column(nullable = false)
    private String password;
    
    /**
     * 使用者頭像URL
     * 可選，最大長度500個字元
     */
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;
    
    /**
     * 使用者角色
     * 必填，預設為一般使用者（USER）
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;
    
    /**
     * 使用者性別
     * 可選，使用Gender列舉類型
     */
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    /**
     * 使用者生日
     * 可選，儲存日期和時間
     */
    @Column(name = "birthday")
    private LocalDateTime birthday;
    
    /**
     * 使用者地址
     * 可選，最大長度500個字元
     */
    @Column(length = 500)
    private String address;
    
    @Column(length = 20)
    private String phone;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // UserDetails 實現
    @Override
    @JsonIgnoreProperties
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            return List.of();
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
} 