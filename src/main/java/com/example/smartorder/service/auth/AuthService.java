package com.example.smartorder.service.auth;

import com.example.smartorder.dto.AuthDto;
import com.example.smartorder.model.Auth;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {

	/**
	 * 회원 가입
	 */
	void register(Auth.Register parmeter);

	/**
	 * 로그인
	 */
	AuthDto login(Auth.Login parameter);

	/**
	 * 이메일 인증
	 */
	void emailAuth(String uuid);

}
