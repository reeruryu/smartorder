package com.example.smartorder.service.admin;

import com.example.smartorder.dto.MenuDto;
import com.example.smartorder.model.AdminMenu.Add;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminMenuService {

	/**
	 * 카테고리 별 메뉴를 보여줍니다.
	 */
	Page<MenuDto> list(Long categoryId, Pageable pageable);

	/**
	 * 메뉴를 추가합니다.
	 */
	void add(Long menuId, Add parameter);

	/**
	 * 메뉴를 변경합니다.
	 */
	void update(Long menuId, Add parameter);

	/**
	 * 메뉴를 삭제합니다.
	 */
	void del(List<Long> idList);
}
