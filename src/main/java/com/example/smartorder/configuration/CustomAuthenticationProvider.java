package com.example.smartorder.configuration;

import static com.example.smartorder.member.type.ErrorCode.USER_NOT_EMAIL_AUTH;
import static com.example.smartorder.member.type.ErrorCode.USER_NOT_FOUND;
import static com.example.smartorder.member.type.ErrorCode.USER_STATUS_STOP;
import static com.example.smartorder.member.type.ErrorCode.USER_STATUS_WITHDRAW;
import static com.example.smartorder.member.type.UserStatus.STATUS_EMAIL_REQ;
import static com.example.smartorder.member.type.UserStatus.STATUS_STOP;
import static com.example.smartorder.member.type.UserStatus.STATUS_WITHDRAW;

import com.example.smartorder.member.exception.MemberException;
import com.example.smartorder.member.model.CustomUserDetails;
import com.example.smartorder.member.service.MemberService;
import com.example.smartorder.member.type.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private final MemberService memberService;

	private final PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication)
		throws AuthenticationException {

//		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
//
//		String username = token.getName();
//		String password = (String)token.getCredentials();

		String username = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();


		CustomUserDetails user =
			(CustomUserDetails)memberService.loadUserByUsername(username);

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new BadCredentialsException(USER_NOT_FOUND.getDescription());
		}

		UserStatus userStatus = user.getMember().getUserStatus();
		if (STATUS_EMAIL_REQ.equals(userStatus)) {
			throw new MemberException(USER_NOT_EMAIL_AUTH.getDescription());
		}

		if (STATUS_STOP.equals(userStatus)) {
			throw new MemberException(USER_STATUS_STOP.getDescription());
		}

		if (STATUS_WITHDRAW.equals(userStatus)) {
			throw new MemberException(USER_STATUS_WITHDRAW.getDescription());
		}

		return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}
}
