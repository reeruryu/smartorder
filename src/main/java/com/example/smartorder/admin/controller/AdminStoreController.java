package com.example.smartorder.admin.controller;

import com.example.smartorder.admin.dto.StoreDto;
import com.example.smartorder.admin.model.StoreInput;
import com.example.smartorder.admin.model.StoreParam;
import com.example.smartorder.admin.service.AdminStoreService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class AdminStoreController extends BaseController {

	private final AdminStoreService adminStoreService;
	@GetMapping("/admin/store/list.do")
	public String list(Model model, StoreParam parameter) {

		parameter.init();

		List<StoreDto> storeList = adminStoreService.list(parameter);

		long totalCount = 0;
		if (!CollectionUtils.isEmpty(storeList)) {
			totalCount = storeList.get(0).getTotalCount();
		}
		String queryString = parameter.getQueryString();
		String pagerHtml = getPagerHtml(totalCount, parameter.getPageSize(),
			parameter.getPageIndex(), queryString);

		model.addAttribute("list", storeList);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("pager", pagerHtml);

		return "admin/store/list";
	}

	@GetMapping(value = {"/admin/store/add.do", "/admin/store/edit.do"})
	public String add(Model model, HttpServletRequest request
		, StoreInput parameter) {

		boolean editMode = request.getRequestURI().contains("/edit.do");
		StoreDto detail = new StoreDto();

		if (editMode) {
			long id = parameter.getId();
			StoreDto editStore = adminStoreService.getById(id);
			if (editStore == null) {
				model.addAttribute("message", "가게 정보가 존재하지 않습니다.");
				return "common/error";
			}
			detail = editStore;
		}

		model.addAttribute("detail", detail);
		model.addAttribute("editMode", editMode);
		return "admin/store/add";
	}

	@PostMapping(value = {"/admin/store/add.do", "/admin/store/edit.do"})
	public String addSubmit(Model model, HttpServletRequest request
		, StoreInput parameter) {

		boolean editMode = request.getRequestURI().contains("/edit.do");

		if (editMode) {
			long id = parameter.getId();
			StoreDto editStore = adminStoreService.getById(id);
			if (editStore == null) {
				model.addAttribute("message", "가게 정보가 존재하지 않습니다.");
				return "common/error";
			}

			boolean result = adminStoreService.set(parameter);

		} else {
			boolean result = adminStoreService.add(parameter);
		}

		return "redirect:/admin/store/list.do";
	}

	@PostMapping("/admin/store/delete.do")
	public String del(Model model, HttpServletRequest request
		, StoreInput parameter) {

		boolean result = adminStoreService.del(parameter.getIdList());

		return "redirect:/admin/store/list.do";
	}
}
