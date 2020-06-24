package com.rebwon.demosecurityboard.modules.common.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.rebwon.demosecurityboard.modules.account.domain.ActivityScore;

@Converter(autoApply = true)
public class ActivityScoreConverter implements AttributeConverter<ActivityScore, Integer> {
	@Override
	public Integer convertToDatabaseColumn(ActivityScore score) {
		return score.getScore();
	}

	@Override
	public ActivityScore convertToEntityAttribute(Integer score) {
		return ActivityScore.valueOf(score);
	}
}
