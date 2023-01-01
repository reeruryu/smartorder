package com.example.smartorder.admin.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryInput {
	private long id;
	private String categoryName;
	private int sortValue;

}
