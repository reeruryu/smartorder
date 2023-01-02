package com.example.smartorder.admin.mapper;

import com.example.smartorder.admin.dto.CategoryDto;
import com.example.smartorder.admin.dto.MemberDto;
import com.example.smartorder.admin.model.MemberParam;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

	List<CategoryDto> selectList();

}
