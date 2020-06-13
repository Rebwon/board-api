package com.rebwon.demosecurityboard.modules.account.web;

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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebwon.demosecurityboard.modules.account.domain.Account;
import com.rebwon.demosecurityboard.modules.account.domain.AccountRepository;
import com.rebwon.demosecurityboard.modules.account.mock.WithAccount;
import com.rebwon.demosecurityboard.modules.account.web.payload.SignUpPayload;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

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
	@DisplayName("인증된 사용자가 자신의 정보를 조회")
	void given_WithAuthMockUser_When_getAccount_Then_return_Account_HTTP_CODE_200() throws Exception {
		mockMvc.perform(get("/api/accounts/1"))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@WithAccount("rebwon")
	@DisplayName("인증된 사용자가 자신의 리소스가 아닌 리소스를 접근할 경우 404 에러")
	void given_WithAuthMockUser_When_getAccount_Not_Mine_Then_HTTP_CODE_404() throws Exception {
		mockMvc.perform(get("/api/accounts/123"))
			.andDo(print())
			.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("사용자가 입력한 값을 검증하고 회원가입 처리")
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
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("이미 사용 중인 닉네임으로 회원가입할 경우 실패")
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
	@DisplayName("이미 사용 중인 이메일로 회원가입할 경우 실패")
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
	@DisplayName("사용자가 입력한 값이 empty인 경우 회원가입 실패")
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
	@DisplayName("사용자가 입력한 값이 이메일 형식이 아닐 경우 회원가입 실패")
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
	@DisplayName("사용자가 입력한 값이 empty이거나 길이가 짧은 비밀번호일 경우 회원가입 실패")
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
