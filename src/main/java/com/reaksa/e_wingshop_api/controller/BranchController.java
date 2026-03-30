package com.reaksa.e_wingshop_api.controller;

import com.reaksa.e_wingshop_api.dto.request.BranchRequest;
import com.reaksa.e_wingshop_api.entity.Branch;
import com.reaksa.e_wingshop_api.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @GetMapping
    public ResponseEntity<List<Branch>> getAll() {
        return ResponseEntity.ok(branchService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Branch> getById(@PathVariable Long id) {
        return ResponseEntity.ok(branchService.findById(id));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<Branch>> nearby(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "10") double radiusKm) {
        return ResponseEntity.ok(branchService.findNearby(lat, lng, radiusKm));
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<Branch> create(@Valid @RequestBody BranchRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(branchService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<Branch> update(@PathVariable Long id,
                                         @Valid @RequestBody BranchRequest request) {
        return ResponseEntity.ok(branchService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        branchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
