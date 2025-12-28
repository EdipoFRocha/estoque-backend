package br.com.controle.estoque.repository;

import br.com.controle.estoque.dto.StockMovementView;
import br.com.controle.estoque.dto.StockDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class MovementQueryRepository {

    private final JdbcTemplate jdbcTemplate;

    public MovementQueryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<StockMovementView> lastMovementsByTypes(List<String> types, int limit, Long companyId) {
        // fallback seguro: se não vier nada, usa os dois tipos de ajuste
        List<String> filtered = (types == null || types.isEmpty())
                ? List.of("ADJUSTMENT_IN", "ADJUSTMENT_OUT")
                : types.stream()
                .map(String::trim)
                .filter(t -> t.matches("[A-Z_]+"))
                .toList();

        List<String> safe = filtered.isEmpty()
                ? List.of("ADJUSTMENT_IN", "ADJUSTMENT_OUT")
                : filtered;

        final List<String> finalTypes = safe;

        String placeholders = String.join(
                ",",
                java.util.Collections.nCopies(finalTypes.size(), "?")
        );



        String sql = """
    SELECT sm.id,
           sm.type,
           sm.material_id,
           m.code  AS material_code,
           m.name  AS material_name,
           sm.warehouse_id,
           w.code  AS warehouse_code,
           w.name  AS warehouse_name,
           sm.location_id,
           l.code  AS location_code,
           l.name  AS location_name,
           sm.qty,
           sm.reason,
           sm.note,
           sm.created_at
    FROM mvp_clean.stock_movement sm
    LEFT JOIN mvp_clean.material  m ON m.id = sm.material_id AND m.company_id = sm.company_id
    LEFT JOIN mvp_clean.warehouse w ON w.id = sm.warehouse_id AND w.company_id = sm.company_id
    LEFT JOIN mvp_clean.location  l ON l.id = sm.location_id AND l.company_id = sm.company_id
    WHERE sm.company_id = ?
      AND sm.type IN (%s)
    ORDER BY sm.id DESC
    LIMIT ?
""".formatted(placeholders);

        return jdbcTemplate.query(
                sql,
                ps -> {
                    int i = 1;
                    // 1) companyId
                    ps.setLong(i++, companyId);

                    // 2..N) tipos
                    for (String t : safe) {
                        ps.setString(i++, t);
                    }

                    // último parâmetro: limit
                    ps.setInt(i, limit <= 0 ? 20 : limit);
                },
                (rs, i) ->
                        new StockMovementView(
                                rs.getLong("id"),
                                rs.getString("type"),
                                rs.getLong("material_id"),
                                rs.getString("material_code"),
                                rs.getString("material_name"),
                                rs.getLong("warehouse_id"),
                                rs.getString("warehouse_code"),
                                rs.getString("warehouse_name"),
                                rs.getLong("location_id"),
                                rs.getString("location_code"),
                                rs.getString("location_name"),
                                rs.getBigDecimal("qty"),
                                rs.getString("reason"),
                                rs.getString("note"),
                                rs.getTimestamp("created_at") != null
                                        ? rs.getTimestamp("created_at").toLocalDateTime()
                                        : null
                        )
        );
    }

    public List<StockDTO> stockByMaterial(Long materialId, Long companyId) {
        String sql = """
        SELECT sb.material_id,
               l.warehouse_id,
               sb.location_id,
               sb.qty AS on_hand
        FROM mvp_clean.stock_balance sb
        JOIN mvp_clean.location l ON l.id = sb.location_id AND l.company_id = sb.company_id
        WHERE sb.company_id = ?
          AND sb.material_id = ?
        ORDER BY l.warehouse_id, sb.location_id
    """;

        return jdbcTemplate.query(
                sql,
                ps -> {
                    ps.setLong(1, companyId);
                    ps.setLong(2, materialId);
                },
                (rs, i) -> new StockDTO(
                        rs.getLong("material_id"),
                        rs.getLong("warehouse_id"),
                        rs.getLong("location_id"),
                        rs.getBigDecimal("on_hand")
                )
        );
    }

    public Long findLocationId(Long warehouseId, String code) {
        return jdbcTemplate.queryForObject("""
            SELECT id FROM mvp_clean.location
            WHERE warehouse_id = ? AND code = ?
        """, Long.class, warehouseId, code);
    }
}
