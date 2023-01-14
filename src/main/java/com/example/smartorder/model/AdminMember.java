package com.example.smartorder.model;

import com.example.smartorder.type.valid.EnumTypeValid;
import com.example.smartorder.type.UserRole;
import javax.validation.constraints.NotNull;
import lombok.Data;

public class AdminMember {

	@Data
	public static class UpdateRole {

		@NotNull(message = "유저를 선택해주세요")
		Long memberId;

		@EnumTypeValid(enumClass = UserRole.class, message = "invalid UserRole")
		@NotNull(message = "UserRole을 선택해 주세요.")
		UserRole userRole;
	}

}
