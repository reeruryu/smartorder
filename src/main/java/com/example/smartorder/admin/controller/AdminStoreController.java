package com.example.smartorder.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class AdminStoreController {
	@GetMapping("/admin/store/list.do")
	public String list(Model model) {

		return "admin/store/list";
	}

}
