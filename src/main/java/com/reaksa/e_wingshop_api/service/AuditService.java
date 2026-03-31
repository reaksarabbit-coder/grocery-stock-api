package com.reaksa.e_wingshop_api.service;

import com.reaksa.e_wingshop_api.entity.AuditLog;
import com.reaksa.e_wingshop_api.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    /**
     * Fire-and-forget — runs in a separate thread so it never blocks the
     * calling transaction. Uses REQUIRES_NEW so it commits independently
     * even if the parent transaction later rolls back (audit survives).
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(Long userId, String userEmail,
                    String action, String entityType, Long entityId, String detail) {
        auditLogRepository.save(AuditLog.builder()
                .userId(userId)
                .userEmail(userEmail)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .detail(detail)
                .build());
    }

    @Transactional(readOnly = true)
    public Page<AuditLog> findByEntity(String type, Long id, int page, int size) {
        return auditLogRepository.findByEntityTypeAndEntityId(
                type, id, PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    @Transactional(readOnly = true)
    public Page<AuditLog> findByUser(Long userId, int page, int size) {
        return auditLogRepository.findByUserId(
                userId, PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    @Transactional(readOnly = true)
    public List<AuditLog> recent() {
        return auditLogRepository.findTop50ByOrderByCreatedAtDesc();
    }
}
