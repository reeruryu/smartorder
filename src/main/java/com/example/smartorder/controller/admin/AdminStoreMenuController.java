package com.example.smartorder.controller.admin;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.model.AdminStoreMenu;
import com.example.smartorder.service.admin.AdminStoreMenuService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/store-menu")
public class AdminStoreMenuController {
	private final AdminStoreMenuService adminStoreMenuService;

	@PostMapping("/add/{storeId}.do") // 새로운 가게 모든 메뉴 첫 등록 시
	public ApiResponse addAllStoreMenu(@PathVariable Long storeId) {

		adminStoreMenuService.addAllStoreMenu(storeId);

		return ApiResponse.OK();
	}

	@PostMapping("/add-new.do") // 신 메뉴 출시
	public ApiResponse addNewStoreMenu(@RequestBody @Valid AdminStoreMenu.AddNew parameter) {

		adminStoreMenuService.addNewStoreMenu(parameter.getMenuIdList());

		return ApiResponse.OK();
	}

}
