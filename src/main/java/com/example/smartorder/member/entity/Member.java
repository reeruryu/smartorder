package com.example.smartorder.member.entity;

import com.example.smartorder.entity.BaseEntity;
import com.example.smartorder.member.type.UserRole;
import com.example.smartorder.member.type.UserStatus;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
public class Member extends BaseEntity {

	@Id
	private String userId;

	private String userName;
	private String phone;
	private String pw;

	private boolean emailAuthYn;
	private LocalDateTime emailAuthDt;
	private String emailAuthKey;


	private boolean ceoYn; // 필요 없
	private boolean adminYn; // 필요 없

	@Enumerated(EnumType.STRING)
	private UserRole userRole; // userRole로 바꿈

	@Enumerated(EnumType.STRING)
	private UserStatus userStatus;

//	@CreatedDate
//	private LocalDateTime regDt;// 가입날짜
//	@LastModifiedDate
//	private LocalDateTime udDt; // 수정날짜
	private LocalDateTime wdDt; // 탈퇴날짜

}
