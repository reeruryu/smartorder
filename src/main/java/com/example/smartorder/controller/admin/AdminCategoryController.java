package com.example.smartorder.controller.admin;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.dto.CategoryDto;
import com.example.smartorder.model.AdminCategory;
import com.example.smartorder.service.admin.AdminCategoryService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
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
	public ApiResponse list() {

		List<CategoryDto> list = adminCategoryService.list();

		return ApiResponse.OK(list);
	}

	@PostMapping("/add.do")
	public ApiResponse add(@RequestBody @Valid AdminCategory.Add parameter) {

		adminCategoryService.add(parameter);

		return ApiResponse.OK();
	}

	@PutMapping("/update/{id}.do")
	public ApiResponse update(@PathVariable Long id,
		@RequestParam String categoryName,
		@RequestParam(required = false, defaultValue = "0") int sortValue) {

		adminCategoryService.update(id, categoryName, sortValue);

		return ApiResponse.OK();
	}

	@DeleteMapping("/delete/{id}.do")
	public ApiResponse del(@PathVariable Long id) {

		adminCategoryService.del(id);

		return ApiResponse.OK();
	}

}
