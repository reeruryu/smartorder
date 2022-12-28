package com.example.smartorder.member.entity;

import com.example.smartorder.member.type.UserStatus;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Member {

	@Id
	private String userId;

	private String userName;
	private String phone;
	private String pw;

	private boolean emailAuthYn;
	private LocalDateTime emailAuthDt;
	private String emailAuthKey;


	private boolean ceoYn;
	private boolean adminYn;

	@Enumerated(EnumType.STRING)
	private UserStatus userStatus;

	@CreatedDate
	private LocalDateTime regDt;// 가입날짜
	@LastModifiedDate
	private LocalDateTime udDt; // 수정날짜
	private LocalDateTime wdDt; // 탈퇴날짜

}
