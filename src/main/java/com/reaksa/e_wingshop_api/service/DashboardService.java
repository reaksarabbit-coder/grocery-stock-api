package com.reaksa.e_wingshop_api.service;

import com.reaksa.e_wingshop_api.dto.response.DashboardSummaryDTO;
import com.reaksa.e_wingshop_api.entity.AuditLog;
import com.reaksa.e_wingshop_api.repository.AuditLogRepository;
import com.reaksa.e_wingshop_api.repository.InventoryRepository;
import com.reaksa.e_wingshop_api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final int LOW_STOCK_THRESHOLD = 10;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final Pattern SIGNED_NUMBER_PATTERN = Pattern.compile("(-?\\d+)");

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final AuditLogRepository auditLogRepository;

    @Transactional(readOnly = true)
    public DashboardSummaryDTO getDashboardSummary(LocalDate from, LocalDate to, Long branchId) {
        LocalDate end = to == null ? LocalDate.now() : to;
        LocalDate start = from == null ? end.minusDays(29) : from;


        return DashboardSummaryDTO.builder()
                .totalProducts(productRepository.count())
                .lowStockCount(inventoryRepository.countByQuantityBelow(LOW_STOCK_THRESHOLD, branchId))
                .stockMovement(buildStockMovement(start, end))
                .build();
    }

    private List<DashboardSummaryDTO.RevenueTrendPoint> buildRevenueTrend(List<Map<String, Object>> rows) {
        List<DashboardSummaryDTO.RevenueTrendPoint> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            result.add(DashboardSummaryDTO.RevenueTrendPoint.builder()
                    .date(formatDate(row.get("date")))
                    .dailyRevenue(toDouble(row.get("revenue")))
                    .build());
        }
        return result;
    }

    private List<DashboardSummaryDTO.StockMovementPoint> buildStockMovement(LocalDate from, LocalDate to) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(LocalTime.MAX);
        List<AuditLog> logs = auditLogRepository.findInventoryLogsBetween(start, end);

        Map<String, long[]> byDay = new LinkedHashMap<>();
        for (AuditLog log : logs) {
            String dateKey = log.getCreatedAt().toLocalDate().format(DATE_FORMATTER);
            long[] totals = byDay.computeIfAbsent(dateKey, ignored -> new long[]{0L, 0L});

            Long delta = extractSignedDelta(log.getDetail());
            if (delta == null) {
                continue;
            }
            if (delta > 0) {
                totals[0] += delta;
            } else if (delta < 0) {
                totals[1] += Math.abs(delta);
            }
        }

        List<DashboardSummaryDTO.StockMovementPoint> points = new ArrayList<>();
        byDay.forEach((date, totals) -> points.add(DashboardSummaryDTO.StockMovementPoint.builder()
                .date(date)
                .stockIn(totals[0])
                .stockOut(totals[1])
                .build()));
        return points;
    }

    private String formatDate(Object value) {
        if (value == null) return null;
        if (value instanceof LocalDate date) return date.format(DATE_FORMATTER);
        if (value instanceof LocalDateTime dateTime) return dateTime.toLocalDate().format(DATE_FORMATTER);

        String raw = value.toString();
        try {
            return LocalDate.parse(raw).format(DATE_FORMATTER);
        } catch (DateTimeParseException ignored) {
        }
        try {
            return LocalDateTime.parse(raw).toLocalDate().format(DATE_FORMATTER);
        } catch (DateTimeParseException ignored) {
        }
        int splitAt = raw.indexOf('T');
        if (splitAt > 0) {
            return raw.substring(0, splitAt);
        }
        return raw.length() >= 10 ? raw.substring(0, 10) : raw;
    }

    private Long extractSignedDelta(String detail) {
        if (detail == null || detail.isBlank()) return null;

        Matcher matcher = SIGNED_NUMBER_PATTERN.matcher(detail);
        Long selected = null;
        while (matcher.find()) {
            long candidate = Long.parseLong(matcher.group(1));
            if (candidate < 0) {
                return candidate;
            }
            if (selected == null) {
                selected = candidate;
            }
        }
        return selected;
    }

    private Double toDouble(Object value) {
        if (value == null) return 0.0d;
        if (value instanceof BigDecimal decimal) return decimal.doubleValue();
        if (value instanceof Number number) return number.doubleValue();
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException ex) {
            return 0.0d;
        }
    }

    private Long toLong(Object value) {
        if (value == null) return 0L;
        if (value instanceof Number number) return number.longValue();
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException ex) {
            return 0L;
        }
    }
}
