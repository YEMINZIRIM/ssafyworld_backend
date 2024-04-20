package com.yeminjilim.ssafyworld.member.error;

import com.yeminjilim.ssafyworld.global.CustomException;

public class CustomMemberException extends CustomException {

	public CustomMemberException(MemberErrorCode errorCode) {
		super(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage());
	}
}