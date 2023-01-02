package com.example.smartorder.admin.service;

import com.example.smartorder.admin.dto.MemberDto;
import com.example.smartorder.admin.dto.MenuDto;
import com.example.smartorder.admin.model.MenuInput;
import com.example.smartorder.admin.model.MenuParam;
import java.util.List;
import java.util.Optional;

public interface AdminMenuService {

	List<MenuDto> list(MenuParam parameter);

	MenuDto getById(long id);

	boolean add(MenuInput parameter);

	boolean set(MenuInput parameter);

	boolean del(String idList);
}
