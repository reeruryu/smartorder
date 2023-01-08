package com.example.smartorder.controller;

import static com.example.smartorder.common.error.ErrorCode.*;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.dto.CartMenuDto;
import com.example.smartorder.model.OrderCartMenu;
import com.example.smartorder.model.AddCartMenu;
import com.example.smartorder.model.UpdateCartMenu;
import com.example.smartorder.service.CartService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

	@GetMapping("/cart/{userId}")
	public ApiResponse<List<CartMenuDto>> cartMenuList(
		@NotNull @PathVariable String userId) {

		List<CartMenuDto> list = cartService.getCartMenuList(userId);

		return ApiResponse.OK(list);
	}

	@PutMapping("/cart/{userId}")
	public ApiResponse<String> updateCartMenu(
		@NotNull @PathVariable String userId,
		@Valid @RequestBody UpdateCartMenu parameter) {

		cartService.updateCartMenu(parameter, userId);

		return ApiResponse.OK();
	}

	@DeleteMapping("/cart/{userId}/{cartMenuId}")
	public ApiResponse<String> deleteCartMenu(
		@NotNull @PathVariable String userId,
		@NotNull @PathVariable Long cartMenuId) {

		cartService.deleteCartMenu(cartMenuId, userId);

		return ApiResponse.OK();
	}

//	 주문 번호 리턴
	@PostMapping("/cart/{userId}/order")
	public ApiResponse<Long> orderCartMenu(@NotNull @PathVariable String userId,
		@Valid @RequestBody OrderCartMenu parameter) {

		Long orderId = cartService.orderCartMenu(parameter.getCartId(), userId);

		return ApiResponse.OK(orderId);
	}

}
