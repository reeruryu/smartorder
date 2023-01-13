package com.example.smartorder.admin.model;

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
public class StoreInput {

	private Long id;

	private String userId;
	private String storeName;
	private String zipcode;
	private String addr;
	private String addrDetail;

	private String idList;


}
