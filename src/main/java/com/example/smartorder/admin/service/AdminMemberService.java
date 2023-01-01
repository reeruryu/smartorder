package com.example.smartorder.admin.service;

import com.example.smartorder.admin.dto.MemberDto;
import com.example.smartorder.admin.model.MemberParam;
import java.util.List;

public interface AdminMemberService {

	List<MemberDto> list(MemberParam parameter);

	MemberDto detail(String userId);
}
