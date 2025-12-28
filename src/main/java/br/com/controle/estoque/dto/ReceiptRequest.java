package br.com.controle.estoque.dto;

public record ReceiptRequest(
        String nfNumber,
        Long invoiceItemId,
        Long materialId,
        Double qty,
        Long warehouseId,
        Long locationId,
        String note
) {}
