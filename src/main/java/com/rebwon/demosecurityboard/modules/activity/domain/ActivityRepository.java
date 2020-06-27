package com.rebwon.demosecurityboard.modules.activity.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
	void deleteActivityByAccountIdEqualsAndPostIdEquals(Long accountId, Long postId);
	List<Activity> findByAccountId(Long accountId);
}
