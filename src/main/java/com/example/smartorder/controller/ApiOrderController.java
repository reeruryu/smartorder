package com.example.smartorder.controller;

import static com.example.smartorder.common.error.ErrorCode.*;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.common.error.ErrorCode;
import com.example.smartorder.common.exception.NotFoundException;
import com.example.smartorder.dto.OrderHistDto;
import com.example.smartorder.model.GetCeoOrderHist;
import com.example.smartorder.model.OrderCancel;
import com.example.smartorder.model.OrderCeoCancel;
import com.example.smartorder.service.OrderService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order")
public class ApiOrderController {
	private final OrderService orderService;

	@GetMapping("/{userId}")
	public ApiResponse<Page<OrderHistDto>> getOrderHist(@NotNull @PathVariable String userId,
		@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
		@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate endDate,
		@PageableDefault(size = 10, sort = "regDt", direction = Direction.DESC) Pageable pageable) {

		if (startDate.isAfter(endDate)) {
			throw new NotFoundException(END_FASTER_THAN_START); // 익셉션 수정 계획 ..
		}

		Page<OrderHistDto> orderDtos = orderService.getOrderHist(pageable, startDate, endDate, userId);

		return ApiResponse.OK(orderDtos);
	}

	// TODO  custom validator를 따로 만들어서 처리하기
	@GetMapping("/ceo/{userId}")
	public ApiResponse<Page<OrderHistDto>> getCeoOrderHist(@NotNull @PathVariable String userId,
		@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
		@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate endDate,
		@RequestBody GetCeoOrderHist parameter,
		@PageableDefault(size = 10, sort = "regDt", direction = Direction.DESC) Pageable pageable) {

		if (startDate.isAfter(endDate)) {
			throw new NotFoundException(END_FASTER_THAN_START); // 익셉션 수정 계획 ..
		}

		Page<OrderHistDto> orderDtos = orderService.getCeoOrderHist(pageable, startDate, endDate, parameter.getStoreId(), userId);

		return ApiResponse.OK(orderDtos);
	}

	@PostMapping("/{userId}/cancel")
	public ApiResponse<Long> orderCancel(@NotNull @PathVariable String userId,
		@RequestBody OrderCancel parameter) {

		Long orderId = orderService.orderCancel(parameter, userId);

		return ApiResponse.OK(orderId);
	}

	@PostMapping("/ceo/{userId}/cancel")
	public ApiResponse<Long> orderCeoCancel(@NotNull @PathVariable String userId,
		@RequestBody OrderCeoCancel parameter) {

		Long orderId = orderService.orderCeoCancel(parameter, userId);

		return ApiResponse.OK(orderId);
	}

}
