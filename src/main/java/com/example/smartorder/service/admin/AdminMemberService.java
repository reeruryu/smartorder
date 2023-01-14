package com.example.smartorder.service.admin;

import com.example.smartorder.dto.MemberDto;
import com.example.smartorder.type.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminMemberService {

	/**
	 * 유저 리스트를 보여줍니다.
	 */
	Page<MemberDto> list(String userId, Pageable pageable);


	/**
	 * UserRole을 변경합니다.
	 */
	void updateRole(Long id, UserRole userRole);
}
