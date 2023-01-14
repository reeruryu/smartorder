package com.example.smartorder.controller.smartorder;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.dto.StoreDto;
import com.example.smartorder.service.smartorder.CustomerService;
import java.util.List;
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
	public ApiResponse getNearStoreList(
		@PathVariable String userId,
		@RequestParam double lat, @RequestParam double lnt) {

		List<StoreDto> list = customerService.getNearStoreList(userId, lat, lnt);

		return ApiResponse.OK(list);
	}



}
