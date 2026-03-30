package com.reaksa.e_wingshop_api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class InventoryAlertResponse {

    private Long   inventoryId;
    private Long   productId;
    private String productName;
    private String productBarcode;
    private Long   branchId;
    private String branchName;
    private int    quantity;
    private int    lowStockThreshold;
    private LocalDate expiryDate;
    private int    daysUntilExpiry;   // negative = already expired
    private String alertType;          // LOW_STOCK | EXPIRING_SOON | EXPIRED
}
