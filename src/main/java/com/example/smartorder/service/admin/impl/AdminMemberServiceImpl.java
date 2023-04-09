package com.example.smartorder.service.admin.impl;

import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.dto.MemberDto;
import com.example.smartorder.entity.Member;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.service.admin.AdminMemberService;
import com.example.smartorder.type.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminMemberServiceImpl implements AdminMemberService {
	private final MemberRepository memberRepository;

	@Override
	public Page<MemberDto> list(String userId, Pageable pageable) {
		Page<Member> members;

		if (userId == null) {
			members = memberRepository.findAll(pageable);
		} else {
			members = memberRepository.findByUserIdContaining(userId, pageable);
		}

		return members.map(MemberDto::of);
	}

	@Override
	public void updateRole(Long id, UserRole userRole) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		member.setUserRole(userRole);
		memberRepository.save(member);

	}
}
