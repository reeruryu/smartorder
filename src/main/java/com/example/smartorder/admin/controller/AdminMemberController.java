package com.example.smartorder.admin.controller;

import com.example.smartorder.admin.dto.MemberDto;
import com.example.smartorder.admin.model.MemberParam;
import com.example.smartorder.admin.service.AdminMemberService;
import com.example.smartorder.admin.model.MemberInput;
import com.example.smartorder.member.type.UserRole;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class AdminMemberController extends BaseController {

	private final AdminMemberService adminMemberService;

	@GetMapping("/admin/member/list.do")
	public String list(Model model, MemberParam parameter) {

		parameter.init();
		List<MemberDto> members = adminMemberService.list(parameter);

		long totalCount = 0;
		if (members != null && members.size() > 0) {
			totalCount = members.get(0).getTotalCount();
		}
		String queryString = parameter.getQueryString();
		String pagerHtml = getPagerHtml(totalCount, parameter.getPageSize(), parameter.getPageIndex(), queryString);

		model.addAttribute("list", members);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("pager", pagerHtml);

		return "admin/member/list";
	}

	@GetMapping("/admin/member/detail.do")
	public String detail(Model model, MemberParam parameter) {

		MemberDto member = adminMemberService.detail(parameter.getUserId());
		model.addAttribute("member", member);
		model.addAttribute("userRole", UserRole.values());

		return "admin/member/detail";
	}

	@PostMapping("/admin/member/role.do")
	public String status(Model model, MemberInput parameter) {
		boolean result = adminMemberService.updateRole(parameter.getUserId(), parameter.getUserRole());

		return "redirect:/admin/member/detail.do?userId=" + parameter.getUserId();
	}

}
