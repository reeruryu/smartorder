package com.example.smartorder.mapper;

import com.example.smartorder.dto.CartMenuDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartMenuMapper {

	List<CartMenuDto> selectList(Long cartId);

}
