package com.rebwon.demosecurityboard.modules.post.api;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import com.rebwon.demosecurityboard.modules.account.domain.Account;
import com.rebwon.demosecurityboard.modules.account.domain.AccountRepository;
import com.rebwon.demosecurityboard.modules.account.domain.UserAccount;
import com.rebwon.demosecurityboard.modules.account.mock.WithAccount;
import com.rebwon.demosecurityboard.modules.common.ControllerTests;
import com.rebwon.demosecurityboard.modules.post.api.payload.PostCreatePayload;
import com.rebwon.demosecurityboard.modules.post.domain.Post;
import com.rebwon.demosecurityboard.modules.post.domain.PostRepository;

class PostControllerTests extends ControllerTests {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PostRepository postRepository;
	private Post setupPost;

	@BeforeEach
	void setUp() {
		Account account = Account.of("chuslu@naver.com", "password!", "chulsu");
		accountRepository.save(account);
		setupPost = Post.of("test", "test contents", account, "test", Collections.emptyList());
		postRepository.save(setupPost);
	}

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
			.andExpect(jsonPath("id").exists())
			.andExpect(jsonPath("title").exists())
			.andExpect(jsonPath("content").exists())
			.andExpect(jsonPath("writer").exists())
			.andExpect(jsonPath("category").exists())
			.andExpect(header().exists(HttpHeaders.LOCATION))
			.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE.concat(UTF8)))
			.andExpect(jsonPath("_links.self").exists())
			.andExpect(jsonPath("_links.update-post").exists())
			.andDo(document("create-post",
				links(
					linkWithRel("self").description("link to self"),
					linkWithRel("update-post").description("link to update an existing post")
				),
				requestHeaders(
					headerWithName(HttpHeaders.ACCEPT).description("accept header"),
					headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type")
				),
				requestFields(
					fieldWithPath("title").type(JsonFieldType.STRING).description("<<title,title of new post>>"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("<<content,content of new post>>"),
					fieldWithPath("categoryName").type(JsonFieldType.STRING).description("<<categoryName,categoryName of new post>>"),
					fieldWithPath("tagName").type(JsonFieldType.ARRAY).description("<<tagName,tagName of new post>>")
				),
				responseHeaders(
					headerWithName(HttpHeaders.LOCATION).description("location"),
					headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
				),
				responseFields(
					fieldWithPath("id").description("identifier of new post"),
					fieldWithPath("title").description("title of new post"),
					fieldWithPath("content").description("content of new post"),
					fieldWithPath("writer.id").description("writer identifier of new post"),
					fieldWithPath("writer.nickname").description("writer nickname of new post"),
					fieldWithPath("createdDate").description("createdDate of new post"),
					fieldWithPath("modifiedDate").description("modifiedDate of new post"),
					fieldWithPath("category.id").description("category identifier of new post"),
					fieldWithPath("category.name").description("category name of new post"),
					fieldWithPath("tags.[0].id").description("tag identifier of new post"),
					fieldWithPath("tags.[0].name").description("tag name of new post"),
					fieldWithPath("tags.[1].id").description("tag identifier of new post"),
					fieldWithPath("tags.[1].name").description("tag name of new post"),
					fieldWithPath("tags.[2].id").description("tag identifier of new post"),
					fieldWithPath("tags.[2].name").description("tag name of new post"),
					fieldWithPath("countOfRecommend").description("countOfRecommend of new post"),
					fieldWithPath("commented").description("commented of new post"),
					fieldWithPath("_links.self.href").description("link to self"),
					fieldWithPath("_links.update-post.href").description("link to update-post")
				)
			));
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
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("message").exists())
			.andExpect(jsonPath("status").exists())
			.andExpect(jsonPath("errors").exists())
			.andExpect(jsonPath("code").exists())
			.andExpect(jsonPath("_links.index").exists())
			.andDo(document("errors-posts",
				links(
					linkWithRel("index").description("link to index")
				),
				relaxedResponseFields(
					fieldWithPath("message").description("error message"),
					fieldWithPath("status").description("error status"),
					fieldWithPath("errors").description("detail error information"),
					fieldWithPath("code").description("error code"),
					fieldWithPath("_links.index.href").description("link to index")
				)
			));
	}

	@Test
	@DisplayName("비인증 사용자가 게시글 1건 조회 - 성공")
	void when_getPost_Then_Success_HTTP_CODE_200() throws Exception {
		mockMvc.perform(get("/api/posts/" + setupPost.getId()))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@WithAccount("rebwon")
	@DisplayName("인증된 사용자가 게시글 1건 조회 - 성공")
	void given_AuthAccount_Posts_when_getPost_Then_Success_HTTP_CODE_200() throws Exception {
		UserAccount userAccount = getUserAccount();
		Account authAccount = userAccount.getAccount();
		Post post = postRepository.save(
			Post.of("The Auth Account", "Auth contents", authAccount, "Auth", Collections.emptyList()));

		mockMvc.perform(get("/api/posts/" + post.getId()))
			.andDo(print())
			.andExpect(status().isOk());
	}
}