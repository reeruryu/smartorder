package com.example.smartorder.controller.smartorder;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.dto.StoreMenuDto;
import com.example.smartorder.model.StoreMenuParam;
import com.example.smartorder.service.smartorder.StoreMenuService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ceo/store-menu")
public class StoreMenuController {

	private final StoreMenuService storeMenuService;

	@GetMapping("/{storeId}")
	public ApiResponse listStoreMenu(@PathVariable Long storeId,
		@RequestParam(required = false) Long categoryId,
		Principal principal) {

		List<StoreMenuDto> storeMenuList =
			storeMenuService.listStoreMenu(storeId, categoryId, principal.getName());

		return ApiResponse.OK(storeMenuList);
	}
	@PutMapping("/{storeId}")
	public ApiResponse updateStoreMenu(
		@PathVariable Long storeId, @RequestParam Long storeMenuId,
		@RequestBody StoreMenuParam.Update parameter, Principal principal) {

		storeMenuService.updateStoreMenu(storeId, storeMenuId, parameter, principal.getName());

		return ApiResponse.OK();
	}
}
