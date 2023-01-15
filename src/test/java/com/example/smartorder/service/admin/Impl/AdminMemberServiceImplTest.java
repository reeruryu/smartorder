package com.example.smartorder.service.admin.Impl;

//import static org.junit.jupiter.api.Assertions.*;

import static com.example.smartorder.type.UserRole.ROLE_CEO;
import static com.example.smartorder.type.UserRole.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.smartorder.common.error.ErrorCode;
import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.dto.MemberDto;
import com.example.smartorder.entity.Member;
import com.example.smartorder.repository.MemberRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
class AdminMemberServiceImplTest {
	@Mock
	private MemberRepository memberRepository;
	@InjectMocks
	private AdminMemberServiceImpl memberService;

	/**
	 * 메뉴 리스트 보기
	 */
	@Test
	@DisplayName("회원 리스트 보기 성공 - 전체보기(userId 입력 받지 않았을 때)")
	void listSuccess_userIdNull() {
		// given
		List<Member> memberList = Arrays.asList(
			Member.builder()
				.userId("aaa@gmail.com")
				.userName("이름a")
				.build(),
			Member.builder()
				.userId("bbb@gmail.com")
				.userName("이름b")
				.build()
		);
		Page<Member> members = new PageImpl<>(memberList);

		given(memberRepository.findAll(any(Pageable.class)))
			.willReturn(members);

		// when
		Page<MemberDto> memberDtos =
			memberService.list(null, Pageable.unpaged());

		// then
		assertEquals("aaa@gmail.com", memberDtos.getContent().get(0).getUserId());
		assertEquals("bbb@gmail.com", memberDtos.getContent().get(1).getUserId());
		assertEquals("이름a", memberDtos.getContent().get(0).getUserName());
		assertEquals("이름b", memberDtos.getContent().get(1).getUserName());
		assertEquals(2, memberDtos.getTotalElements());
		assertEquals(1, memberDtos.getTotalPages());
		assertEquals(0, memberDtos.getNumber());
	}

	@Test
	@DisplayName("회원 리스트 보기 성공 - 검색된 아이디 찾기(userId 입력 받았을 때)")
	void listSuccess_userIdNotNull() {
		// given
		List<Member> memberList = Arrays.asList(
			Member.builder()
				.userId("aaa@gmail.com")
				.userName("이름a")
				.build(),
			Member.builder()
				.userId("aaab@gmail.com")
				.userName("이름ab")
				.build()
		);
		Page<Member> members = new PageImpl<>(memberList);

		given(memberRepository.findByUserIdContaining(anyString(), any(Pageable.class)))
			.willReturn(members);

		// when
		Page<MemberDto> memberDtos =
			memberService.list("aaa", Pageable.unpaged());

		// then
		assertEquals("aaa@gmail.com", memberDtos.getContent().get(0).getUserId());
		assertEquals("aaab@gmail.com", memberDtos.getContent().get(1).getUserId());
		assertEquals("이름a", memberDtos.getContent().get(0).getUserName());
		assertEquals("이름ab", memberDtos.getContent().get(1).getUserName());
		assertEquals(2, memberDtos.getTotalElements());
		assertEquals(1, memberDtos.getTotalPages());
		assertEquals(0, memberDtos.getNumber());

	}

	@Test
	@DisplayName("회원 권한 변경 성공")
	void updateRoleSuccess() {
		// given
		Member member = Member.builder()
			.userId("aaa@gmail.com")
			.userName("이름a")
			.userRole(ROLE_USER)
			.build();

		given(memberRepository.findById(anyLong()))
			.willReturn(Optional.of(member));
		ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);

		// when
		memberService.updateRole(1L, ROLE_CEO);

		// then
		verify(memberRepository, times(1)).save(captor.capture());
		assertEquals(ROLE_CEO, captor.getValue().getUserRole());
	}

	@Test
	@DisplayName("회원 권한 변경 실패 - 존재하지 않는 회원")
	void updateRoleFail_notFoundUser() {
		// given
		given(memberRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> memberService.updateRole(1L, ROLE_CEO));

		// then
		assertEquals(ErrorCode.NOT_FOUND_USER, exception.getErrorCode());
	}


}