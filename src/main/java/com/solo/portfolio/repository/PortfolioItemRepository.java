package com.solo.portfolio.repository;

import com.solo.portfolio.model.entity.PortfolioItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 作品集項目資料存儲庫介面
 * 提供對作品集項目實體的基本CRUD操作
 * 繼承自JpaRepository，實現了基本的資料庫操作方法
 * 
 * @see PortfolioItem 作品集項目實體
 * @see JpaRepository JPA資料庫操作介面
 */
public interface PortfolioItemRepository extends JpaRepository<PortfolioItem, String> {}


