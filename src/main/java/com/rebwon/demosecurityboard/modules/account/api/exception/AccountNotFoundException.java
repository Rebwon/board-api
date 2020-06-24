package com.rebwon.demosecurityboard.modules.account.api.exception;

import com.rebwon.demosecurityboard.modules.common.error.ErrorCode;
import com.rebwon.demosecurityboard.modules.common.exception.BusinessException;

public class AccountNotFoundException extends BusinessException {
	public AccountNotFoundException() {
		super(ErrorCode.NOT_FOUND);
	}
}
