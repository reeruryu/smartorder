package com.example.smartorder.controller.admin;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.dto.MenuDto;
import com.example.smartorder.model.AdminMenu;
import com.example.smartorder.service.admin.AdminCategoryService;
import com.example.smartorder.service.admin.AdminMenuService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/menu")
public class AdminMenuController {

	private final AdminMenuService adminMenuService;

	@GetMapping("/list.do")
	public ApiResponse list(@RequestParam(required = false) Long categoryId,
		@PageableDefault(size = 10, sort = "sortValue", direction = Direction.ASC) Pageable pageable) {

		Page<MenuDto> menuList = adminMenuService.list(categoryId, pageable);

		return ApiResponse.OK(menuList);
	}

	@PostMapping("/add.do")
	public ApiResponse add(@RequestBody @Valid AdminMenu.Add parameter) {

		adminMenuService.add(parameter);

		return ApiResponse.OK();
	}

	@PutMapping("/update.do/{menuId}")
	public ApiResponse update(@PathVariable Long menuId,
		@RequestBody @Valid AdminMenu.Add parameter) {

		adminMenuService.update(menuId, parameter);

		return ApiResponse.OK();
	}

	@DeleteMapping("/delete.do")
	public ApiResponse del(@RequestBody @Valid AdminMenu.Del parameter) {

		adminMenuService.del(parameter.getIdList());

		return ApiResponse.OK();
	}
}
