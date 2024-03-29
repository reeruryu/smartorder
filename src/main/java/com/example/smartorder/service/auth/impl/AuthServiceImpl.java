package com.example.smartorder.service.auth.impl;

import static com.example.smartorder.common.error.ErrorCode.ALREADY_AUTH_EMAIL;
import static com.example.smartorder.common.error.ErrorCode.ALREADY_USERID_EXISTS;
import static com.example.smartorder.common.error.ErrorCode.FAIL_SEND_MAIL;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;
import static com.example.smartorder.common.error.ErrorCode.USER_NOT_EMAIL_AUTH;
import static com.example.smartorder.common.error.ErrorCode.USER_STATUS_STOP;
import static com.example.smartorder.common.error.ErrorCode.USER_STATUS_WITHDRAW;
import static com.example.smartorder.type.UserRole.ROLE_USER;
import static com.example.smartorder.type.UserStatus.STATUS_EMAIL_REQ;
import static com.example.smartorder.type.UserStatus.STATUS_ING;
import static com.example.smartorder.type.UserStatus.STATUS_STOP;
import static com.example.smartorder.type.UserStatus.STATUS_WITHDRAW;

import com.example.smartorder.common.component.MailComponents;
import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.dto.AuthDto;
import com.example.smartorder.entity.Member;
import com.example.smartorder.model.Auth;
import com.example.smartorder.model.CustomUserDetails;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.service.auth.AuthService;
import com.example.smartorder.common.util.PasswordUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

	private final MemberRepository memberRepository;
	private final MailComponents mailComponents;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = memberRepository.findByUserId(username)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		List<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(member);

		return new CustomUserDetails(member, grantedAuthorities);
	}


	@Override
	public void register(Auth.Register parameter) {

		boolean exists = memberRepository.existsByUserId(parameter.getUserId());
		if (exists) {
			throw new CustomException(ALREADY_USERID_EXISTS);
		}

		String uuid = UUID.randomUUID().toString();
		if (!sendAuthMail(parameter.getUserId(), uuid)) {
			throw new CustomException(FAIL_SEND_MAIL);
		}

		String encPassword = PasswordUtils.encPassword(parameter.getPw());
		memberRepository.save(parameter.toEntity(encPassword, uuid));
	}

	@Override
	public AuthDto login(Auth.Login parameter) {
		Member member = memberRepository.findByUserId(parameter.getUserId())
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		if (!PasswordUtils.equals(parameter.getPw(), member.getPw())) {
			throw new CustomException(NOT_FOUND_USER);
		}

		checkUserStatus(member);

		List<String> roles = new ArrayList<>();
		roles.add(member.getUserRole().name());

		return AuthDto.builder()
			.userId(member.getUserId())
			.userRole(roles)
			.build();
	}

	@Override
	public void emailAuth(String uuid) {
		Member member = memberRepository.findByEmailAuthKey(uuid)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		if (member.getUserStatus() != STATUS_EMAIL_REQ) {
			throw new CustomException(ALREADY_AUTH_EMAIL);
		}

		member.setEmailAuthDt(LocalDateTime.now());
		member.setUserStatus(STATUS_ING);
		memberRepository.save(member);
	}

	private boolean sendAuthMail(String userId, String uuid) {
		String subject = "smartorder 사이트 가입을 축하드립니다.";
		String text = "<p>smartorder 사이트 가입을 축하드립니다.</p>" + "<p>아래 링크를 클릭하시면 회원 활성화가 됩니다.</p>"
			+ "<div><a href='http://localhost:8080/auth/email?uuid=" + uuid
			+ "'> 링크 클릭 </a></div>";
		return mailComponents.sendMail(userId, subject, text);
	}

	private static void checkUserStatus(Member member) {
		if (member.getUserStatus() == STATUS_EMAIL_REQ) {
			throw new CustomException(USER_NOT_EMAIL_AUTH);
		}

		if (member.getUserStatus() == STATUS_STOP) {
			throw new CustomException(USER_STATUS_STOP);
		}

		if (member.getUserStatus() == STATUS_WITHDRAW) {
			throw new CustomException(USER_STATUS_WITHDRAW);
		}
	}

	private static List<GrantedAuthority> getGrantedAuthorities(Member member) {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		if (ROLE_USER != member.getUserRole()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(member.getUserRole().name()));
		}
		return grantedAuthorities;
	}

}
