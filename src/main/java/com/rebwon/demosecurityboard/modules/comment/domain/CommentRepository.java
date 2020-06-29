package com.rebwon.demosecurityboard.modules.comment.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByPost_Id(Long postId);
}
