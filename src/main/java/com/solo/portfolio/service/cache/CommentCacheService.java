package com.solo.portfolio.service.cache;

import com.solo.portfolio.constants.RedisCacheConstants;
import com.solo.portfolio.model.entity.Comment;
import com.solo.portfolio.service.RedisService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * 評論緩存服務
 */
@Service
public class CommentCacheService {
    
    private final RedisService redisService;
    
    public CommentCacheService(RedisService redisService) {
        this.redisService = redisService;
    }
    
    /**
     * 獲取緩存的評論
     * @param id 評論ID
     * @return Optional包裝的評論
     */
    public Optional<Comment> getCachedComment(String id) {
        try {
            Object cached = redisService.get(RedisCacheConstants.COMMENT_COUNT_KEY + id);
            if (cached instanceof Comment) {
                return Optional.of((Comment) cached);
            }
        } catch (Exception e) {
            // 緩存操作失敗時，記錄日誌但不影響主流程
            return Optional.empty();
        }
        return Optional.empty();
    }
    
    /**
     * 緩存評論
     * @param comment 要緩存的評論
     */
    public void cacheComment(Comment comment) {
        try {
            redisService.set(
                RedisCacheConstants.COMMENT_COUNT_KEY + comment.getId(),
                comment,
                RedisCacheConstants.COMMENT_CACHE_TIME
            );
        } catch (Exception e) {
            // 緩存操作失敗時，記錄日誌但不影響主流程
        }
    }
    
    /**
     * 刪除評論緩存
     * @param id 評論ID
     */
    public void deleteCommentCache(String id) {
        try {
            redisService.delete(RedisCacheConstants.COMMENT_COUNT_KEY + id);
        } catch (Exception e) {
            // 緩存操作失敗時，記錄日誌但不影響主流程
        }
    }
    
    /**
     * 緩存文章的評論列表
     * @param postId 文章ID
     * @param comments 評論列表
     */
    public void cachePostComments(String postId, List<Comment> comments) {
        try {
            redisService.set(
                RedisCacheConstants.COMMENT_COUNT_KEY + postId + ":list",
                comments,
                RedisCacheConstants.COMMENT_CACHE_TIME
            );
        } catch (Exception e) {
            // 緩存操作失敗時，記錄日誌但不影響主流程
        }
    }
    
    /**
     * 獲取緩存的文章評論列表
     * @param postId 文章ID
     * @return Optional包裝的評論列表
     */
    @SuppressWarnings("unchecked")
    public Optional<List<Comment>> getCachedPostComments(String postId) {
        try {
            Object cached = redisService.get(RedisCacheConstants.COMMENT_COUNT_KEY + postId + ":list");
            if (cached instanceof List<?>) {
                return Optional.of((List<Comment>) cached);
            }
        } catch (Exception e) {
            // 緩存操作失敗時，記錄日誌但不影響主流程
            return Optional.empty();
        }
        return Optional.empty();
    }
    
    /**
     * 更新文章評論數量
     * @param postId 文章ID
     */
    public void incrementCommentCount(String postId) {
        try {
            String countKey = RedisCacheConstants.COMMENT_COUNT_KEY + postId + ":count";
            redisService.increment(countKey);
            redisService.expire(countKey, RedisCacheConstants.COMMENT_CACHE_TIME);
        } catch (Exception e) {
            // 緩存操作失敗時，記錄日誌但不影響主流程
        }
    }
    
    /**
     * 緩存最新評論列表
     * @param comments 最新評論列表
     */
    public void cacheLatestComments(List<Comment> comments) {
        try {
            redisService.set(
                RedisCacheConstants.LATEST_COMMENTS,
                comments,
                RedisCacheConstants.COMMENT_CACHE_TIME
            );
        } catch (Exception e) {
            // 緩存操作失敗時，記錄日誌但不影響主流程
        }
    }
    
    /**
     * 獲取緩存的最新評論列表
     * @return Optional包裝的評論列表
     */
    @SuppressWarnings("unchecked")
    public Optional<List<Comment>> getCachedLatestComments() {
        try {
            Object cached = redisService.get(RedisCacheConstants.LATEST_COMMENTS);
            if (cached instanceof List<?>) {
                return Optional.of((List<Comment>) cached);
            }
        } catch (Exception e) {
            // 緩存操作失敗時，記錄日誌但不影響主流程
            return Optional.empty();
        }
        return Optional.empty();
    }
}
