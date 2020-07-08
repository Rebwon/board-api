package com.rebwon.demosecurityboard.modules.activity.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rebwon.demosecurityboard.modules.activity.domain.Activity;
import com.rebwon.demosecurityboard.modules.activity.domain.ActivityRepository;
import com.rebwon.demosecurityboard.modules.activity.domain.PostScoreCondition;
import com.rebwon.demosecurityboard.modules.post.domain.event.PostCreatedEvent;
import com.rebwon.demosecurityboard.modules.post.domain.event.PostDeletedEvent;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ActivityService {
	private final ActivityRepository activityRepository;

	public void writePost(PostCreatedEvent createdEvent) {
		activityRepository.save(
			Activity.writePost(createdEvent.getWriterId(), createdEvent.getPostId(), new PostScoreCondition())
		);
	}

	public void deletePost(PostDeletedEvent deletedEvent) {
		activityRepository.deleteActivityByAccountIdEqualsAndPostIdEquals(deletedEvent.getWriterId(), deletedEvent.getPostId());
	}
}
