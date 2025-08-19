package com.solo.portfolio.controller;

import com.solo.portfolio.model.dto.BlogPostRequest;
import com.solo.portfolio.model.dto.PortfolioItemRequest;
import com.solo.portfolio.model.entity.BlogPost;
import com.solo.portfolio.model.entity.PortfolioItem;
import com.solo.portfolio.service.ContentService;
import com.solo.portfolio.config.OpenApiConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 內容控制器
 * 處理作品集和部落格文章的CRUD操作
 */
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "內容", description = "作品集和部落格內容管理")
public class ContentController {
    /**
     * 內容服務
     * 處理所有與內容相關的業務邏輯
     */
    private final ContentService contentService;

    /**
     * 作品集相關端點
     */
    
    /**
     * 獲取所有作品集項目
     * @return 作品集項目列表
     */
    @GetMapping("/portfolio")
    @Operation(summary = "列出所有作品集項目")
    public ResponseEntity<List<PortfolioItem>> getPortfolio() {
        return ResponseEntity.ok(contentService.getAllPortfolioItems());
    }

    /**
     * 根據ID獲取特定作品集項目
     * @param id 作品集項目ID
     * @return 作品集項目
     */
    @GetMapping("/portfolio/{id}")
    @Operation(summary = "根據ID獲取作品集項目")
    public ResponseEntity<PortfolioItem> getPortfolioItem(@PathVariable String id) {
        return ResponseEntity.ok(contentService.getPortfolioItemById(id));
    }

    /**
     * 創建新的作品集項目
     * @param request 作品集項目請求資料
     * @return 新建的作品集項目
     */
    @PostMapping("/portfolio")
    @Operation(summary = "創建作品集項目")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    public ResponseEntity<PortfolioItem> createPortfolioItem(@RequestBody PortfolioItemRequest request) {
        return ResponseEntity.ok(contentService.createPortfolioItem(request));
    }

    /**
     * 更新現有的作品集項目
     * @param id 作品集項目ID
     * @param request 更新的作品集項目資料
     * @return 更新後的作品集項目
     */
    @PutMapping("/portfolio/{id}")
    @Operation(summary = "更新作品集項目")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    public ResponseEntity<PortfolioItem> updatePortfolioItem(@PathVariable String id, @RequestBody PortfolioItemRequest request) {
        return ResponseEntity.ok(contentService.updatePortfolioItem(id, request));
    }

    /**
     * 刪除指定的作品集項目
     * @param id 要刪除的作品集項目ID
     * @return 空回應
     */
    @DeleteMapping("/portfolio/{id}")
    @Operation(summary = "刪除作品集項目")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    public ResponseEntity<Void> deletePortfolioItem(@PathVariable String id) {
        contentService.deletePortfolioItem(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 部落格文章相關端點
     */

    /**
     * 獲取所有部落格文章
     * @return 部落格文章列表
     */
    @GetMapping("/posts")
    @Operation(summary = "列出所有部落格文章")
    public ResponseEntity<List<BlogPost>> getPosts() {
        return ResponseEntity.ok(contentService.getAllPosts());
    }

    /**
     * 根據ID獲取特定部落格文章
     * @param id 部落格文章ID
     * @return 部落格文章
     */
    @GetMapping("/posts/{id}")
    @Operation(summary = "根據ID獲取部落格文章")
    public ResponseEntity<BlogPost> getPost(@PathVariable String id) {
        return ResponseEntity.ok(contentService.getPostById(id));
    }

    /**
     * 創建新的部落格文章
     * @param request 部落格文章請求資料
     * @return 新建的部落格文章
     */
    @PostMapping("/posts")
    @Operation(summary = "創建部落格文章")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    public ResponseEntity<BlogPost> createPost(@RequestBody BlogPostRequest request) {
        return ResponseEntity.ok(contentService.createPost(request));
    }

    /**
     * 更新現有的部落格文章
     * @param id 部落格文章ID
     * @param request 更新的部落格文章資料
     * @return 更新後的部落格文章
     */
    @PutMapping("/posts/{id}")
    @Operation(summary = "更新部落格文章")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    public ResponseEntity<BlogPost> updatePost(@PathVariable String id, @RequestBody BlogPostRequest request) {
        return ResponseEntity.ok(contentService.updatePost(id, request));
    }

    /**
     * 刪除指定的部落格文章
     * @param id 要刪除的部落格文章ID
     * @return 空回應
     */
    @DeleteMapping("/posts/{id}")
    @Operation(summary = "刪除部落格文章")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        contentService.deletePost(id);
        return ResponseEntity.ok().build();
    }
}


