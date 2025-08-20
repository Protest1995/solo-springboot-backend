package com.solo.portfolio.controller;

import com.solo.portfolio.model.dto.CommentRequest;
import com.solo.portfolio.model.dto.CommentResponse;
import com.solo.portfolio.service.AuthService;
import com.solo.portfolio.service.CommentService;
import com.solo.portfolio.config.OpenApiConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 評論控制器
 * 處理部落格文章評論的相關請求
 * 包含查看、新增和刪除評論的功能
 */
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "評論", description = "部落格文章評論管理")
public class CommentController {
    /**
     * 評論服務
     * 處理評論相關的業務邏輯
     */
    private final CommentService commentService;

    /**
     * 認證服務
     * 用於驗證使用者權杖
     */
    private final AuthService authService;

    /**
     * 獲取特定文章的所有評論
     * 
     * @param postId 文章ID
     * @return 評論列表
     */
    @GetMapping("/post/{postId}")
    @Operation(summary = "列出文章的所有評論")
    public ResponseEntity<List<CommentResponse>> getByPost(@PathVariable String postId) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }

    /**
     * 新增評論
     * 需要使用者已登入（提供有效的JWT權杖）
     * 
     * @param authorization JWT權杖（Bearer格式）
     * @param req 評論請求資料
     * @return 新增的評論
     */
    @PostMapping
    @Operation(summary = "新增文章評論")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    public ResponseEntity<CommentResponse> add(@RequestHeader(value = "Authorization", required = false) String authorization,
                                               @RequestBody CommentRequest req) {
        String username = null;
        // 從JWT權杖中提取使用者名稱
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            try { username = authService.getUsernameFromAccessToken(token); } catch (Exception ignored) {}
        }
        // 驗證失敗時返回401未授權狀態
        if (username == null || username.isBlank()) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(commentService.addComment(username, req));
    }

    /**
     * 刪除評論
     * 僅管理員可執行此操作
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "刪除評論（僅管理員）")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    public ResponseEntity<Void> delete(@RequestHeader(value = "Authorization", required = false) String authorization,
                                       @PathVariable String id) {
        String username = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            try { username = authService.getUsernameFromAccessToken(token); } catch (Exception ignored) {}
        }
        if (username == null || username.isBlank()) {
            return ResponseEntity.status(401).build();
        }
        // 僅允許管理員/超級用戶
        var user = authService.findUserByUsername(username);
        var role = user.getRole();
        if (role == null || !("ADMIN".equals(role.name()) || "SUPER_USER".equals(role.name()))) {
            return ResponseEntity.status(403).build();
        }
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }
}


