package com.example.smartorder.controller;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.model.AddCartMenu;
import com.example.smartorder.service.CartService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ApiCartController {
	private final CartService cartService;

	@PostMapping("/cart/{userId}")
	public ApiResponse<String> addCartMenu(@NotNull @PathVariable String userId,
		@Valid @RequestBody AddCartMenu parameter) {
		cartService.addCartMenu(parameter, userId);

		return ApiResponse.OK();
	}
}
