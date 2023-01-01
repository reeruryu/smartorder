package com.example.smartorder.member.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {
	ROLE_ADMIN("관리자"),
	ROLE_USER("고객"),
	ROLE_CEO("점주")
	;

	private final String description;
}
