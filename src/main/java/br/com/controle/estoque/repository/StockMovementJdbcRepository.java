package br.com.controle.estoque.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StockMovementJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public StockMovementJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsMovementForMaterial(Long companyId, Long materialId) {
        Integer one = jdbcTemplate.queryForObject("""
            SELECT 1
            FROM mvp_clean.stock_movement
            WHERE company_id = ?
              AND material_id = ?
            LIMIT 1
        """, Integer.class, companyId, materialId);

        return one != null;
    }
}
