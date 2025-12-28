package br.com.controle.estoque.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.math.BigDecimal;
import jakarta.persistence.Column;

@Entity
@Table(name = "material", schema = "mvp_clean")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String code;

    @Column(nullable = false, length = 160)
    private String name;

    @Column(nullable = false, length = 40)
    private String sku;

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getUnit() { return unit; }

    public void setUnit(String unit) { this.unit = unit; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Boolean getActive() { return active; }

    public void setActive(Boolean active) { this.active = active; }

    public Company getCompany() { return company; }

    public void setCompany(Company company) { this.company = company; }

    public OffsetDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }

    public BigDecimal getSalePrice() { return salePrice; }

    @Column(name = "sale_price", precision = 12, scale = 2)
    private BigDecimal salePrice;

    @Column(nullable = false, length = 10)
    private String unit;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    void prePersist() {
        createdAt = OffsetDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void preUpdate() { updatedAt = OffsetDateTime.now(); }
}
