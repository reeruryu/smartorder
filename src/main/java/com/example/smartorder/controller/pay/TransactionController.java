package com.example.smartorder.controller.pay;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.dto.TransactionConvPayDto;
import com.example.smartorder.dto.TransactionPointDto;
import com.example.smartorder.service.pay.TransactionService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

	private final TransactionService transactionService;

	@GetMapping("/convpay")
	public ApiResponse getConvPayTransaction(Principal principal,
		@PageableDefault(size = 20, sort = "regDt", direction = Direction.DESC) Pageable pageable) {

		Page<TransactionConvPayDto> transaction = transactionService
			.getConvPayTransaction(pageable, principal.getName());

		return ApiResponse.OK(transaction);
	}

	@GetMapping("/point")
	public ApiResponse getPointTransaction(Principal principal,
		@PageableDefault(size = 20, sort = "regDt", direction = Direction.DESC) Pageable pageable) {

		Page<TransactionPointDto> transaction = transactionService
			.getPointTransaction(pageable, principal.getName());

		return ApiResponse.OK(transaction);
	}

}
