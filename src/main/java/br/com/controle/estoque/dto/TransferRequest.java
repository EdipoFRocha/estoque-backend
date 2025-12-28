package br.com.controle.estoque.dto;

import java.math.BigDecimal;

public record TransferRequest(
        Long materialId,
        Long warehouseId,
        Long fromLocationId,
        Long toLocationId,
        BigDecimal qty,
        String note
) {}
