package com.example.smartorder.member.exception;

import org.springframework.security.core.AuthenticationException;

public class MemberException extends AuthenticationException {
	public MemberException(String error) {
		super(error);
	}
}
