package com.example.smartorder.category.repository;

import com.example.smartorder.admin.model.CategoryInput;
import com.example.smartorder.category.entity.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByCategoryName(String categoryName);

}
