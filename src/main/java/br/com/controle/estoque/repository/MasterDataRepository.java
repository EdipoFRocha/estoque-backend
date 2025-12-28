package br.com.controle.estoque.repository;

import br.com.controle.estoque.dto.LocationDto;
import br.com.controle.estoque.dto.MaterialDto;
import br.com.controle.estoque.dto.WarehouseDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MasterDataRepository {

    private final JdbcTemplate jdbcTemplate;

    public MasterDataRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MaterialDto> findMaterials(Long companyId) {
        String sql = """
            SELECT id, sku, code, name, sale_price
            FROM mvp_clean.material
            WHERE company_id = ?
              AND active = true
            ORDER BY name
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new MaterialDto(
                rs.getLong("id"),
                rs.getString("sku"),
                rs.getString("code"),
                rs.getString("name"),
                rs.getBigDecimal("sale_price")
        ), companyId);
    }

    public List<WarehouseDto> findWarehouses(Long companyId) {
        return jdbcTemplate.query("""
                SELECT id, code, name
                FROM mvp_clean.warehouse
                WHERE company_id = ?
                ORDER BY name
                """,
                ps -> ps.setLong(1, companyId),
                (rs, i) -> new WarehouseDto(
                        rs.getLong("id"),
                        rs.getString("code"),
                        rs.getString("name")
                )
        );
    }

    public List<LocationDto> findLocationsByWarehouse(Long companyId, Long warehouseId) {
        return jdbcTemplate.query("""
                SELECT id, warehouse_id, code, name
                FROM mvp_clean.location
                WHERE company_id = ?
                  AND warehouse_id = ?
                ORDER BY name
                """,
                ps -> {
                    ps.setLong(1, companyId);
                    ps.setLong(2, warehouseId);
                },
                (rs, i) -> new LocationDto(
                        rs.getLong("id"),
                        rs.getLong("warehouse_id"),
                        rs.getString("code"),
                        rs.getString("name")
                )
        );
    }
}
