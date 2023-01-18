package com.example.smartorder.controller.smartorder;

import static com.example.smartorder.common.error.ErrorCode.END_FASTER_THAN_START;

import com.example.smartorder.common.dto.ApiResponse;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.dto.OrderHistDto;
import com.example.smartorder.model.OrderParam;
import com.example.smartorder.service.smartorder.OrderService;
import java.security.Principal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderController {
	private final OrderService orderService;

	@GetMapping("/order")
	public ApiResponse getOrderHist(
		@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
		@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate endDate,
		@PageableDefault(size = 10, sort = "regDt", direction = Direction.DESC) Pageable pageable,
		Principal principal) {

		Page<OrderHistDto> orderDtos = orderService.getOrderHist(pageable, startDate, endDate,
			principal.getName());

		return ApiResponse.OK(orderDtos);
	}

	@PostMapping("/order/cancel")
	public ApiResponse orderCancel(@RequestBody OrderParam.Cancel parameter,
		Principal principal) {

		Long orderId = orderService.orderCancel(parameter, principal.getName());

		return ApiResponse.OK(orderId);
	}

	@GetMapping("/ceo/order/{storeId}")
	public ApiResponse getCeoOrderHist(
		@PathVariable Long storeId,
		@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
		@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate endDate,
		@PageableDefault(size = 10, sort = "regDt", direction = Direction.DESC) Pageable pageable,
		Principal principal) {


		Page<OrderHistDto> orderDtos =
			orderService.getCeoOrderHist(pageable, startDate, endDate, storeId, principal.getName());

		return ApiResponse.OK(orderDtos);
	}

	@PostMapping("/ceo/order/cancel")
	public ApiResponse orderCeoCancel(@RequestBody OrderParam.CeoCancel parameter,
		Principal principal) {

		Long orderId = orderService.orderCeoCancel(parameter, principal.getName());

		return ApiResponse.OK(orderId);
	}

}
