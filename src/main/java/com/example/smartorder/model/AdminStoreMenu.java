package com.example.smartorder.model;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

public class AdminStoreMenu {
	@Data
	public static class AddNew {
		@NotNull(message = "메뉴를 선택해 주세요.")
		List<Long> menuIdList;

	}
}
