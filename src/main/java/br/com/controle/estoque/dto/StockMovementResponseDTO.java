package br.com.controle.estoque.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StockMovementResponseDTO(
        Long id,
        String type,
        Long lotId,
        Long materialId,
        Long warehouseId,
        Long locationId,
        BigDecimal qty,
        String reason,
        String note,
        LocalDateTime createdAt
) {}
