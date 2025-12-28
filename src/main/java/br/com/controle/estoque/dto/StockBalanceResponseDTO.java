package br.com.controle.estoque.dto;

import java.math.BigDecimal;

public record StockBalanceResponseDTO(
        Long id,
        Long materialId,
        String materialCode,
        String materialName,
        Long locationId,
        String locationCode,
        String locationName,
        BigDecimal qty
) {}
