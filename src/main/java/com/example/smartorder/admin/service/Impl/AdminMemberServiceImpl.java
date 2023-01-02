package com.example.smartorder.admin.service.Impl;

import com.example.smartorder.admin.dto.MemberDto;
import com.example.smartorder.admin.mapper.MemberMapper;
import com.example.smartorder.admin.model.MemberParam;
import com.example.smartorder.admin.service.AdminMemberService;
import com.example.smartorder.member.entity.Member;
import com.example.smartorder.member.repository.MemberRepository;
import com.example.smartorder.member.type.UserRole;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
@Service
public class AdminMemberServiceImpl implements AdminMemberService {

	private final MemberRepository memberRepository;
	private final MemberMapper memberMapper;

	@Override
	public List<MemberDto> list(MemberParam parameter) {

		long totalCount = memberMapper.selectListCount(parameter);

		List<MemberDto> list = memberMapper.selectList(parameter);
		if (!CollectionUtils.isEmpty(list)) {
			int i = 0;
			for (MemberDto x: list) {
				x.setTotalCount(totalCount);
				x.setSeq(totalCount - parameter.getPageStart() - i);
				i++;
			}
		}

		return list;
	}

	@Override
	public MemberDto detail(String userId) {
		Optional<Member> optionalMember = memberRepository.findById(userId);

		if (!optionalMember.isPresent()) {
			return null;
		}

		Member member = optionalMember.get();

		return MemberDto.of(member);
	}

	@Override
	public boolean updateRole(String userId, UserRole userRole) {
		Optional<Member> optionalMember = memberRepository.findById(userId);
		if (!optionalMember.isPresent()) {
			return false;
		}

		Member member = optionalMember.get();

		member.setUserRole(userRole);
		memberRepository.save(member);

		return true;
	}
}
