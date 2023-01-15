package com.example.smartorder.model;

import com.example.smartorder.entity.Category;
import com.example.smartorder.entity.Menu;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AdminMenu {
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Data
	public static class Add { // update와 같아서 같이 쓰임

		@NotNull(message = "카테고리를 선택해 주세요.")
		Long categoryId;

		@NotBlank(message = "메뉴명을 입력하세요.")
		String menuName;

		@Min(value = 0, message = "최소 0원 이상 입력해 주세요.")
		long menuPrice;

		int sortValue;

		@NotNull(message = "메뉴 설명란을 입력해 주세요.")
		String description;

		public Menu toEntity(Category category) {
			return Menu.builder()
				.category(category)
				.menuName(this.menuName)
				.menuPrice(this.menuPrice)
				.description(this.description)
				.sortValue(this.sortValue)
				.build();
		}
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Data
	public static class Del {

		@NotNull(message = "삭제할 메뉴를 선택해 주세요.")
		List<Long> idList;
	}

}
