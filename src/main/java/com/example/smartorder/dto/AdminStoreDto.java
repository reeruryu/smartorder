package com.example.smartorder.dto;

import com.example.smartorder.entity.Store;
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
public class AdminStoreDto {
	Long id;

	String userId;

	String storeName;

	String zipcode;
	String addr;
	String addrDetail;

	LocalDateTime regDt;


	public static AdminStoreDto of(Store store) {
		return AdminStoreDto.builder()
			.id(store.getId())
			.userId(store.getMember().getUserId())
			.storeName(store.getStoreName())
			.zipcode(store.getZipcode())
			.addr(store.getAddr())
			.addrDetail(store.getAddrDetail())
			.regDt(store.getRegDt())
			.build();
	}

}
