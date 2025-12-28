package br.com.controle.estoque.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_balance", schema = "mvp_clean")
public class StockBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id")
    private Material material;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(nullable = false, precision = 18, scale = 3)
    private BigDecimal qty = BigDecimal.ZERO;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        updatedAt = LocalDateTime.now();
        if (qty == null) qty = BigDecimal.ZERO;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
        if (qty == null) qty = BigDecimal.ZERO;
    }

    public Long getId() { return id; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }

    public Material getMaterial() { return material; }
    public void setMaterial(Material material) { this.material = material; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public BigDecimal getQty() { return qty; }
    public void setQty(BigDecimal qty) { this.qty = qty; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
