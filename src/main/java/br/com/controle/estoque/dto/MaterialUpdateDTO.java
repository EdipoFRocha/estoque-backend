package br.com.controle.estoque.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MaterialUpdateDTO(

        @Size(max = 50, message = "Código deve ter no máximo 50 caracteres.")
        String code,

        @Size(max = 160, message = "Nome deve ter no máximo 160 caracteres.")
        String name,

        @Size(max = 10, message = "Unidade deve ter no máximo 10 caracteres.")
        String unit,

        @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres.")
        String description,

        Boolean active,

        // ✅ novo (opcional)
        @DecimalMin(value = "0.00", inclusive = true, message = "Preço deve ser >= 0.")
        BigDecimal salePrice
) {}
