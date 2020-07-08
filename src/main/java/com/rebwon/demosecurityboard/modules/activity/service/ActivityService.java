package com.rebwon.demosecurityboard.modules.activity.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rebwon.demosecurityboard.modules.activity.domain.ActivityRepository;
import com.rebwon.demosecurityboard.modules.activity.domain.PostActivity;
import com.rebwon.demosecurityboard.modules.post.domain.event.PostCreatedEvent;
import com.rebwon.demosecurityboard.modules.post.domain.event.PostDeletedEvent;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ActivityService {
	private final ActivityRepository activityRepository;

	public void writePost(PostCreatedEvent createdEvent) {
		activityRepository.save(new PostActivity(createdEvent.getWriterId(), createdEvent.getPostId()));
	}

	public void deletePost(PostDeletedEvent deletedEvent) {
		activityRepository.deleteActivity(deletedEvent.getWriterId(), deletedEvent.getPostId());
	}
}
