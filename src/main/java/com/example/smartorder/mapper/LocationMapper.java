package com.example.smartorder.mapper;

import com.example.smartorder.dto.LocationDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LocationMapper {
	List<LocationDto> selectNearList(Double lat, Double lnt);

}