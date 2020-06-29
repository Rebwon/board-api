package com.rebwon.demosecurityboard.modules.post.domain;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable @Getter
@NoArgsConstructor
public class Tag {
	private String name;

	public Tag(String name) {
		this.name = name;
	}
}
