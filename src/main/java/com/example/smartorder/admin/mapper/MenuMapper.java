package com.example.smartorder.admin.mapper;

import com.example.smartorder.admin.dto.MenuDto;
import com.example.smartorder.admin.model.MenuParam;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuMapper {

	List<MenuDto> selectList(MenuParam parameter);
	Long selectListCount(MenuParam parameter);

}
