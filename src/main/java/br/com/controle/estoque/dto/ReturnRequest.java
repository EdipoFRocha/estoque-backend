package br.com.controle.estoque.dto;

import java.math.BigDecimal;

public record ReturnRequest(
        Long materialId,
        Long warehouseId,
        Long locationId,
        BigDecimal qty,
        String note
) {}
