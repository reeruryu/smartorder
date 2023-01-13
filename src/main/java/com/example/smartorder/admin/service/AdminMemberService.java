package com.example.smartorder.admin.service;

import com.example.smartorder.admin.dto.MemberDto;
import com.example.smartorder.admin.model.MemberParam;
import com.example.smartorder.type.UserRole;
import java.util.List;

public interface AdminMemberService {

	List<MemberDto> list(MemberParam parameter);

	MemberDto detail(String userId);

	boolean updateRole(String userId, UserRole userRole);
}
