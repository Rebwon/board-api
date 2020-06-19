package com.rebwon.demosecurityboard.modules.post.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;

import com.rebwon.demosecurityboard.modules.account.domain.AccountRepository;
import com.rebwon.demosecurityboard.modules.account.mock.WithAccount;
import com.rebwon.demosecurityboard.modules.common.ControllerTests;
import com.rebwon.demosecurityboard.modules.post.api.payload.PostCreatePayload;
import com.rebwon.demosecurityboard.modules.post.domain.PostRepository;

class PostControllerTest extends ControllerTests {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PostRepository postRepository;

	@AfterEach
	void tearDown() {
		postRepository.deleteAll();
		accountRepository.deleteAll();
	}

	@Test
	@WithAccount("rebwon")
	@DisplayName("게시글 작성 - 성공")
	void given_Payload_When_CreatePost_Then_Success_HTTP_CODE_201() throws Exception {
		PostCreatePayload payload = PostCreatePayload.builder()
			.title("Test Title")
			.content("Test Contents")
			.categoryName("SpringBoot")
			.tagName(Arrays.asList("Hibernate", "Spring", "ATDD"))
			.build();

		mockMvc.perform(post("/api/posts")
				.content(objectMapper.writeValueAsString(payload))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaTypes.HAL_JSON)
			)
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("id").exists());
	}

	@Test
	@WithAccount("rebwon")
	@DisplayName("게시글 작성 - Empty Payload 입력 - 실패")
	void given_emptyPayload_When_CreatePost_Then_Fail_HTTP_CODE_400() throws Exception {
		PostCreatePayload payload = new PostCreatePayload();

		mockMvc.perform(post("/api/posts")
			.content(objectMapper.writeValueAsString(payload))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaTypes.HAL_JSON)
		)
			.andDo(print())
			.andExpect(status().isBadRequest());
	}

}