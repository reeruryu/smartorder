package com.example.smartorder.dto;

import com.example.smartorder.entity.Member;
import com.example.smartorder.type.UserRole;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MemberDto {
	private String userId;
	private String userName;
	private String phone;
	private UserRole userRole;
	private LocalDateTime regDt;
	private LocalDateTime wdDt;


	public static MemberDto of(Member member) {
		return MemberDto.builder()
			.userId(member.getUserId())
			.userName(member.getUserName())
			.phone(member.getPhone())
			.regDt(member.getRegDt())
			.wdDt(member.getWdDt())
			.userRole(member.getUserRole())
			.build();
	}
}
