package com.example.smartorder.dto;

import com.example.smartorder.entity.StoreMenu;
import com.example.smartorder.type.SaleState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FrontStoreMenuDto {

	Long id;
	String menuName;
	long menuPrice;
	SaleState saleState;

	public static FrontStoreMenuDto of(StoreMenu storeMenu) {
		return FrontStoreMenuDto.builder()
			.id(storeMenu.getId())
			.menuName(storeMenu.getMenu().getMenuName())
			.menuPrice(storeMenu.getMenu().getMenuPrice())
			.saleState(storeMenu.getSaleState())
			.build();
	}
}
