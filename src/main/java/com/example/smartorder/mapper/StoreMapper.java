package com.example.smartorder.mapper;

import com.example.smartorder.dto.StoreDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreMapper {
	List<StoreDto> selectNearList(double lat, double lnt);

}
