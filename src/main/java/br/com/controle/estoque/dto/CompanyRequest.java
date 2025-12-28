package br.com.controle.estoque.dto;

import jakarta.validation.constraints.NotBlank;

public record CompanyRequest(
        @NotBlank String name,
        String tradeName,
        String document
) {}
