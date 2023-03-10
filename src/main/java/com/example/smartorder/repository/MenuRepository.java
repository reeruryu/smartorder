package com.example.smartorder.repository;

import com.example.smartorder.entity.Menu;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
	boolean existsByMenuName(String menuName);
	List<Menu> findAllByCategoryId(Long categoryId);
	@Query(value = "delete from Menu m "
		+ "where m.category.id = :id")
	void deleteAllByCategoryId(@Param("id") Long categoryId);

	Page<Menu> findAllByCategoryId(Long categoryId, Pageable pageable);
	List<Menu> findByIdIn(List<Long> idList);
	@Query(value = "select m from Menu m "
		+ "where m.menuName = :menuName "
		+ "and m.id <> :menuId")
	Optional<Menu> existsByMenuNameExceptId(String menuName, Long menuId);

	@Query(value = "delete from Menu m "
		+ "where m.id in :idList")
	void deleteAllByMenuIdIn(List<Long> idList);

}
