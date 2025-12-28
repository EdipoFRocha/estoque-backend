package br.com.controle.estoque.dto;

public record WarehouseResponseDTO(
        Long id,
        String code,
        String name,
        Boolean active
) {}
