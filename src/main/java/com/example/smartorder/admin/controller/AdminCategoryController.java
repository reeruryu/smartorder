package com.example.smartorder.admin.controller;

import com.example.smartorder.admin.dto.MemberDto;
import com.example.smartorder.admin.model.MemberParam;
import com.example.smartorder.admin.service.AdminMemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class AdminCategoryController {

	@GetMapping("/admin/category/list.do")
	public String list(Model model) {


		return "admin/category/list";
	}
}
