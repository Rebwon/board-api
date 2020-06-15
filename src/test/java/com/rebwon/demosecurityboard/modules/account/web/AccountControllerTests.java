package com.rebwon.demosecurityboard.modules.account.web;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.rebwon.demosecurityboard.modules.account.domain.Account;
import com.rebwon.demosecurityboard.modules.account.domain.AccountRepository;
import com.rebwon.demosecurityboard.modules.account.domain.UserAccount;
import com.rebwon.demosecurityboard.modules.account.mock.WithAccount;
import com.rebwon.demosecurityboard.modules.account.web.payload.AccountUpdatePayload;
import com.rebwon.demosecurityboard.modules.account.web.payload.SignUpPayload;
import com.rebwon.demosecurityboard.modules.common.ControllerTests;

public class AccountControllerTests extends ControllerTests {

	@Autowired
	private AccountRepository accountRepository;

	@BeforeEach
	void setUp() {
		accountRepository.save(Account.of("test@gmail.com", "testpass", "test"));
	}

	@AfterEach
	void tearDown() {
		accountRepository.deleteAll();
	}

	@Test
	@WithAccount("rebwon")
	@DisplayName("정보수정 - 성공")
	void given_UpdatePayload_When_AccountUpdate_Then_return_HTTP_CODE_204() throws Exception {
		AccountUpdatePayload payload = AccountUpdatePayload.builder()
			.nickname("rebon")
			.newPassword("123456789")
			.newPasswordConfirm("123456789")
			.build();
		UserAccount userAccount = getUserAccount();

		mockMvc.perform(put("/api/accounts/" + userAccount.getAccount().getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(payload))
		)
			.andDo(print())
			.andExpect(status().isNoContent());
	}

	@Test
	@WithAccount("rebwon")
	@DisplayName("정보수정 - 입력 형식에 맞지 않음 - 실패")
	void given_ValidUpdatePayload_When_AccountUpdate_Then_return_HTTP_CODE_400() throws Exception {
		AccountUpdatePayload payload = AccountUpdatePayload.builder()
			.nickname("rebon")
			.newPassword("123")
			.newPasswordConfirm("123")
			.build();

		mockMvc.perform(put("/api/accounts/123")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(payload))
		)
			.andDo(print())
			.andExpect(status().isBadRequest());
	}

	@Test
	@WithAccount("rebwon")
	@DisplayName("정보수정 - 이미 존재하는 닉네임과 일치하지 않는 비밀번호 입력 - 실패")
	void given_ValidateUpdatePayload_When_AccountUpdate_Then_return_HTTP_CODE_400() throws Exception {
		AccountUpdatePayload payload = AccountUpdatePayload.builder()
			.nickname("rebwon")
			.newPassword("123456789!")
			.newPasswordConfirm("1234567890!")
			.build();

		mockMvc.perform(put("/api/accounts/123")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(payload))
		)
			.andDo(print())
			.andExpect(status().isBadRequest());
	}

	@Test
	@WithAccount("rebwon")
	@DisplayName("정보수정 - 자신의 정보가 아닌 정보를 수정 - 실패")
	void given_UpdatePayload_When_AccountUpdate_Then_return_HTTP_CODE_400() throws Exception {
		AccountUpdatePayload payload = AccountUpdatePayload.builder()
			.nickname("rebon")
			.newPassword("123456789")
			.newPasswordConfirm("123456789")
			.build();

		mockMvc.perform(put("/api/accounts/123")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(payload))
		)
			.andDo(print())
			.andExpect(status().isBadRequest());
	}

	@Test
	@WithAccount("rebwon")
	@DisplayName("계정조회 - 성공")
	void given_WithAuthMockUser_When_getAccount_Then_return_Account_HTTP_CODE_200() throws Exception {
		UserAccount account = getUserAccount();
		mockMvc.perform(get("/api/accounts/"+ account.getAccount().getId()))
			.andDo(print())
			.andExpect(status().isOk());
	}

	private UserAccount getUserAccount() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		return (UserAccount) authentication.getPrincipal();
	}

	@Test
	@WithAccount("rebwon")
	@DisplayName("계정조회 - 자신의 정보가 아닌 정보 조회 - 실패")
	void given_WithAuthMockUser_When_getAccount_Is_Not_Mine_Resource_Then_HTTP_CODE_400() throws Exception {
		mockMvc.perform(get("/api/accounts/123"))
			.andDo(print())
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("회원가입 - 성공")
	void given_Payload_When_signUpProcess_Then_Success_HTTP_CODE_200() throws Exception {
		SignUpPayload payload = SignUpPayload.builder()
			.email("rebwon@gmail.com")
			.password("password!")
			.nickname("Rebwon")
			.build();

		mockMvc.perform(post("/api/accounts")
				.content(objectMapper.writeValueAsString(payload))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("id").exists())
			.andDo(document("create-account",
				requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type")
				),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING).description("<<email,email of new account>>"),
					fieldWithPath("password").type(JsonFieldType.STRING).description("<<password,password of new account>>"),
					fieldWithPath("nickname").type(JsonFieldType.STRING).description("<<nickname,nickname of new account>>")
				),
				responseFields(
					fieldWithPath("id").description("identifier of new account"),
					fieldWithPath("email").description("email of new account"),
					fieldWithPath("password").description("password of new account"),
					fieldWithPath("nickname").description("nickname of new account"),
					fieldWithPath("createdDate").description("createdDate of new account"),
					fieldWithPath("modifiedDate").description("modifiedDate of new account"),
					fieldWithPath("roles").description("roles of new account")
					)
				))
		;
	}

	@Test
	@DisplayName("회원가입 - 이미 사용중인 닉네임 입력 - 실패")
	void signUpProcess_When_DuplicateNickname_Then_Failed_HTTP_CODE_400() throws Exception {
		SignUpPayload payload = SignUpPayload.builder()
			.email("rebwon@gmail.com")
			.password("password!")
			.nickname("test")
			.build();

		mockMvc.perform(post("/api/accounts")
			.content(objectMapper.writeValueAsString(payload))
			.contentType(MediaType.APPLICATION_JSON)
		)
			.andDo(print())
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("회원가입 - 이미 사용중인 이메일 입력 - 실패")
	void signUpProcess_When_DuplicateEmail_Then_Failed_HTTP_CODE_400() throws Exception {
		SignUpPayload payload = SignUpPayload.builder()
			.email("test@gmail.com")
			.password("password!")
			.nickname("test1")
			.build();

		mockMvc.perform(post("/api/accounts")
			.content(objectMapper.writeValueAsString(payload))
			.contentType(MediaType.APPLICATION_JSON)
		)
			.andDo(print())
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("회원가입 - Empty Payload 입력 - 실패")
	void given_EmptyPayload_When_signUpProcess_Then_Failed_HTTP_CODE_400() throws Exception {
		SignUpPayload payload = new SignUpPayload();

		mockMvc.perform(post("/api/accounts")
			.content(objectMapper.writeValueAsString(payload))
			.contentType(MediaType.APPLICATION_JSON)
		)
			.andDo(print())
			.andExpect(status().isBadRequest());
	}

	@ParameterizedTest
	@DisplayName("회원가입 - 잘못된 이메일 형식 - 실패")
	@ValueSource(strings = {"sss.com", "abcdef", "abs@@"})
	void given_isNotEmailRegexPayload_When_signUpProcess_Then_Failed_HTTP_CODE_400(String email) throws Exception {
		SignUpPayload payload = SignUpPayload.builder()
			.email(email)
			.build();

		mockMvc.perform(post("/api/accounts")
			.content(objectMapper.writeValueAsString(payload))
			.contentType(MediaType.APPLICATION_JSON)
		)
			.andDo(print())
			.andExpect(status().isBadRequest());
	}

	@ParameterizedTest
	@DisplayName("회원가입 - 잘못된 비밀번호 형식 - 실패")
	@ValueSource(strings = {"", " ", "1234"})
	void given_isEmptyOrShortPassword_When_signUpProcess_Then_Failed_HTTP_CODE_400(String password) throws Exception {
		SignUpPayload payload = SignUpPayload.builder()
			.password(password)
			.build();

		mockMvc.perform(post("/api/accounts")
			.content(objectMapper.writeValueAsString(payload))
			.contentType(MediaType.APPLICATION_JSON)
		)
			.andDo(print())
			.andExpect(status().isBadRequest());
	}
}
