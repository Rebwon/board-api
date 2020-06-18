package com.rebwon.demosecurityboard.modules.account.api.payload;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdatePayload {
	@NotBlank
	private String nickname;
	@Length(min = 8, max = 50)
	@NotBlank
	private String newPassword;
	@Length(min = 8, max = 50)
	@NotBlank
	private String newPasswordConfirm;
}
