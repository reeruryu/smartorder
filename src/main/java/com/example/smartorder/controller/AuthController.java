package com.example.smartorder.controller;

import com.example.smartorder.model.Auth;
import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.component.TokenProvider;
import com.example.smartorder.dto.AuthDto;
import com.example.smartorder.service.AuthService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;
	private final TokenProvider tokenProvider;

	@PostMapping("/register")
	public ApiResponse register(@Valid @RequestBody Auth.Register parameter,
		BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			return ApiResponse.fail(errors);
		}

		authService.register(parameter);
		return ApiResponse.OK();
	}

	@PostMapping("/login")
	public ApiResponse<String> login(@RequestBody Auth.Login parameter,
		BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			return ApiResponse.fail(errors);
		}

		AuthDto authDto = authService.login(parameter);
		String token = tokenProvider.generateToken(authDto.getUserId(), authDto.getUserRole());

		return ApiResponse.OK(token);
	}

	@GetMapping("/email")
	public ApiResponse emailAuth(@RequestParam String uuid) {

		authService.emailAuth(uuid);

		return ApiResponse.OK();
	}

}
