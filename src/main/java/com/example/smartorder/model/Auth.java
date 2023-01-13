package com.example.smartorder.model;

import com.example.smartorder.entity.Member;
import com.example.smartorder.type.UserRole;
import com.example.smartorder.type.UserStatus;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

public class Auth {

	@Data
	public static class Register {

		@NotBlank(message = "이메일 주소를 입력하세요.")
		@Email(message = "이메일 주소가 올바르지 않습니다.")
		String userId;

		@NotBlank(message = "이름을 입력하세요")
		String userName;

		@NotBlank(message = "휴대폰 번호를 입력하세요")
		@Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
		String phone;

		@NotBlank(message = "비밀번호를 입력하세요.")
		String pw;

		public Member toEntity(String encPassword, String uuid) {
			return Member.builder()
				.userId(this.userId)
				.userName(this.userName)
				.phone(this.phone)
				.pw(encPassword)
				.emailAuthKey(uuid)
				.userRole(UserRole.ROLE_USER)
				.userStatus(UserStatus.STATUS_EMAIL_REQ)
				.build();
		}
	}

	@Data
	public static class Login {

		@NotBlank(message = "이메일 주소를 입력하세요.")
		@Email(message = "이메일 주소가 올바르지 않습니다.")
		String userId;

		@NotBlank(message = "비밀번호를 입력하세요.")
		String pw;
	}
}
