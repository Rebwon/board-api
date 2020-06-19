package com.rebwon.demosecurityboard.modules.account.api.exception;

import com.rebwon.demosecurityboard.modules.common.ErrorCode;
import com.rebwon.demosecurityboard.modules.common.exception.BusinessException;

public class EmailDuplicateException extends BusinessException {
	public EmailDuplicateException(String email) {
		super(email, ErrorCode.EMAIL_DUPLICATION);
	}
}
