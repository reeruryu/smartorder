package com.example.smartorder.admin.controller;

import com.example.smartorder.admin.dto.CategoryDto;
import com.example.smartorder.admin.dto.MenuDto;
import com.example.smartorder.admin.model.MenuInput;
import com.example.smartorder.admin.model.MenuParam;
import com.example.smartorder.admin.service.AdminCategoryService;
import com.example.smartorder.admin.service.AdminMenuService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
public class AdminMenuController extends BaseController {

	private final AdminMenuService adminMenuService;
	private final AdminCategoryService adminCategoryService;

	@GetMapping("/admin/menu/list.do")
	public String list(Model model, MenuParam parameter) {

		parameter.init();

		List<MenuDto> menuList = adminMenuService.list(parameter);

		long totalCount = 0;
		if (!CollectionUtils.isEmpty(menuList)) {
			totalCount = menuList.get(0).getTotalCount();
		}
		String queryString = parameter.getQueryString();
		String pagerHtml = getPagerHtml(totalCount, parameter.getPageSize(),
			parameter.getPageIndex(), queryString);

		model.addAttribute("list", menuList);
		model.addAttribute("menuTotalCount", totalCount);
		model.addAttribute("pager", pagerHtml);

		List<CategoryDto> categoryList = adminCategoryService.list();
		model.addAttribute("categoryList", categoryList);

		return "admin/menu/list";
	}

	@GetMapping(value = {"/admin/menu/add.do", "/admin/menu/edit.do"})
	public String add(Model model, HttpServletRequest request
		, MenuInput parameter) {

		model.addAttribute("category", adminCategoryService.list());

		boolean editMode = request.getRequestURI().contains("/edit.do");
		MenuDto detail = new MenuDto();

		if (editMode) {
			long id = parameter.getId();
			MenuDto editMenu = adminMenuService.getById(id);
			if (editMenu == null) {
				model.addAttribute("message", "메뉴 정보가 존재하지 않습니다.");
				return "common/error";
			}
			detail = editMenu;
		}

		model.addAttribute("detail", detail);
		model.addAttribute("editMode", editMode);
		return "admin/menu/add";
	}

	@PostMapping(value = {"/admin/menu/add.do", "/admin/menu/edit.do"})
	public String addSubmit(Model model, HttpServletRequest request
		, MenuInput parameter) {

		boolean editMode = request.getRequestURI().contains("/edit.do");

		if (editMode) {
			long id = parameter.getId();
			MenuDto editMenu = adminMenuService.getById(id);
			if (editMenu == null) {
				model.addAttribute("message", "강좌정보가 존재하지 않습니다.");
				return "common/error";
			}

			boolean result = adminMenuService.set(parameter);

		} else {
			boolean result = adminMenuService.add(parameter);
		}

		return "redirect:/admin/menu/list.do";
	}

	@PostMapping("/admin/menu/delete.do")
	public String del(Model model, HttpServletRequest request
		, MenuInput parameter) {

		boolean result = adminMenuService.del(parameter.getIdList());

		return "redirect:/admin/menu/list.do";
	}
}
