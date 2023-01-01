package com.example.smartorder.admin.controller;

import com.example.smartorder.admin.dto.CategoryDto;
import com.example.smartorder.admin.dto.MemberDto;
import com.example.smartorder.admin.model.CategoryInput;
import com.example.smartorder.admin.model.MemberParam;
import com.example.smartorder.admin.service.AdminCategoryService;
import com.example.smartorder.admin.service.AdminMemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class AdminCategoryController {

	private final AdminCategoryService adminCategoryService;

	@GetMapping("/admin/category/list.do")
	public String list(Model model) {

		List<CategoryDto> list = adminCategoryService.list();
		model.addAttribute("list", list);

		return "admin/category/list";
	}

	@PostMapping("/admin/category/add.do")
	public String add(Model model, CategoryInput parameter,
		RedirectAttributes re) {

		boolean result = adminCategoryService.add(parameter);
		if (!result) {
			re.addFlashAttribute("errorMessage", "중복된 카테고리가 존재합니다.");
		}

		return "redirect:/admin/category/list.do";
	}

	@PostMapping("/admin/category/update.do")
	public String update(Model model, CategoryInput parameter,
		RedirectAttributes re) {

		boolean result = adminCategoryService.update(parameter);
		if (!result) {
			re.addFlashAttribute("errorMessage", "중복된 카테고리가 존재합니다.");
		}

		return "redirect:/admin/category/list.do";
	}

	@PostMapping("/admin/category/delete.do")
	public String del(CategoryInput parameter) {

		boolean result = adminCategoryService.del(parameter);

		return "redirect:/admin/category/list.do";
	}


}
