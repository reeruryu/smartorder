package com.example.smartorder.controller.pay;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.dto.PointDto;
import com.example.smartorder.service.pay.PointService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/point")
public class PointController {

	private final PointService pointService;

	@GetMapping
	public ApiResponse getBalance(Principal principal) {
		PointDto pointDto = pointService.getBalance(principal.getName());

		return ApiResponse.OK(pointDto);
	}

}
