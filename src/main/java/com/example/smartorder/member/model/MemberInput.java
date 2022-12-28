package com.example.smartorder.member.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
@ToString
public class MemberInput {
	String userId;
	String userName;
	String phone;
	String pw;

}
