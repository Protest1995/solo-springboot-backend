package com.solo.portfolio.service;

import com.solo.portfolio.model.entity.BlogPost;
import com.solo.portfolio.repository.BlogPostRepository;
import com.solo.portfolio.service.cache.BlogPostCacheService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * 博客文章服務類
 */
@Service
public class BlogPostService {
    
    private final BlogPostRepository blogPostRepository;
    private final BlogPostCacheService blogPostCacheService;
    
    public BlogPostService(BlogPostRepository blogPostRepository, 
                         BlogPostCacheService blogPostCacheService) {
        this.blogPostRepository = blogPostRepository;
        this.blogPostCacheService = blogPostCacheService;
    }
    
    /**
     * 獲取單篇博客文章
     * 優先從緩存中獲取，如果緩存中不存在則從數據庫獲取並緩存
     */
    public Optional<BlogPost> getBlogPost(String id) {
        // 嘗試從緩存獲取
        Optional<BlogPost> cachedPost = blogPostCacheService.getCachedBlogPost(id);
        if (cachedPost.isPresent()) {
            return cachedPost;
        }
        
        // 從數據庫獲取
        Optional<BlogPost> blogPost = blogPostRepository.findById(id);
        blogPost.ifPresent(post -> {
            blogPostCacheService.cacheBlogPost(post);
            blogPostCacheService.incrementViewCount(id);
        });
        
        return blogPost;
    }
    
    /**
     * 獲取精選博客文章列表
     * 優先從緩存中獲取，如果緩存中不存在則從數據庫獲取並緩存
     */
    public List<BlogPost> getFeaturedPosts() {
        // 嘗試從緩存獲取
        Optional<List<BlogPost>> cachedPosts = blogPostCacheService.getCachedFeaturedPosts();
        if (cachedPosts.isPresent()) {
            return cachedPosts.get();
        }
        
        // 從數據庫獲取
        List<BlogPost> featuredPosts = blogPostRepository.findByIsLockedFalse();
        blogPostCacheService.cacheFeaturedPosts(featuredPosts);
        
        return featuredPosts;
    }
    
    /**
     * 保存或更新博客文章
     */
    @Transactional
    public BlogPost saveBlogPost(BlogPost blogPost) {
        BlogPost savedPost = blogPostRepository.save(blogPost);
        // 更新緩存
        blogPostCacheService.cacheBlogPost(savedPost);
        return savedPost;
    }
    
    /**
     * 刪除博客文章
     */
    @Transactional
    public void deleteBlogPost(String id) {
        blogPostRepository.deleteById(id);
        // 刪除緩存
        blogPostCacheService.deleteBlogPostCache(id);
    }
    
    /**
     * 更新文章瀏覽次數
     */
    @Transactional
    public void incrementViewCount(String id) {
        blogPostRepository.findById(id).ifPresent(post -> {
            post.setViews(post.getViews() + 1);
            blogPostRepository.save(post);
            blogPostCacheService.incrementViewCount(id);
        });
    }
}
