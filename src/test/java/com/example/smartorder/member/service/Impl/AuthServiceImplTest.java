package com.example.smartorder.member.service.Impl;

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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.smartorder.common.component.MailComponents;
import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.common.util.PasswordUtils;
import com.example.smartorder.dto.AuthDto;
import com.example.smartorder.entity.Member;
import com.example.smartorder.model.Auth;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.service.auth.Impl.AuthServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
	
	@Mock
	private MailComponents mailComponents;
	@Mock
	private MemberRepository memberRepository;
	@InjectMocks
	private AuthServiceImpl authService;

	/**
	 * 회원가입 테스트
	 */
	@Test
	@DisplayName("회원가입 성공")
	void registerSuccess() {
		// given
		Auth.Register req = Auth.Register.builder()
			.userId("hong@gmail.com")
			.userName("홍길동")
			.phone("010-0000-1111")
			.pw("hongPw").build();
		Member member = req.toEntity("encPw", "uuid");

		given(memberRepository.existsByUserId(anyString()))
			.willReturn(false);
		given(mailComponents.sendMail(anyString(), anyString(), anyString()))
			.willReturn(true);
		given(memberRepository.save(any()))
			.willReturn(member);
		ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);

		// when
		authService.register(req);

		// then
		verify(memberRepository, times(1)).save(captor.capture());
		assertEquals(member.getUserId(), captor.getValue().getUserId());
		assertEquals(ROLE_USER, captor.getValue().getUserRole());
		assertEquals(STATUS_EMAIL_REQ, captor.getValue().getUserStatus());
		assertNotNull(captor.getValue().getEmailAuthKey());
	}

	@Test
	@DisplayName("회원가입 실패 - 회원 아이디 중복")
	void registerFail_alreadyUserExists() {
		// given
		Auth.Register req = Auth.Register.builder()
			.userId("hong@gmail.com")
			.userName("홍길동")
			.phone("010-0000-1111")
			.pw("hongPw").build();

		given(memberRepository.existsByUserId(anyString()))
			.willReturn(true);

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> authService.register(req));

		// then
		assertEquals(ALREADY_USERID_EXISTS, exception.getErrorCode());
	}

	@Test
	@DisplayName("회원가입 실패 - 메일 전송 실패")
	void registerFail_failSendMail() {
		// given
		Auth.Register req = Auth.Register.builder()
			.userId("hong@gmail.com")
			.userName("홍길동")
			.phone("010-0000-1111")
			.pw("hongPw").build();

		given(memberRepository.existsByUserId(anyString()))
			.willReturn(false);
		given(mailComponents.sendMail(anyString(), anyString(), anyString()))
			.willReturn(false);

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> authService.register(req));

		// then
		assertEquals(FAIL_SEND_MAIL, exception.getErrorCode());
	}

	/**
	 * 로그인 테스트
	 */
	@Test
	@DisplayName("로그인 성공")
	void loginSuccess() {
		// given
		Auth.Login req = Auth.Login.builder()
			.userId("hong@gmail.com")
			.pw("hongPw").build();
		Member member = Member.builder()
			.userId("member@gmail.com")
			.pw(PasswordUtils.encPassword(req.getPw()))
			.userRole(ROLE_USER)
			.userStatus(STATUS_ING)
			.build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));

		// when
		AuthDto authDto = authService.login(req);

		// then
		assertEquals(member.getUserId(), authDto.getUserId());
		assertTrue(authDto.getUserRole().contains(member.getUserRole().name()));
	}

	@Test
	@DisplayName("로그인 실패 - 해당 아이디 없음")
	void loginFail_notFoundUserId() {
		// given
		Auth.Login req = Auth.Login.builder()
			.userId("hong@gmail.com")
			.pw("hongPw").build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> authService.login(req));

		// then
		assertEquals(NOT_FOUND_USER, exception.getErrorCode());
	}

	@Test
	@DisplayName("로그인 실패 - 비밀번호 불일치")
	void loginFail_passwordMismatch() {
		// given
		Auth.Login req = Auth.Login.builder()
			.userId("hong@gmail.com")
			.pw("hongPw").build();
		Member member = Member.builder()
			.userId("member@gmail.com")
			.pw(PasswordUtils.encPassword("!hongPw"))
			.build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> authService.login(req));

		// then
		assertEquals(NOT_FOUND_USER, exception.getErrorCode());
	}

	@Test
	@DisplayName("로그인 실패 - 이메일 인증을 안한 유저")
	void loginFail_unavailableUserStatus_emailReq() {
		// given
		Auth.Login req = Auth.Login.builder()
			.userId("hong@gmail.com")
			.pw("hongPw").build();
		Member member = Member.builder()
			.userId("member@gmail.com")
			.pw(PasswordUtils.encPassword(req.getPw()))
			.userRole(ROLE_USER)
			.userStatus(STATUS_EMAIL_REQ)
			.build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> authService.login(req));

		// then
		assertEquals(USER_NOT_EMAIL_AUTH, exception.getErrorCode());
	}

	@Test
	@DisplayName("로그인 실패 - 정지된 유저")
	void loginFail_unavailableUserStatus_stop() {
		// given
		Auth.Login req = Auth.Login.builder()
			.userId("hong@gmail.com")
			.pw("hongPw").build();
		Member member = Member.builder()
			.userId("member@gmail.com")
			.pw(PasswordUtils.encPassword(req.getPw()))
			.userRole(ROLE_USER)
			.userStatus(STATUS_STOP)
			.build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> authService.login(req));

		// then
		assertEquals(USER_STATUS_STOP, exception.getErrorCode());
	}

	@Test
	@DisplayName("로그인 실패 - 탈퇴한 유저")
	void loginFail_unavailableUserStatus_withdraw() {
		// given
		Auth.Login req = Auth.Login.builder()
			.userId("hong@gmail.com")
			.pw("hongPw").build();
		Member member = Member.builder()
			.userId("member@gmail.com")
			.pw(PasswordUtils.encPassword(req.getPw()))
			.userRole(ROLE_USER)
			.userStatus(STATUS_WITHDRAW)
			.build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> authService.login(req));

		// then
		assertEquals(USER_STATUS_WITHDRAW, exception.getErrorCode());
	}

	/**
	 * 이메일 인증 테스트
	 */
	@Test
	@DisplayName("이메일 인증 성공")
	void emailAuthSuccess() {
		// given
		Member member = Member.builder()
			.userId("member@gmail.com")
			.pw("encPw")
			.userStatus(STATUS_EMAIL_REQ)
			.emailAuthKey("uuid")
			.build();

		given(memberRepository.findByEmailAuthKey(anyString()))
			.willReturn(Optional.of(member));
		ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);

		// when
		authService.emailAuth("uuid");

		// then
		verify(memberRepository, times(1)).save(captor.capture());
		assertEquals(STATUS_ING, captor.getValue().getUserStatus());
		assertNotNull(captor.getValue().getEmailAuthDt());

	}

	@Test
	@DisplayName("이메일 인증 실패 - 해당 유저 없음")
	void emailAuthFail_notFoundUser() {
		// given
		given(memberRepository.findByEmailAuthKey(anyString()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> authService.emailAuth("uuid"));

		// then
		assertEquals(NOT_FOUND_USER, exception.getErrorCode());
	}

	@Test
	@DisplayName("이메일 인증 실패 - 이미 인증을 완료한 유저")
	void emailAuthFail_alreadyAuthEmail() {
		// given
		Member member = Member.builder()
			.userId("member@gmail.com")
			.userStatus(STATUS_ING)
			.build();

		given(memberRepository.findByEmailAuthKey(anyString()))
			.willReturn(Optional.of(member));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> authService.emailAuth("uuid"));

		// then
		assertEquals(ALREADY_AUTH_EMAIL, exception.getErrorCode());
	}

}