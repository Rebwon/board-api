package com.rebwon.demosecurityboard.modules.index;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.rebwon.demosecurityboard.modules.common.ControllerTests;

class IndexControllerTests extends ControllerTests {

	@Test
	@DisplayName("인덱스 페이지 조회")
	void indexPage() throws Exception {
		mockMvc.perform(get("/api"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("_links.accounts").exists())
				.andExpect(jsonPath("_links.posts").exists())
				.andDo(document("index",
					links(
						linkWithRel("accounts").description("link to accounts"),
						linkWithRel("posts").description("link to posts")
					),
					responseFields(
						fieldWithPath("_links.accounts.href").description("link to accounts"),
						fieldWithPath("_links.posts.href").description("link to posts")
					)
				));
	}
}