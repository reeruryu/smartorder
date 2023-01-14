package com.example.smartorder.member.service.Impl;

import static org.mockito.ArgumentMatchers.any;

import com.example.smartorder.common.component.MailComponents;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.service.auth.Impl.AuthServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
	
	@Mock
	private MailComponents mailComponents;
	@Mock
	private MemberRepository memberRepository;

	@InjectMocks
	private AuthServiceImpl memberService;

	// 슬랙 연동 확인


}