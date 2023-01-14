package com.example.smartorder.controller.smartorder;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.model.StoreParam;
import com.example.smartorder.service.smartorder.StoreService;
import java.security.Principal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ceo/store")
public class StoreController {
	private final StoreService storeService;

	@PutMapping("/{storeId}/openDay")
	public ApiResponse updateStoreOpenDay(@PathVariable Long storeId,
		@RequestBody @Valid StoreParam.OpenDay parameter, Principal principal) {

		storeService.updateStoreOpenDay(storeId, parameter.getOpenDayList(), principal.getName());

		return ApiResponse.OK();
	}

	@PutMapping("/{storeId}/openTime")
	public ApiResponse updateStoreOpenTime(@PathVariable Long storeId,
		@RequestBody @Valid StoreParam.OpenTime parameter, Principal principal) {

		storeService.updateStoreOpenTime(storeId, parameter, principal.getName());

		return ApiResponse.OK();
	}

	@PutMapping("/{storeId}/openYn")
	public ApiResponse updateStoreOpenYn(@PathVariable Long storeId,
		@RequestBody @Valid StoreParam.OpenYn parameter, Principal principal) {

		storeService.updateStoreOpenYn(storeId, parameter.isOpenYn(), principal.getName());

		return ApiResponse.OK();
	}
}
