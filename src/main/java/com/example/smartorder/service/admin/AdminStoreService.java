package com.example.smartorder.service.admin;

import com.example.smartorder.dto.AdminStoreDto;
import com.example.smartorder.model.AdminStore;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminStoreService {

	/**
	 * 가게 리스트를 보여줍니다.
	 */
	Page<AdminStoreDto> list(Pageable pageable);

	/**
	 * 가게를 추가합니다.
	 */
	void add(AdminStore.Add parameter);

	/**
	 * 가게 정보를 수정합니다.
	 */
	void update(Long storeId, AdminStore.Add parameter);


	/**
	 * 선택된 가게를 삭제합니다.
	 */
	void del(List<Long> idList);

}
