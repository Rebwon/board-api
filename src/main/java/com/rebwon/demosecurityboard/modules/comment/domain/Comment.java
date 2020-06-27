package com.rebwon.demosecurityboard.modules.comment.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.rebwon.demosecurityboard.modules.common.domain.BaseEntity;
import com.rebwon.demosecurityboard.modules.post.domain.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Comment extends BaseEntity {
	@Id @GeneratedValue
	private Long id;
	@Column(nullable = false)
	private Long accountId;
	@ManyToOne
	private Post post;
	private String content;
	private int countOfRecommend = 0;

	public static Comment of(Long accountId, String content) {
		Comment comment  = new Comment();
		comment.accountId = accountId;
		comment.content = content;
		return comment;
	}
}
