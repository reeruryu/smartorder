package com.example.smartorder.admin.service;

import com.example.smartorder.admin.dto.StoreDto;
import com.example.smartorder.admin.model.StoreInput;
import com.example.smartorder.admin.model.StoreParam;
import java.util.List;

public interface AdminStoreService {

	List<StoreDto> list(StoreParam parameter);

	StoreDto getById(long id);

	boolean add(StoreInput parameter);

	boolean set(StoreInput parameter);

	boolean del(String idList);

}
