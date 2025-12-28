package br.com.controle.estoque.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record StockReceiptRequestDTO(
        @NotNull Long materialId,
        @NotNull Long locationId,
        @NotNull @Positive BigDecimal quantity,
        String documentRef,
        String notes
) {}
