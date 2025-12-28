package br.com.controle.estoque.dto;

import java.time.LocalDateTime;

public record CompanyResponse(
        Long id,
        String name,
        String tradeName,
        String document,
        Boolean active,
        LocalDateTime createdAt
) {}
