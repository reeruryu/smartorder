package com.example.smartorder.repository;

import com.example.smartorder.entity.StoreMenu;
import com.example.smartorder.type.SaleState;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreMenuRepository extends JpaRepository<StoreMenu, Long> {

	@Query(value = "delete from StoreMenu sm "
		+ "where sm.menu.id in :ids")
	void deleteAllByMenuIdIn(@Param("ids") List<Long> idList);

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

	// 스프링 배치에 사용
	Page<StoreMenu> findAllBySaleStateAndSoldOutDtBetween(
		SaleState saleState, LocalDateTime start, LocalDateTime end, Pageable pageable);

}