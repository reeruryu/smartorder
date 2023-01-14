package com.example.smartorder.repository;

import com.example.smartorder.entity.Menu;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
	Optional<Menu> findByMenuName(String menuName);
	List<Menu> findAllByCategoryId(Long categoryId);
	Page<Menu> findAllByCategoryId(Long categoryId, Pageable pageable);
	List<Menu> findByIdIn(List<Long> idList);

}
