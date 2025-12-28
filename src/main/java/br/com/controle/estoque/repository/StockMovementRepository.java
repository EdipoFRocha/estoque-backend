package br.com.controle.estoque.repository;

import br.com.controle.estoque.model.StockMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    // (se você ainda usa em algum lugar)
    List<StockMovement> findByCompany_Id(Long companyId);

    // ✅ Para o endpoint novo /api/stock/movements (paginado e filtrável)
    Page<StockMovement> findByCompany_IdOrderByCreatedAtDesc(Long companyId, Pageable pageable);

    Page<StockMovement> findByCompany_IdAndMaterialIdOrderByCreatedAtDesc(Long companyId, Long materialId, Pageable pageable);

    Page<StockMovement> findByCompany_IdAndWarehouseIdOrderByCreatedAtDesc(Long companyId, Long warehouseId, Pageable pageable);

    Page<StockMovement> findByCompany_IdAndLocationIdOrderByCreatedAtDesc(Long companyId, Long locationId, Pageable pageable);

    Page<StockMovement> findByCompany_IdAndTypeOrderByCreatedAtDesc(Long companyId, String type, Pageable pageable);

    Page<StockMovement> findByCompany_IdAndCreatedAtBetweenOrderByCreatedAtDesc(
            Long companyId, LocalDateTime start, LocalDateTime end, Pageable pageable
    );

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO mvp_clean.stock_movement
        (type, material_id, warehouse_id, location_id, qty, note, reason, created_at, company_id)
        VALUES (
            CASE WHEN :qty > 0 THEN 'ADJUSTMENT_IN' ELSE 'ADJUSTMENT_OUT' END,
            :materialId,
            :warehouseId,
            :locationId,
            :qty,
            :note,
            :reason,
            NOW(),
            :companyId
        )
        """, nativeQuery = true)
    int insertAdjustment(
            @Param("materialId") Long materialId,
            @Param("warehouseId") Long warehouseId,
            @Param("locationId") Long locationId,
            @Param("qty") java.math.BigDecimal qty,
            @Param("reason") String reason,
            @Param("note") String note,
            @Param("companyId") Long companyId
    );
}
