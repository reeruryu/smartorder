package com.example.smartorder.dto;

import com.example.smartorder.entity.Menu;
import java.time.LocalDateTime;
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
public class MenuDto {
	private Long id;

	private String categoryName;

	private String menuName;

	private long menuPrice;
	private int sortValue;
	private String description;

	private LocalDateTime regDt;

	public static MenuDto of(Menu menu) {
		return MenuDto.builder()
			.id(menu.getId())
			.categoryName(menu.getCategory().getCategoryName())
			.menuName(menu.getMenuName())
			.menuPrice(menu.getMenuPrice())
			.sortValue(menu.getSortValue())
			.description(menu.getDescription())
			.regDt(menu.getRegDt())
			.build();
	}
}
