package com.example.smartorder.dto;

import com.example.smartorder.entity.CartMenu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartMenuDto {
	Long id;
	private int menuCount;


	String menuName;
	long menuPrice;

	Long storeId;

	public static CartMenuDto of(CartMenu cartMenu) {
		return CartMenuDto.builder()
			.id(cartMenu.getId())
			.menuCount(cartMenu.getMenuCount())
			.menuName(cartMenu.getStoreMenu().getMenu().getMenuName())
			.menuPrice(cartMenu.getStoreMenu().getMenu().getMenuPrice())
			.storeId(cartMenu.getStoreMenu().getStore().getId())
			.build();
	}
}
