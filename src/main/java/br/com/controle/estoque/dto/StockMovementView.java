package br.com.controle.estoque.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StockMovementView(
        Long id,
        String type,
        Long materialId,
        String materialCode,
        String materialName,
        Long warehouseId,
        String warehouseCode,
        String warehouseName,
        Long locationId,
        String locationCode,
        String locationName,
        BigDecimal qty,
        String reason,
        String note,
        LocalDateTime createdAt
) {}
