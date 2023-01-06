package com.example.smartorder.dto;

import com.example.smartorder.type.SaleState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StoreMenuDto {

	Long id;
	String menuName;
	double menuPrice;

	SaleState saleState;
	boolean hiddenYn;

}
