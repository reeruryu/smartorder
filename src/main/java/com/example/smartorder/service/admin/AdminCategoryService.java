package com.example.smartorder.service.admin;

import com.example.smartorder.dto.CategoryDto;
import com.example.smartorder.model.AdminCategory.Add;
import java.util.List;

public interface AdminCategoryService {

	/**
	 * 카테고리 리스트를 보여줍니다.
	 */
	List<CategoryDto> list();

	/**
	 * 카테고리를 추가합니다.
	 */

	void add(Add parameter);


	/**
	 * 카테고리 이름이나 정렬 순서를 변경합니다.
	 */
	void update(Long id, String categoryName, int sortValue);

	/**
	 * 카테고리를 삭제합니다.
	 */
	void del(Long id);

}
