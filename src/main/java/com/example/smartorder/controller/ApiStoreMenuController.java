package com.example.smartorder.controller;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.dto.StoreMenuDto;
import com.example.smartorder.model.UpdateStoreMenu;
import com.example.smartorder.service.StoreMenuService;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ApiStoreMenuController {

	private final StoreMenuService storeMenuService;

	// TODO spring security -> Principal principal -> principal.getName() -> service에 userId 추가

	@GetMapping("/ceo/store/{storeId}/list/menu")
	public ApiResponse<List<StoreMenuDto>> listStoreMenu(@NotNull @PathVariable Long storeId,
		@RequestParam(required = false) Long categoryId) {

		List<StoreMenuDto> storeMenuList = storeMenuService.listStoreMenu(storeId,
			categoryId);

		return ApiResponse.OK(storeMenuList);
	}
	@PutMapping("/ceo/store/{storeId}/update/menu") //
	public ApiResponse updateStoreMenu(@NotNull @PathVariable Long storeId,
		@RequestParam Long storeMenuId, @RequestBody UpdateStoreMenu parameter) {

		storeMenuService.updateStoreMenu(storeId, storeMenuId, parameter); // , userId);

		return ApiResponse.OK();
	}
}
