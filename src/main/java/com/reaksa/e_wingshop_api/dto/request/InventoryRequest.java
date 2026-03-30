package com.reaksa.e_wingshop_api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InventoryRequest {

    @NotNull @Positive
    private Long branchId;

    @NotNull @Positive
    private Long productId;

    @NotNull @Min(0)
    private Integer quantity;

    @Min(0)
    private Integer lowStockThreshold = 10;

    private LocalDate expiryDate;
}
