package com.example.smartorder.controller.admin;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.dto.AdminStoreDto;
import com.example.smartorder.model.AdminStore;
import com.example.smartorder.service.admin.AdminStoreService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/store")
public class AdminStoreController {

	private final AdminStoreService adminStoreService;
	@GetMapping("/list.do")
	public ApiResponse<Page<AdminStoreDto>> list(
		@PageableDefault(size = 10, sort = "regDt", direction = Direction.DESC) Pageable pageable) {

		Page<AdminStoreDto> storeList = adminStoreService.list(pageable);

		return ApiResponse.OK(storeList);
	}

	@PostMapping("/add.do")
	public ApiResponse add(@Valid @RequestBody AdminStore.Add parameter, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			return ApiResponse.fail(errors);
		}

		adminStoreService.add(parameter);
		return ApiResponse.OK();
	}

	@GetMapping("/update/{storeId}.do")
	public ApiResponse update(@NotNull @PathVariable Long storeId,
		@Valid @RequestBody AdminStore.Add parameter, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			return ApiResponse.fail(errors);
		}

		adminStoreService.update(storeId, parameter);
		return ApiResponse.OK();
	}

	@DeleteMapping("/delete/{storeId}.do")
	public ApiResponse del(@RequestBody AdminStore.Del parameter, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			return ApiResponse.fail(errors);
		}

		adminStoreService.del(parameter.getIdList());

		return ApiResponse.OK();
	}
}
