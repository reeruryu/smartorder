package com.example.smartorder.controller.pay;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.model.ConvPayParam;
import com.example.smartorder.service.pay.ConvPayService;
import java.security.Principal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/convpay")
public class ConvPayController {

	private final ConvPayService convPayService;

	@PostMapping
	public ApiResponse addMoney(@RequestBody @Valid ConvPayParam.AddMoney parameter,
		Principal principal) {

		convPayService.addMoney(parameter.getAmount(), principal.getName());

		return ApiResponse.OK();
	}

}
