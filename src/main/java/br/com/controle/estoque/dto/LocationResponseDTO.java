package br.com.controle.estoque.dto;

public record LocationResponseDTO(
        Long id,
        Long warehouseId,
        String warehouseName,
        String code,
        String name,
        Boolean active
) {}
