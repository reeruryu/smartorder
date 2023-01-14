package com.example.smartorder.controller.admin;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.dto.CategoryDto;
import com.example.smartorder.model.AdminCategory;
import com.example.smartorder.service.admin.AdminCategoryService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/admin/category")
public class AdminCategoryController {

	private final AdminCategoryService adminCategoryService;

	@GetMapping("/list.do")
	public ApiResponse<List<CategoryDto>> list() {

		List<CategoryDto> list = adminCategoryService.list();

		return ApiResponse.OK(list);
	}

	@PostMapping("/add.do")
	public ApiResponse add(@Valid @RequestBody AdminCategory.Add parameter,
		BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			return ApiResponse.fail(errors);
		}

		adminCategoryService.add(parameter);

		return ApiResponse.OK();
	}

	@PutMapping("/update/{id}.do")
	public ApiResponse update(@NotNull @PathVariable Long id,
		@NotNull @RequestParam String categoryName, @RequestParam int sortValue,
		BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			return ApiResponse.fail(errors);
		}

		adminCategoryService.update(id, categoryName, sortValue);

		return ApiResponse.OK();
	}

	@DeleteMapping("/delete/{id}.do")
	public ApiResponse del(@NotNull @PathVariable Long id,
		BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			return ApiResponse.fail(errors);
		}

		adminCategoryService.del(id);

		return ApiResponse.OK();
	}

}
