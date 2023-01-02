package com.example.smartorder.admin.service;

import com.example.smartorder.admin.dto.CategoryDto;
import com.example.smartorder.admin.model.CategoryInput;
import java.util.List;

public interface AdminCategoryService {

	List<CategoryDto> list();

	boolean add(CategoryInput parameter);

	boolean update(CategoryInput parameter);

	boolean del(CategoryInput parameter);

//	List<CategoryDto> listByMenu();
}
