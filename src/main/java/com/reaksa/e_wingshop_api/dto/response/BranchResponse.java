package com.reaksa.e_wingshop_api.dto.response;

import com.reaksa.e_wingshop_api.entity.Branch;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BranchResponse {
    private Long       id;
    private String     name;
    private String     address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String     phone;
    private LocalDateTime createdAt;

    public static BranchResponse from(Branch branch) {
        if (branch == null) return null;
        return BranchResponse.builder()
                .id(branch.getId())
                .name(branch.getName())
                .address(branch.getAddress())
                .latitude(branch.getLatitude())
                .longitude(branch.getLongitude())
                .phone(branch.getPhone())
                .createdAt(branch.getCreatedAt())
                .build();
    }
}
