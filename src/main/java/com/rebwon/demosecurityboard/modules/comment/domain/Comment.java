package com.rebwon.demosecurityboard.modules.comment.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.rebwon.demosecurityboard.modules.account.domain.Account;
import com.rebwon.demosecurityboard.modules.common.domain.BaseEntity;
import com.rebwon.demosecurityboard.modules.post.domain.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Comment extends BaseEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	private Account replyee;
	@ManyToOne(fetch = FetchType.LAZY)
	private Post post;
	private String content;
	private Integer likeCount = 0;

	public static Comment of(Account replyee, Post post, String content) {
		Comment comment  = new Comment();
		comment.replyee = replyee;
		comment.post = post;
		comment.content = content;
		return comment;
	}
}
