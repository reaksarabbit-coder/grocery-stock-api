package com.reaksa.e_wingshop_api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AddStockRequest {

    @NotNull
    @Positive
    private Long branchId;

    @NotNull
    @Positive
    private Long productId;

    @NotNull(message = "Added quantity is required")
    @Min(value = 1, message = "Added quantity must be at least 1")
    private Integer quantity;

    @Min(0)
    private Integer lowStockThreshold;

    private LocalDate expiryDate;
}
