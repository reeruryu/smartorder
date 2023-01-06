package com.example.smartorder.mapper;

import com.example.smartorder.dto.StoreMenuDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreMenuMapper {
	List<StoreMenuDto> selectList(Long storeId, Long categoryId);

}
