package com.solo.portfolio.service;

import com.solo.portfolio.model.dto.CommentRequest;
import com.solo.portfolio.model.dto.CommentResponse;
import com.solo.portfolio.model.entity.Comment;
import com.solo.portfolio.model.entity.User;
import com.solo.portfolio.repository.CommentRepository;
import com.solo.portfolio.repository.UserRepository;
import com.solo.portfolio.service.cache.CommentCacheService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 評論服務類
 * 處理部落格文章評論的相關業務邏輯
 */
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentCacheService commentCacheService;
    private final UserRepository userRepository;
    
    public CommentService(CommentRepository commentRepository,
                         CommentCacheService commentCacheService,
                         UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.commentCacheService = commentCacheService;
        this.userRepository = userRepository;
    }
    
    // 移除重複的 userRepository 聲明

    /**
     * 獲取指定文章的所有評論
     * @param postId 文章ID
     * @return 評論列表
     */
    public List<CommentResponse> getCommentsByPost(String postId) {
        // 嘗試從緩存獲取
        Optional<List<Comment>> cachedComments = commentCacheService.getCachedPostComments(postId);
        List<Comment> comments;
        
        if (cachedComments.isPresent()) {
            comments = cachedComments.get();
        } else {
            // 從數據庫獲取並緩存
            comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
            commentCacheService.cachePostComments(postId, comments);
        }
        
        return comments.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * 新增評論
     * @param username 使用者名稱
     * @param req 評論請求資料
     * @return 新增的評論回應
     */
    @Transactional
    public CommentResponse addComment(String username, CommentRequest req) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("使用者不存在"));

        Comment c = new Comment();
        c.setId(UUID.randomUUID().toString());
        c.setPostId(req.getPostId());
        c.setUserId(user.getId());
        c.setUsername(user.getUsername());
        c.setAvatarUrl(user.getAvatarUrl() != null ? user.getAvatarUrl() : "/images/profile.jpg");
        c.setText(req.getText());
        c.setParentId(req.getParentId());

        Comment saved = commentRepository.save(c);
        
        // 更新緩存
        commentCacheService.cacheComment(saved);
        // 使緩存的評論列表失效，因為已經添加了新評論
        commentCacheService.deleteCommentCache(req.getPostId());
        // 增加評論計數
        commentCacheService.incrementCommentCount(req.getPostId());
        
        return toResponse(saved);
    }

    @Transactional
    public void deleteComment(String id) {
        // 在刪除之前獲取評論信息，以便更新相關緩存
        commentRepository.findById(id).ifPresent(comment -> {
            commentRepository.deleteById(id);
            // 刪除評論緩存
            commentCacheService.deleteCommentCache(id);
            // 使該文章的評論列表緩存失效
            commentCacheService.deleteCommentCache(comment.getPostId());
        });
    }

    private CommentResponse toResponse(Comment c) {
        String iso = c.getCreatedAt() == null ? null : c.getCreatedAt().atOffset(ZoneOffset.UTC).toString();
        return new CommentResponse(
                c.getId(), c.getPostId(), c.getUserId(), c.getUsername(), c.getAvatarUrl(), iso, c.getText(), c.getParentId()
        );
    }
}



