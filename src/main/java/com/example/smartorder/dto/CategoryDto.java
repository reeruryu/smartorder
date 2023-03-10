package com.example.smartorder.dto;

import com.example.smartorder.entity.Category;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CategoryDto {
	Long id;
	String categoryName;
	int sortValue;

	public static List<CategoryDto> of(List<Category> list) {
		if (list != null) {
			List<CategoryDto> categoryList = new ArrayList<>();
			for (Category x: list) {
				categoryList.add(of(x));
			}
			return categoryList;
		}

		return null;
	}

	public static CategoryDto of(Category category) {
		return CategoryDto.builder()
			.id(category.getId())
			.categoryName(category.getCategoryName())
			.sortValue(category.getSortValue())
			.build();
	}

}
