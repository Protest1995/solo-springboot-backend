package com.solo.portfolio.repository;

import com.solo.portfolio.model.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 部落格文章資料存儲庫介面
 * 提供對部落格文章實體的基本CRUD操作
 * 繼承自JpaRepository，實現了基本的資料庫操作方法
 * 
 * @see BlogPost 部落格文章實體
 * @see JpaRepository JPA資料庫操作介面
 */
public interface BlogPostRepository extends JpaRepository<BlogPost, String> {}


