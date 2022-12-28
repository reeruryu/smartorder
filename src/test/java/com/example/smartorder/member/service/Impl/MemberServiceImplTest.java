package com.example.smartorder.member.service.Impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.smartorder.component.MailComponents;
import com.example.smartorder.member.entity.Member;
import com.example.smartorder.member.exception.MemberException;
import com.example.smartorder.member.model.MemberInput;
import com.example.smartorder.member.repository.MemberRepository;
import com.example.smartorder.member.service.MemberService;
import com.example.smartorder.member.type.ErrorCode;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

	@Mock
	private MailComponents mailComponents;
	@Mock
	private MemberRepository memberRepository;

	@InjectMocks
	private MemberServiceImpl memberService;

//	@Test
//	void success() {
//		Member member = Member.builder()
//			.userId("abc@abc")
//			.emailAuthYn(false)
//			.build();
//
//		MemberException exception = assertThrows(MemberException.class,
//			() -> MemberServiceImpl.checkAvailableUserStatus(member));
//
//		assertEquals(ErrorCode.USER_NOT_EMAIL_AUTH, exception.getErrorCode());
//	}


}