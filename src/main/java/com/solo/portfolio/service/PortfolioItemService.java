package com.solo.portfolio.service;

import com.solo.portfolio.model.entity.PortfolioItem;
import com.solo.portfolio.repository.PortfolioItemRepository;
import com.solo.portfolio.service.cache.PortfolioItemCacheService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * 作品集服務類
 */
@Service
public class PortfolioItemService {
    
    private final PortfolioItemRepository portfolioItemRepository;
    private final PortfolioItemCacheService portfolioItemCacheService;
    
    public PortfolioItemService(PortfolioItemRepository portfolioItemRepository,
                              PortfolioItemCacheService portfolioItemCacheService) {
        this.portfolioItemRepository = portfolioItemRepository;
        this.portfolioItemCacheService = portfolioItemCacheService;
    }
    
    /**
     * 獲取單個作品項目
     * 優先從緩存中獲取，如果緩存中不存在則從數據庫獲取並緩存
     */
    public Optional<PortfolioItem> getPortfolioItem(String id) {
        // 嘗試從緩存獲取
        Optional<PortfolioItem> cachedItem = portfolioItemCacheService.getCachedPortfolioItem(id);
        if (cachedItem.isPresent()) {
            return cachedItem;
        }
        
        // 從數據庫獲取
        Optional<PortfolioItem> item = portfolioItemRepository.findById(id);
        item.ifPresent(portfolioItemCacheService::cachePortfolioItem);
        
        return item;
    }
    
    /**
     * 獲取精選作品列表
     */
    public List<PortfolioItem> getFeaturedItems() {
        // 嘗試從緩存獲取
        Optional<List<PortfolioItem>> cachedItems = portfolioItemCacheService.getCachedFeaturedItems();
        if (cachedItems.isPresent()) {
            return cachedItems.get();
        }
        
        // 從數據庫獲取
        List<PortfolioItem> featuredItems = portfolioItemRepository.findByIsFeaturedTrue();
        portfolioItemCacheService.cacheFeaturedItems(featuredItems);
        
        return featuredItems;
    }
    
    /**
     * 根據分類獲取作品列表
     */
    public List<PortfolioItem> getItemsByCategory(String categoryKey) {
        // 嘗試從緩存獲取
        Optional<List<PortfolioItem>> cachedItems = portfolioItemCacheService.getCachedCategoryItems(categoryKey);
        if (cachedItems.isPresent()) {
            return cachedItems.get();
        }
        
        // 從數據庫獲取
        List<PortfolioItem> items = portfolioItemRepository.findByCategoryKey(categoryKey);
        portfolioItemCacheService.cacheCategoryItems(categoryKey, items);
        
        return items;
    }
    
    /**
     * 保存或更新作品項目
     */
    @Transactional
    public PortfolioItem savePortfolioItem(PortfolioItem portfolioItem) {
        PortfolioItem savedItem = portfolioItemRepository.save(portfolioItem);
        // 更新緩存
        portfolioItemCacheService.cachePortfolioItem(savedItem);
        return savedItem;
    }
    
    /**
     * 刪除作品項目
     */
    @Transactional
    public void deletePortfolioItem(String id) {
        portfolioItemRepository.deleteById(id);
        // 刪除緩存
        portfolioItemCacheService.deletePortfolioItemCache(id);
    }
}
