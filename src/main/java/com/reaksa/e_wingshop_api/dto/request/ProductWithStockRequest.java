package com.reaksa.e_wingshop_api.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductWithStockRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 150)
    private String name;

    private String description;

    @Size(max = 50)
    private String barcode;

    private String imageUrl;

    @NotNull(message = "Category is required")
    @Positive
    private Long categoryId;

    @NotNull(message = "Cost price is required")
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal costPrice;

    @NotNull(message = "Selling price is required")
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal sellingPrice;

    private Boolean isActive = true;

    @NotNull(message = "Branch is required for initial stock")
    @Positive
    private Long branchId;

    @NotNull(message = "Initial quantity is required")
    @Min(0)
    private Integer quantity;

    @Min(0)
    private Integer lowStockThreshold = 10;

    private LocalDate expiryDate;
}
