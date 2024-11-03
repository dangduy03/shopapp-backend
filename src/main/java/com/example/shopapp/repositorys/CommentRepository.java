package com.example.shopapp.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.example.shopapp.models.Comment;

import java.util.List;
public interface CommentRepository extends JpaRepository<Comment, Long> {
	
    List<Comment> findByUserIdAndProductId(@Param("userId") Long userId,
                                           @Param("productId") Long productId);
    
    List<Comment> findByProductId(@Param("productId") Long productId);
}
