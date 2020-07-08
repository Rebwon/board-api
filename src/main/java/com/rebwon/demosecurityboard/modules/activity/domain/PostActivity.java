package com.rebwon.demosecurityboard.modules.activity.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter @DiscriminatorValue("POST")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostActivity extends Activity{
	private Long postId;

	public PostActivity(Long accountId, Long postId) {
		super(accountId, 10);
		this.postId = postId;
	}
}
