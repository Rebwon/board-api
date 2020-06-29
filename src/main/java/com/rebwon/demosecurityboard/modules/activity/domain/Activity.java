package com.rebwon.demosecurityboard.modules.activity.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.rebwon.demosecurityboard.modules.common.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter @DynamicUpdate @DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
public class Activity extends BaseEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private Long accountId;
	private Long postId;
	private Long commentId;
	private Integer score = 0;

	private Activity(Long accountId, Long postId, Long commentId,
		ScoreCondition condition) {
		this.accountId = accountId;
		this.postId = postId;
		this.commentId = commentId;
		this.score = condition.getScore();
	}

	public static Activity writePost(Long accountId, Long postId, ScoreCondition condition) {
		return new Activity(accountId, postId, null, condition);
	}

	public static Activity writeComment(Long accountId, Long commentId, ScoreCondition condition) {
		return new Activity(accountId, null, commentId, condition);
	}
}
