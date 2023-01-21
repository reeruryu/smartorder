package com.example.smartorder.controller.pay;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.model.PayParam;
import com.example.smartorder.service.pay.PayService;
import java.security.Principal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pay")
public class PayController {

	private final PayService payService;

	@PostMapping
	public ApiResponse pay(@RequestBody @Valid PayParam parameter,
		Principal principal) {

		payService.pay(parameter, principal.getName());

		return ApiResponse.OK();
	}

}
