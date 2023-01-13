package com.example.smartorder.admin.dto;

import com.example.smartorder.entity.Store;
import java.time.LocalDateTime;
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
public class StoreDto {
	private Long id;

	private String userId;

	private String storeName;

	private String zipcode;
	private String addr;
	private String addrDetail;

	private LocalDateTime regDt;

	long totalCount;
	long seq;

	public static StoreDto of(Store store) {
		return StoreDto.builder()
			.id(store.getId())
			.userId(store.getMember().getUserId())
			.storeName(store.getStoreName())
			.zipcode(store.getZipcode())
			.addr(store.getAddr())
			.addrDetail(store.getAddrDetail())
			.regDt(store.getRegDt())
			.build();
	}

	public static List<StoreDto> of(List<Store> stores) {

		if (stores == null) {
			return null;
		}

		List<StoreDto> storeList = new ArrayList<>();
		for (Store x: stores) {
			storeList.add(StoreDto.of(x));
		}
		return storeList;

	}

}
