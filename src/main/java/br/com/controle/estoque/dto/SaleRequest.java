package br.com.controle.estoque.dto;

import java.math.BigDecimal;

public record SaleRequest(
        Long materialId,
        BigDecimal qty,
        Long warehouseId,
        Long locationId,
        String note
) {}
