package com.reaksa.e_wingshop_api.repository;

import com.reaksa.e_wingshop_api.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByBranchIdAndProductId(Long branchId, Long productId);

    Page<Inventory> findByBranchId(Long branchId, Pageable pageable);

    // Low stock: quantity at or below threshold
    @Query("""
        SELECT i FROM Inventory i
        JOIN FETCH i.product p
        JOIN FETCH i.branch b
        WHERE i.quantity <= i.lowStockThreshold
        AND (:branchId IS NULL OR i.branch.id = :branchId)
        """)
    List<Inventory> findLowStock(@Param("branchId") Long branchId);

    // Items expiring within N days
    @Query("""
        SELECT i FROM Inventory i
        JOIN FETCH i.product p
        JOIN FETCH i.branch b
        WHERE i.expiryDate IS NOT NULL
        AND i.expiryDate BETWEEN :today AND :cutoff
        AND (:branchId IS NULL OR i.branch.id = :branchId)
        ORDER BY i.expiryDate ASC
        """)
    List<Inventory> findExpiringSoon(@Param("today") LocalDate today,
                                     @Param("cutoff") LocalDate cutoff,
                                     @Param("branchId") Long branchId);

    // Already expired
    @Query("""
        SELECT i FROM Inventory i
        JOIN FETCH i.product p
        JOIN FETCH i.branch b
        WHERE i.expiryDate IS NOT NULL
        AND i.expiryDate < :today
        """)
    List<Inventory> findExpired(@Param("today") LocalDate today);

    // Bulk quantity decrement (used during order fulfilment)
    @Modifying
    @Query("""
        UPDATE Inventory i SET i.quantity = i.quantity - :delta
        WHERE i.branch.id = :branchId AND i.product.id = :productId
        """)
    int decrementQuantity(@Param("branchId") Long branchId,
                          @Param("productId") Long productId,
                          @Param("delta") int delta);
}
