package com.reaksa.e_wingshop_api.repository;

import com.reaksa.e_wingshop_api.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    Page<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId, Pageable pageable);

    Page<AuditLog> findByUserId(Long userId, Pageable pageable);

    List<AuditLog> findTop50ByOrderByCreatedAtDesc();

    @Query("""
        SELECT a FROM AuditLog a
        WHERE LOWER(a.entityType) = 'inventory'
        AND a.createdAt BETWEEN :start AND :end
        ORDER BY a.createdAt ASC
        """)
    List<AuditLog> findInventoryLogsBetween(@Param("start") LocalDateTime start,
                                            @Param("end") LocalDateTime end);
}
