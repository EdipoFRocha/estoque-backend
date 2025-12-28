package br.com.controle.estoque.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public record StockAdjustmentRequest(
        @NotNull Long materialId,
        @NotNull Long warehouseId,
        @NotNull Long locationId,

        @DecimalMin(value = "0.0000001", inclusive = true)
        BigDecimal qty,

        @NotNull String direction,
        @NotNull String reason,
        String note
) {}
