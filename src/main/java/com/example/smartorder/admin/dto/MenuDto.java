package com.example.smartorder.admin.dto;

import com.example.smartorder.category.entity.Category;
import com.example.smartorder.menu.entity.Menu;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.ManyToOne;
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

	private Long categoryId;

	private String menuName;

	private long menuPrice;
	private int sortValue;
	private String description;

	private LocalDateTime regDt;

	long totalCount;
	long seq;

	public static MenuDto of(Menu menu) {
		return MenuDto.builder()
			.id(menu.getId())
			.categoryId(menu.getCategory().getId())
			.menuName(menu.getMenuName())
			.menuPrice(menu.getMenuPrice())
			.sortValue(menu.getSortValue())
			.description(menu.getDescription())
			.regDt(menu.getRegDt())
			.build();
	}

	public static List<MenuDto> of(List<Menu> menus) {

		if (menus == null) {
			return null;
		}

		List<MenuDto> menuList = new ArrayList<>();
		for (Menu x: menus) {
			menuList.add(MenuDto.of(x));
		}
		return menuList;

	}

}
