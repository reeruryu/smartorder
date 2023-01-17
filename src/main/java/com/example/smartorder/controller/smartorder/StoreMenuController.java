package com.example.smartorder.controller.smartorder;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.dto.FrontStoreMenuDto;
import com.example.smartorder.dto.StoreMenuDto;
import com.example.smartorder.model.StoreMenuParam;
import com.example.smartorder.service.smartorder.StoreMenuService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class StoreMenuController {

	private final StoreMenuService storeMenuService;

	@GetMapping("/ceo/store-menu/{storeId}")
	public ApiResponse listStoreMenu(@PathVariable Long storeId,
		@RequestParam(required = false) Long categoryId,
		Principal principal) {

		List<StoreMenuDto> storeMenuList =
			storeMenuService.listStoreMenu(storeId, categoryId, principal.getName());

		return ApiResponse.OK(storeMenuList);
	}
	@PutMapping("/ceo/store-menu/hidden/{storeId}")
	public ApiResponse updateHidden(
		@PathVariable Long storeId, @RequestParam Long storeMenuId,
		@RequestBody StoreMenuParam.UpdateHidden parameter, Principal principal) {

		storeMenuService.updateHidden(storeId, storeMenuId, parameter.isHidden(), principal.getName());

		return ApiResponse.OK();
	}

	@PutMapping("/ceo/store-menu/saleState/{storeId}")
	public ApiResponse updateSaleState(
		@PathVariable Long storeId, @RequestParam Long storeMenuId,
		@RequestBody StoreMenuParam.UpdateSaleState parameter, Principal principal) {

		storeMenuService.updateSaleState(storeId, storeMenuId, parameter.getSaleState(), principal.getName());

		return ApiResponse.OK();
	}

	@GetMapping("/store-menu/{storeId}") // 고객용
	public ApiResponse frontStoreMenu(@PathVariable Long storeId,
		@RequestParam(required = false) Long categoryId,
		@PageableDefault(size = 10) Pageable pageable,
		Principal principal) {

		log.info(categoryId + " ");
		log.info(storeId + " ");

		Page<FrontStoreMenuDto> storeMenuList =
			storeMenuService.frontStoreMenu(storeId, categoryId, pageable, principal.getName());

		return ApiResponse.OK(storeMenuList);
	}
}
