package com.example.smartorder.repository;

import com.example.smartorder.entity.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByCategoryName(String categoryName);
	@Query(value = "select c from Category c "
		+ "where c.categoryName = :categoryName "
		+ "and c.id <> :categoryId")
	Optional<Category> existsByCategoryNameExceptId(String categoryName, Long categoryId);
}
