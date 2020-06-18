package com.rebwon.demosecurityboard.modules.post.api.payload;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCreatePayload {
	@NotBlank
	private String title;
	@NotBlank
	private String content;
	@NotBlank
	private String categoryName;
	private List<String> tagName;
}
