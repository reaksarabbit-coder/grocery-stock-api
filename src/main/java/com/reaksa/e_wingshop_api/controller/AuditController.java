package com.reaksa.e_wingshop_api.controller;

import com.reaksa.e_wingshop_api.entity.AuditLog;
import com.reaksa.e_wingshop_api.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('MANAGER','SUPERADMIN')")
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/recent")
    public ResponseEntity<List<AuditLog>> recent() {
        return ResponseEntity.ok(auditService.recent());
    }

    @GetMapping("/entity/{type}/{id}")
    public ResponseEntity<Page<AuditLog>> byEntity(
            @PathVariable String type,
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(auditService.findByEntity(type, id, page, size));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<AuditLog>> byUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(auditService.findByUser(userId, page, size));
    }
}
