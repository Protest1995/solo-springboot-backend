package com.solo.portfolio.repository;

import com.solo.portfolio.model.entity.PortfolioItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 作品集項目資料存儲庫介面
 * 提供對作品集項目實體的基本CRUD操作
 * 繼承自JpaRepository，實現了基本的資料庫操作方法
 * 
 * @see PortfolioItem 作品集項目實體
 * @see JpaRepository JPA資料庫操作介面
 */
public interface PortfolioItemRepository extends JpaRepository<PortfolioItem, String> {
    /**
     * 查找所有精選作品
     * @return 精選作品列表
     */
    List<PortfolioItem> findByIsFeaturedTrue();
    
    /**
     * 根據分類查找作品
     * @param categoryKey 分類鍵值
     * @return 指定分類的作品列表
     */
    List<PortfolioItem> findByCategoryKey(String categoryKey);
}


