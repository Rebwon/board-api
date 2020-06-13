package com.rebwon.demosecurityboard.modules.account.web.payload;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class AccountUpdatePayload {
	@NotBlank
	private String nickname;
	@NotBlank
	@Length(min = 8, max = 50)
	private String password;
}
