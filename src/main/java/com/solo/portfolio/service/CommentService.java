package com.solo.portfolio.service;

import com.solo.portfolio.model.dto.CommentRequest;
import com.solo.portfolio.model.dto.CommentResponse;
import com.solo.portfolio.model.entity.Comment;
import com.solo.portfolio.model.entity.User;
import com.solo.portfolio.repository.CommentRepository;
import com.solo.portfolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 評論服務類
 * 處理部落格文章評論的相關業務邏輯
 */
@Service
@RequiredArgsConstructor
public class CommentService {
    /**
     * 評論資料存儲庫
     */
    private final CommentRepository commentRepository;
    
    /**
     * 使用者資料存儲庫
     */
    private final UserRepository userRepository;

    /**
     * 獲取指定文章的所有評論
     * @param postId 文章ID
     * @return 評論列表
     */
    public List<CommentResponse> getCommentsByPost(String postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
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
        return toResponse(saved);
    }

    @Transactional
    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }

    private CommentResponse toResponse(Comment c) {
        String iso = c.getCreatedAt() == null ? null : c.getCreatedAt().atOffset(ZoneOffset.UTC).toString();
        return new CommentResponse(
                c.getId(), c.getPostId(), c.getUserId(), c.getUsername(), c.getAvatarUrl(), iso, c.getText(), c.getParentId()
        );
    }
}



