package com.example.smartorder.model;

import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.Store;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

public class AdminStore {

	@Data
	public static class Add { // update와 같아서 같이 쓰임

		@NotBlank(message = "점주 아이디를 입력해 주세요.")
		String userId;

		@NotBlank(message = "가게명을 선택해 주세요.")
		String storeName;

		@NotBlank(message = "우편번호를 입력해 주세요.")
		String zipcode;

		@NotBlank(message = "주소를 입력해 주세요.")
		String addr;

		@NotBlank(message = "상세주소를 입력해 주세요.")
		String addrDetail;

		public Store toEntity(Member member) {
			return Store.builder()
				.member(member)
				.storeName(this.storeName)
				.zipcode(this.zipcode)
				.addr(this.addr)
				.addrDetail(this.addrDetail)
				.build();
		}

	}

	@Data
	public static class Del {
		@NotNull(message = "삭제할 가게를 선택해 주세요.")
		List<Long> idList;
	}
}
