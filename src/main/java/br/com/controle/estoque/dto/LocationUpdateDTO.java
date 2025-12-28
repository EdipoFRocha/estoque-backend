package br.com.controle.estoque.dto;

import jakarta.validation.constraints.Size;

public record LocationUpdateDTO(
        Long warehouseId,

        @Size(max = 50, message = "Código deve ter no máximo 50 caracteres.")
        String code,

        @Size(max = 160, message = "Nome deve ter no máximo 160 caracteres.")
        String name,

        Boolean active
) {}
