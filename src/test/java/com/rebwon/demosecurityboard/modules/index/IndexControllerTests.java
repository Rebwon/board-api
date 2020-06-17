package com.rebwon.demosecurityboard.modules.index;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;

import com.rebwon.demosecurityboard.modules.common.ControllerTests;

class IndexControllerTests extends ControllerTests {

	@Test
	void indexPage() throws Exception {
		mockMvc.perform(get("/api"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("_links.accounts").exists())
				.andDo(document("index",
					links(
						linkWithRel("accounts").description("link to accounts")
					),
					responseFields(fieldWithPath("_links.accounts.href").description("link to accounts"))
					))
		;
	}
}