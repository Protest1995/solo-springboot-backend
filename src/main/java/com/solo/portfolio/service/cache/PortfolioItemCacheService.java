package com.solo.portfolio.service.cache;

import com.solo.portfolio.constants.RedisCacheConstants;
import com.solo.portfolio.model.entity.PortfolioItem;
import com.solo.portfolio.service.RedisService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * 作品集緩存服務
 */
@Service
public class PortfolioItemCacheService {
    
    private final RedisService redisService;
    
    public PortfolioItemCacheService(RedisService redisService) {
        this.redisService = redisService;
    }
    
    /**
     * 獲取緩存的作品集項目
     * @param id 作品ID
     * @return Optional包裝的作品項目
     */
    public Optional<PortfolioItem> getCachedPortfolioItem(String id) {
        try {
            Object cached = redisService.get(RedisCacheConstants.PORTFOLIO_ITEM_KEY + id);
            if (cached instanceof PortfolioItem) {
                return Optional.of((PortfolioItem) cached);
            }
        } catch (Exception e) {
            // 緩存操作失敗時，記錄日誌但不影響主流程
            return Optional.empty();
        }
        return Optional.empty();
    }
    
    /**
     * 緩存作品集項目
     * @param portfolioItem 要緩存的作品項目
     */
    public void cachePortfolioItem(PortfolioItem portfolioItem) {
        try {
            redisService.set(
                RedisCacheConstants.PORTFOLIO_ITEM_KEY + portfolioItem.getId(),
                portfolioItem,
                RedisCacheConstants.PORTFOLIO_CACHE_TIME
            );
        } catch (Exception e) {
            // 緩存操作失敗時，記錄日誌但不影響主流程
        }
    }
    
    /**
     * 刪除作品集項目緩存
     * @param id 作品ID
     */
    public void deletePortfolioItemCache(String id) {
        try {
            redisService.delete(RedisCacheConstants.PORTFOLIO_ITEM_KEY + id);
        } catch (Exception e) {
            // 緩存操作失敗時，記錄日誌但不影響主流程
        }
    }
    
    /**
     * 緩存精選作品列表
     * @param portfolioItems 精選作品列表
     */
    public void cacheFeaturedItems(List<PortfolioItem> portfolioItems) {
        try {
            redisService.set(
                RedisCacheConstants.PORTFOLIO_FEATURED,
                portfolioItems,
                RedisCacheConstants.PORTFOLIO_CACHE_TIME
            );
        } catch (Exception e) {
            // 緩存操作失敗時，記錄日誌但不影響主流程
        }
    }
    
    /**
     * 獲取緩存的精選作品列表
     * @return Optional包裝的作品列表
     */
    @SuppressWarnings("unchecked")
    public Optional<List<PortfolioItem>> getCachedFeaturedItems() {
        try {
            Object cached = redisService.get(RedisCacheConstants.PORTFOLIO_FEATURED);
            if (cached instanceof List<?>) {
                return Optional.of((List<PortfolioItem>) cached);
            }
        } catch (Exception e) {
            // 緩存操作失敗時，記錄日誌但不影響主流程
            return Optional.empty();
        }
        return Optional.empty();
    }
    
    /**
     * 緩存分類作品列表
     * @param categoryKey 分類鍵值
     * @param portfolioItems 該分類的作品列表
     */
    public void cacheCategoryItems(String categoryKey, List<PortfolioItem> portfolioItems) {
        try {
            redisService.set(
                RedisCacheConstants.PORTFOLIO_CATEGORY + categoryKey,
                portfolioItems,
                RedisCacheConstants.PORTFOLIO_CACHE_TIME
            );
        } catch (Exception e) {
            // 緩存操作失敗時，記錄日誌但不影響主流程
        }
    }
    
    /**
     * 獲取緩存的分類作品列表
     * @param categoryKey 分類鍵值
     * @return Optional包裝的作品列表
     */
    @SuppressWarnings("unchecked")
    public Optional<List<PortfolioItem>> getCachedCategoryItems(String categoryKey) {
        try {
            Object cached = redisService.get(RedisCacheConstants.PORTFOLIO_CATEGORY + categoryKey);
            if (cached instanceof List<?>) {
                return Optional.of((List<PortfolioItem>) cached);
            }
        } catch (Exception e) {
            // 緩存操作失敗時，記錄日誌但不影響主流程
            return Optional.empty();
        }
        return Optional.empty();
    }
}
