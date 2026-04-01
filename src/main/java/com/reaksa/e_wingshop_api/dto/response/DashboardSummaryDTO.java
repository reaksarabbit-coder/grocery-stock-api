package com.reaksa.e_wingshop_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDTO {

    private Double totalRevenue;
    private Long totalProducts;
    private Long totalOrders;
    private Long lowStockCount;
    private List<StockMovementPoint> stockMovement;
    private List<RevenueTrendPoint> revenueTrend;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockMovementPoint {
        private String date; // YYYY-MM-DD
        private Long stockIn;
        private Long stockOut;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RevenueTrendPoint {
        private String date; // YYYY-MM-DD
        private Double dailyRevenue;
    }
}
