package br.com.controle.estoque.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LocationRequestDTO(
        @NotNull(message = "warehouseId é obrigatório.")
        Long warehouseId,

        @NotBlank(message = "Código é obrigatório.")
        @Size(max = 50, message = "Código deve ter no máximo 50 caracteres.")
        String code,

        @NotBlank(message = "Nome é obrigatório.")
        @Size(max = 160, message = "Nome deve ter no máximo 160 caracteres.")
        String name
) {}
