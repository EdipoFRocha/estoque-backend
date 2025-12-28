package br.com.controle.estoque.dto;

import java.time.LocalDateTime;

public class CompanyMeResponse {

    private Long id;
    private String name;
    private String document;
    private String tradeName;
    private String logoUrl; // opcional por enquanto
    private LocalDateTime createdAt;

    // GETTERS & SETTERS
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDocument() { return document; }

    public void setDocument(String document) { this.document = document; }

    public String getLogoUrl() { return logoUrl;}

    public void setLogoUrl(String logoUrl) {this.logoUrl = logoUrl;}

    public LocalDateTime getCreatedAt() { return createdAt;}

    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt; }

    public String getTradeName() { return tradeName; }

    public void setTradeName(String tradeName) { this.tradeName = tradeName; }
}
