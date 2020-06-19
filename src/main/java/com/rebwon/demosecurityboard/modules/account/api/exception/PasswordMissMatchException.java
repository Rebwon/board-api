package com.rebwon.demosecurityboard.modules.account.api.exception;

import com.rebwon.demosecurityboard.modules.common.error.ErrorCode;
import com.rebwon.demosecurityboard.modules.common.exception.BusinessException;

public class PasswordMissMatchException extends BusinessException {
	public PasswordMissMatchException() {
		super("", ErrorCode.PASSWORD_MISS_MATCHED);
	}
}
