package com.rebwon.demosecurityboard.modules.activity.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter @DiscriminatorValue("COMMENT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentActivity extends Activity{
	private Long commentId;

	public CommentActivity(Long accountId, Long commentId) {
		super(accountId, 5);
		this.commentId = commentId;
	}
}
