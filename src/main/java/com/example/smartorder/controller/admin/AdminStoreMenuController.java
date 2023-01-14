package com.example.smartorder.controller.admin;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.model.CreateAllStoreMenu;
import com.example.smartorder.model.CreateNewStoreMenu;
import com.example.smartorder.service.StoreMenuService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AdminStoreMenuController {
	private final StoreMenuService storeMenuService;

	@PostMapping("/ceo/create/menus") // 모든 메뉴 첫 등록 시
	public ApiResponse createAllStoreMenu(@Valid @RequestBody CreateAllStoreMenu request,
		Errors errors) { // 가게 아이디

		storeMenuService.createAllStoreMenu(request.getStoreId());

		return ApiResponse.OK();
	}

	@PostMapping("/ceo/create/menu") // 신 메뉴 출시
	public ApiResponse createNewStoreMenu(@Valid @RequestBody CreateNewStoreMenu request,
		Errors errors) { // 가게 아이디

		storeMenuService.createNewStoreMenu(request.getMenuIdList());

		return ApiResponse.OK();
	}

}
