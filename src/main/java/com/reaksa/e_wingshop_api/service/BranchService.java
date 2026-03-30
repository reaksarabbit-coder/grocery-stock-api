package com.reaksa.e_wingshop_api.service;

import com.reaksa.e_wingshop_api.dto.request.BranchRequest;
import com.reaksa.e_wingshop_api.entity.Branch;
import com.reaksa.e_wingshop_api.exception.ResourceNotFoundException;
import com.reaksa.e_wingshop_api.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;

    @Transactional(readOnly = true)
    public List<Branch> findAll() {
        return branchRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Branch findById(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch", id));
    }

    @Transactional(readOnly = true)
    public List<Branch> findNearby(double lat, double lng, double radiusKm) {
        return branchRepository.findNearby(lat, lng, radiusKm);
    }

    @Transactional
    public Branch create(BranchRequest request) {
        Branch branch = Branch.builder()
                .name(request.getName())
                .address(request.getAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .phone(request.getPhone())
                .build();
        return branchRepository.save(branch);
    }

    @Transactional
    public Branch update(Long id, BranchRequest request) {
        Branch branch = findById(id);
        branch.setName(request.getName());
        branch.setAddress(request.getAddress());
        branch.setLatitude(request.getLatitude());
        branch.setLongitude(request.getLongitude());
        branch.setPhone(request.getPhone());
        return branchRepository.save(branch);
    }

    @Transactional
    public void delete(Long id) {
        Branch branch = findById(id);
        branchRepository.delete(branch);
    }
}
