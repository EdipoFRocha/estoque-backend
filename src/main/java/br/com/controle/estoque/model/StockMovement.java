package br.com.controle.estoque.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movement", schema = "mvp_clean")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ex.: ADJUSTMENT_IN, ADJUSTMENT_OUT, RECEIPT, SALE, etc.
    @Column(nullable = false)
    private String type;

    @Column(name = "lot_id")
    private Long lotId; // pode ser null em ajustes

    @Column(name = "material_id", nullable = false)
    private Long materialId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal qty;

    @Column(columnDefinition = "text")
    private String note;

    @Column
    private String reason; // CYCLE_COUNT, DAMAGE, etc.

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 🔹 NOVO: empresa dona do movimento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}
