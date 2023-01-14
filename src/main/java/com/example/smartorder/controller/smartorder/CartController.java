package com.example.smartorder.controller.smartorder;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.dto.CartMenuDto;
import com.example.smartorder.model.CartParam;
import com.example.smartorder.service.smartorder.CartService;
import java.security.Principal;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartController {
	private final CartService cartService;

	@PostMapping
	public ApiResponse addCartMenu(@RequestBody @Valid CartParam.Add parameter,
		Principal principal) {

		cartService.addCartMenu(parameter, principal.getName());

		return ApiResponse.OK();
	}

	@GetMapping
	public ApiResponse cartMenuList(Principal principal) {

		List<CartMenuDto> list = cartService.getCartMenuList(principal.getName());

		return ApiResponse.OK(list);
	}

	@PutMapping
	public ApiResponse updateCartMenu(@Valid @RequestBody CartParam.Update parameter,
		Principal principal) {

		cartService.updateCartMenu(parameter, principal.getName());

		return ApiResponse.OK();
	}

	@DeleteMapping("/{cartMenuId}")
	public ApiResponse deleteCartMenu(@PathVariable Long cartMenuId, Principal principal) {

		cartService.deleteCartMenu(cartMenuId, principal.getName());

		return ApiResponse.OK();
	}

	 // 주문 번호 리턴
	@PostMapping("/order")
	public ApiResponse orderCartMenu(@Valid @RequestBody CartParam.Order parameter,
		Principal principal) {

		Long orderId = cartService.orderCartMenu(parameter.getCartId(), principal.getName());

		return ApiResponse.OK(orderId);
	}

}
