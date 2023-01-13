package com.example.smartorder.controller;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.dto.StoreDto;
import com.example.smartorder.service.CustomerService;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CustomerController {

	private final CustomerService customerService;

	@GetMapping("/store/{userId}")
	public ApiResponse<List<StoreDto>> getNearStoreList(
		@NotNull @PathVariable String userId,
		@RequestParam double lat, @RequestParam double lnt) {

		List<StoreDto> list = customerService.getNearStoreList(userId, lat, lnt);

		return ApiResponse.OK(list);
	}



}
