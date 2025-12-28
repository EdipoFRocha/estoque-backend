package br.com.controle.estoque.dto;

import java.math.BigDecimal;

public record TransferCodeRequest(
        Long materialId,
        Long warehouseId,
        String fromLocationCode,
        String toLocationCode,
        BigDecimal qty,
        String note
) {}
