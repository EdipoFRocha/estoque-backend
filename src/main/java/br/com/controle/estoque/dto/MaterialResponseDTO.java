package br.com.controle.estoque.dto;

import java.math.BigDecimal;

public record MaterialResponseDTO(
        Long id,
        String sku,
        String code,
        String name,
        String unit,
        String description,
        Boolean active,
        BigDecimal salePrice
) {}
