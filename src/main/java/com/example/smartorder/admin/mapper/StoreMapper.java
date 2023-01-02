package com.example.smartorder.admin.mapper;

import com.example.smartorder.admin.dto.MenuDto;
import com.example.smartorder.admin.dto.StoreDto;
import com.example.smartorder.admin.model.MenuParam;
import com.example.smartorder.admin.model.StoreParam;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreMapper {
	List<StoreDto> selectList(StoreParam parameter);
	Long selectListCount(StoreParam parameter);

}
