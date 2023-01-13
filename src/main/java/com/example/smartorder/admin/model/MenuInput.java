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
public class MenuInput {

	private Long id;

	private long categoryId;

	private String menuName;

	private long menuPrice;
	private int sortValue;

//	private String imagePath;

	private String description;

	private String idList;

}
