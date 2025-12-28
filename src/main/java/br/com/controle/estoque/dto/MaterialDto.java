package br.com.controle.estoque.dto;

import java.math.BigDecimal;

public record MaterialDto(
        Long id,
        String sku,
        String code,
        String name,
        BigDecimal salePrice
) {}
