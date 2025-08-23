package com.solo.portfolio.constants;

/**
 * Redis緩存相關常量
 */
public class RedisCacheConstants {
    
    // 博客文章相關緩存
    public static final String BLOG_POST_KEY = "blog:post:";  // 單篇文章緩存key前綴
    public static final String BLOG_POSTS_FEATURED = "blog:featured";  // 精選文章列表
    public static final String BLOG_POSTS_POPULAR = "blog:popular";  // 熱門文章列表
    public static final long BLOG_POST_CACHE_TIME = 1800;  // 文章緩存時間（30分鐘）
    
    // 作品集相關緩存
    public static final String PORTFOLIO_ITEM_KEY = "portfolio:item:";  // 單個作品緩存key前綴
    public static final String PORTFOLIO_FEATURED = "portfolio:featured";  // 精選作品列表
    public static final String PORTFOLIO_CATEGORY = "portfolio:category:";  // 分類作品列表key前綴
    public static final long PORTFOLIO_CACHE_TIME = 3600;  // 作品緩存時間（1小時）
    
    // 評論相關緩存
    public static final String COMMENT_COUNT_KEY = "comment:count:";  // 評論數量key前綴
    public static final String LATEST_COMMENTS = "comments:latest";  // 最新評論列表
    public static final long COMMENT_CACHE_TIME = 300;  // 評論緩存時間（5分鐘）
    
    // 用戶相關緩存
    public static final String USER_INFO_KEY = "user:info:";  // 用戶信息key前綴
    public static final long USER_CACHE_TIME = 3600;  // 用戶信息緩存時間（1小時）
    
    // 防止實例化
    private RedisCacheConstants() {
    }
}
