package br.com.controle.estoque.dto;

import java.math.BigDecimal;

public record StockDTO(Long materialId, Long warehouseId, Long locationId, BigDecimal onHand) {}
