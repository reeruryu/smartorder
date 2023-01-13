package com.example.smartorder.controller;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.dto.StoreMenuDto;
import com.example.smartorder.model.UpdateStoreMenu;
import com.example.smartorder.model.UpdateStoreOpenDay;
import com.example.smartorder.model.UpdateStoreOpenTime;
import com.example.smartorder.model.UpdateStoreOpenYn;
import com.example.smartorder.service.StoreMenuService;
import com.example.smartorder.service.StoreService;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ApiStoreController {
	private final StoreService storeService;

	@PutMapping("/ceo/store/{storeId}/openDay") //
	public ApiResponse updateStoreOpenDay(@NotNull @PathVariable Long storeId,
		@RequestBody UpdateStoreOpenDay parameter) {

		storeService.updateStoreOpenDay(storeId, parameter.getOpenDayList());

		return ApiResponse.OK();
	}

	@PutMapping("/ceo/store/{storeId}/openTime") //
	public ApiResponse updateStoreOpenTime(@NotNull @PathVariable Long storeId,
		@Valid @RequestBody UpdateStoreOpenTime parameter) {

		storeService.updateStoreOpenTime(storeId, parameter);

		return ApiResponse.OK();
	}

	@PutMapping("/ceo/store/{storeId}/openYn") //
	public ApiResponse updateStoreOpenYn(@NotNull @PathVariable Long storeId,
		@Valid @RequestBody UpdateStoreOpenYn parameter) {

		storeService.updateStoreOpenYn(storeId, parameter.isOpenYn());

		return ApiResponse.OK();
	}
}
