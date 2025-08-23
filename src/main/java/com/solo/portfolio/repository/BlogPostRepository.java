package com.solo.portfolio.repository;

import com.solo.portfolio.model.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 部落格文章資料存儲庫介面
 * 提供對部落格文章實體的基本CRUD操作
 * 繼承自JpaRepository，實現了基本的資料庫操作方法
 * 
 * @see BlogPost 部落格文章實體
 * @see JpaRepository JPA資料庫操作介面
 */
public interface BlogPostRepository extends JpaRepository<BlogPost, String> {
    /**
     * 查找所有未鎖定的文章
     * @return 未鎖定的文章列表
     */
    List<BlogPost> findByIsLockedFalse();
    
    /**
     * 根據分類查找文章
     * @param categoryKey 分類鍵值
     * @return 指定分類的文章列表
     */
    List<BlogPost> findByCategoryKey(String categoryKey);
    
    /**
     * 查找熱門文章（根據瀏覽次數排序）
     * @return 前10篇最熱門的文章
     */
    List<BlogPost> findTop10ByOrderByViewsDesc();
}


