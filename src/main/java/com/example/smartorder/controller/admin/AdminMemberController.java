package com.example.smartorder.controller.admin;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.dto.MemberDto;
import com.example.smartorder.model.AdminMember;
import com.example.smartorder.service.admin.AdminMemberService;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/member")
public class AdminMemberController {

	private final AdminMemberService adminMemberService;

	@GetMapping("/list.do")
	public ApiResponse<Page<MemberDto>> list(@RequestParam String userId,
		@PageableDefault(size = 10, sort = "regDt", direction = Direction.DESC) Pageable pageable) {

		Page<MemberDto> list = adminMemberService.list(userId, pageable);

		return ApiResponse.OK(list);
	}
	@PutMapping("/update-role/{id}.do")
	public ApiResponse status(@NotNull @PathVariable Long id,
		AdminMember.UpdateRole parameter, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			return ApiResponse.fail(errors);
		}

		adminMemberService.updateRole(id, parameter.getUserRole());

		return ApiResponse.OK();
	}

}
