package com.example.smartorder.admin.dto;

import com.example.smartorder.member.entity.Member;
import com.example.smartorder.member.type.UserRole;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
	private boolean emailAuthYn;

	private UserRole userRole;
	private LocalDateTime regDt;
	private LocalDateTime wdDt;

	private long totalCount;
	private long seq;


	public static MemberDto of(Member member) {
		return MemberDto.builder()
			.userId(member.getUserId())
			.userName(member.getUserName())
			.phone(member.getPhone())
			.regDt(member.getRegDt())
			.emailAuthYn(member.isEmailAuthYn())
			.userRole(member.getUserRole())
			.build();
	}

	public String getRegDtText() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd MM:mm:ss");
		return regDt != null ? regDt.format(formatter) : "";
	}
}
