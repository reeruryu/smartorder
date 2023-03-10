package com.example.smartorder.model;

import com.example.smartorder.entity.Category;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AdminCategory {

	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	public static class Add {

		@NotBlank(message = "카테고리 이름을 입력하세요")
		String categoryName;

		public Category toEntity() {
			return Category.builder()
				.categoryName(this.getCategoryName())
				.sortValue(0)
				.build();
		}
	}

}
