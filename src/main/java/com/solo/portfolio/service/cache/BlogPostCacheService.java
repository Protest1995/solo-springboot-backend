package com.solo.portfolio.service.cache;

import com.solo.portfolio.constants.RedisCacheConstants;
import com.solo.portfolio.model.entity.BlogPost;
import com.solo.portfolio.service.RedisService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * 博客文章緩存服務
 */
@Service
public class BlogPostCacheService {
    
    private final RedisService redisService;
    
    public BlogPostCacheService(RedisService redisService) {
        this.redisService = redisService;
    }
    
    /**
     * 獲取緩存的博客文章
     * @param id 文章ID
     * @return Optional包裝的博客文章
     */
    public Optional<BlogPost> getCachedBlogPost(String id) {
        try {
            Object cached = redisService.get(RedisCacheConstants.BLOG_POST_KEY + id);
            if (cached instanceof BlogPost) {
                return Optional.of((BlogPost) cached);
            }
        } catch (Exception e) {
            // 緩存操作失敗時，記錄日誌但不影響主流程
            return Optional.empty();
        }
        return Optional.empty();
    }
    
    /**
     * 緩存博客文章
     * @param blogPost 要緩存的博客文章
     */
    public void cacheBlogPost(BlogPost blogPost) {
        try {
            redisService.set(
                RedisCacheConstants.BLOG_POST_KEY + blogPost.getId(),
                blogPost,
                RedisCacheConstants.BLOG_POST_CACHE_TIME
            );
        } catch (Exception e) {
            // 緩存操作失敗時，記錄日誌但不影響主流程
        }
    }
    
    /**
     * 刪除博客文章緩存
     * @param id 文章ID
     */
    public void deleteBlogPostCache(String id) {
        try {
            redisService.delete(RedisCacheConstants.BLOG_POST_KEY + id);
        } catch (Exception e) {
            // 緩存操作失敗時，記錄日誌但不影響主流程
        }
    }
    
    /**
     * 緩存精選博客文章列表
     * @param blogPosts 精選博客文章列表
     */
    public void cacheFeaturedPosts(List<BlogPost> blogPosts) {
        try {
            redisService.set(
                RedisCacheConstants.BLOG_POSTS_FEATURED,
                blogPosts,
                RedisCacheConstants.BLOG_POST_CACHE_TIME
            );
        } catch (Exception e) {
            // 緩存操作失敗時，記錄日誌但不影響主流程
        }
    }
    
    /**
     * 獲取緩存的精選博客文章列表
     * @return Optional包裝的博客文章列表
     */
    @SuppressWarnings("unchecked")
    public Optional<List<BlogPost>> getCachedFeaturedPosts() {
        try {
            Object cached = redisService.get(RedisCacheConstants.BLOG_POSTS_FEATURED);
            if (cached instanceof List<?>) {
                return Optional.of((List<BlogPost>) cached);
            }
        } catch (Exception e) {
            // 緩存操作失敗時，記錄日誌但不影響主流程
            return Optional.empty();
        }
        return Optional.empty();
    }
    
    /**
     * 更新文章瀏覽次數
     * @param id 文章ID
     */
    public void incrementViewCount(String id) {
        try {
            String viewCountKey = RedisCacheConstants.BLOG_POST_KEY + id + ":views";
            redisService.increment(viewCountKey);
            redisService.expire(viewCountKey, RedisCacheConstants.BLOG_POST_CACHE_TIME);
        } catch (Exception e) {
            // 緩存操作失敗時，記錄日誌但不影響主流程
        }
    }
}
