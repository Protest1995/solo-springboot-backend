package com.solo.portfolio.repository;

import com.solo.portfolio.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 評論資料存儲庫介面
 * 提供評論相關的資料庫操作方法
 */
public interface CommentRepository extends JpaRepository<Comment, String> {
    /**
     * 根據文章ID查詢評論列表，並按建立時間升序排序
     * @param postId 文章ID
     * @return 評論列表
     */
    List<Comment> findByPostIdOrderByCreatedAtAsc(String postId);
}



