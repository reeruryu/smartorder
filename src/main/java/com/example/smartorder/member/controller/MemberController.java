package com.example.smartorder.member.controller;

import com.example.smartorder.member.model.MemberInput;
import com.example.smartorder.member.service.MemberService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
public class MemberController {

	private final MemberService memberService;

	@RequestMapping("/member/login")
	public String login() {
		return "member/login";
	}

	@GetMapping("/member/register")
	public String register() {
		return "member/register";
	}

	@PostMapping("/member/register")
	public String registerSubmit(Model model, MemberInput parameter) {

		boolean result = memberService.register(parameter);

		model.addAttribute("result", result);

		return "member/register_complete";
	}

	@GetMapping("/member/email-auth")
	public String emailAuth(Model model, HttpServletRequest request) {
		String uuid = request.getParameter("uuid");

		boolean result = memberService.emailAuth(uuid);
		model.addAttribute("result", result);

		return "member/email_auth";
	}

}
