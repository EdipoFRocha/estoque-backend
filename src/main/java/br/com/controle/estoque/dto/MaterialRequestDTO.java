package br.com.controle.estoque.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MaterialRequestDTO(
        @NotBlank @Size(max = 60) String code,
        @NotBlank @Size(max = 160) String name,
        @NotBlank @Size(max = 10) String unit,
        @Size(max = 255) String description,

        @DecimalMin(value = "0.00", inclusive = true, message = "Preço deve ser >= 0.")
        BigDecimal salePrice
) {}
