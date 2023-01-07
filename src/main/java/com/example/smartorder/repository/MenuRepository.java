package com.example.smartorder.repository;

import com.example.smartorder.entity.Menu;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

//	List<Menu> findByCategoryId(long categoryId);

	Optional<Menu> findByMenuName(String menuName);

	List<Menu> findAllByCategoryId(Long categoryId);

	List<Menu> findByIdIn(List<Long> idList);

}