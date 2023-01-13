package com.example.smartorder.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class StoreDto {
	private Long id;

	private String storeName;

	private String zipcode;
	private String addr;
	private String addrDetail;

	private List<Integer> openDay;

}
