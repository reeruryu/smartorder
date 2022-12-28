package com.example.smartorder.member.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	USER_NOT_FOUND("회원 정보가 존재하지 않습니다."),
	USER_NOT_EMAIL_AUTH("이메일 인증이 완료되지 않았습니다."),
	USER_STATUS_STOP("정지된 회원입니다."),
	USER_STATUS_WITHDRAW("탈되하나 회원입니다.")
	;

	private final String description;
}
