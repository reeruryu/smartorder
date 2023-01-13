package com.example.smartorder.admin.model;

import com.example.smartorder.type.UserRole;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MemberInput {

	private String userId;
	private UserRole userRole;

}
