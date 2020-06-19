package com.rebwon.demosecurityboard.modules.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
	INVALID_INPUT(400, "C001", "Invalid Input"),
	UN_AUTHORIZED(401, "C002", "UnAuthorized"),
	FORBIDDEN(403, "C003", "Forbidden Request"),
	NOT_FOUND(404, "C004", "Resource Not Found"),
	METHOD_NOW_ALLOWED(405, "C005", "Method Now Allowed"),
	EMAIL_DUPLICATION(400, "A001", "Duplicated Email"),
	NICKNAME_DUPLICATION(400, "A002", "Duplicated Nickname"),
	IS_NOT_OWNER(401, "A003", "You are not the owner of the requested resource"),
	PASSWORD_MISS_MATCHED(400, "A004", "Password miss matched")
	;

	private String code;
	private String message;
	private int status;

	ErrorCode(int status, String code, String message) {
		this.status = status;
		this.message = message;
		this.code = code;
	}
}
