package com.rebwon.demosecurityboard.modules.post.domain.event;

import com.rebwon.demosecurityboard.modules.post.domain.Post;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostCreatedEvent {
	private final Post post;

	public Long getWriterId() {
		return post.getWriter().getId();
	}
}
