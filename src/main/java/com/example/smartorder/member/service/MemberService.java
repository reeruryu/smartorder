package com.example.smartorder.member.service;

import com.example.smartorder.member.model.MemberInput;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService extends UserDetailsService {

	/**
	 * 회원 가입
	 */
	boolean register(MemberInput parmeter);

	/**
	 * 이메일 인증
	 */
	boolean emailAuth(String uuid);

}
