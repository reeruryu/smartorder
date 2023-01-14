package com.example.smartorder.repository;

import com.example.smartorder.entity.Menu;
import com.example.smartorder.entity.StoreMenu;
import com.example.smartorder.type.SaleState;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreMenuRepository extends JpaRepository<StoreMenu, Long> {

	void deleteAllByMenu(Menu menu);
	List<StoreMenu> findAllBySaleState(SaleState saleState);

	@Query(value = "select sm from StoreMenu sm "
		+ "where sm.store.id = :storeId "
		+ "and sm.hiddenYn = false")
	Page<StoreMenu> findByStoreIdExceptHiddenYnTrue(@Param("storeId")Long storeId, Pageable pageable);

	@Query(value = "select sm from StoreMenu sm "
		+ "where sm.menu.category.id = :categoryId "
		+ "and sm.store.id = :storeId "
		+ "and sm.hiddenYn = false")
	Page<StoreMenu> findByCategoryIdAndStoreIdExceptHiddenYnTrue(
		@Param("categoryId") Long categoryId, @Param("storeId") Long storeId, Pageable pageable);
}