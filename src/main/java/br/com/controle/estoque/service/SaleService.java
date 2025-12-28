package br.com.controle.estoque.service;

import br.com.controle.estoque.dto.SaleRequest;
import br.com.controle.estoque.model.Company;
import br.com.controle.estoque.model.StockMovement;
import br.com.controle.estoque.repository.StockMovementRepository;
import br.com.controle.estoque.security.CurrentUserService;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class SaleService {

    private final JdbcTemplate jdbc;
    private final CurrentUserService currentUserService;
    private final StockBalanceService stockBalanceService;
    private final StockMovementRepository movementRepository;

    public SaleService(JdbcTemplate jdbc,
                       CurrentUserService currentUserService,
                       StockBalanceService stockBalanceService,
                       StockMovementRepository movementRepository) {
        this.jdbc = jdbc;
        this.currentUserService = currentUserService;
        this.stockBalanceService = stockBalanceService;
        this.movementRepository = movementRepository;
    }

    @Transactional
    public Map<String, Object> registerSale(SaleRequest r) {

        // validações
        if (r == null) throw new IllegalArgumentException("Body é obrigatório");
        if (r.materialId() == null) throw new IllegalArgumentException("materialId é obrigatório");
        if (r.qty() == null || r.qty().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Quantidade deve ser > 0");
        if (r.warehouseId() == null) throw new IllegalArgumentException("warehouseId é obrigatório");
        if (r.locationId() == null) throw new IllegalArgumentException("locationId é obrigatório");

        Long companyId = currentUserService.getCompanyId();
        BigDecimal qty = r.qty();

        // baixa saldo primeiro (se não tiver saldo, aborta)
        stockBalanceService.removeQty(r.materialId(), r.locationId(), qty);

        Long saleId = 0L;
        String warning = null;
        String dbError = null;

        // tenta FIFO
        try {
            String sql = """
                select coalesce(
                  mvp_clean.fn_register_sale_fifo(
                    ?::bigint,
                    ?::numeric,
                    ?::bigint,
                    ?::bigint,
                    ?::text,
                    ?::bigint
                  ), 0
                )::bigint
                """;

            Long result = jdbc.queryForObject(
                    sql,
                    Long.class,
                    r.materialId(),
                    qty,
                    r.warehouseId(),
                    r.locationId(),
                    r.note(),
                    companyId
            );

            saleId = (result == null ? 0L : result);

        } catch (DataAccessException e) {
            // fallback: se a função SQL falhar, registra o movimento via JPA (também NEGATIVO)
            warning = "Função SQL FIFO indisponível; venda registrada via movimento (sem FIFO).";
            dbError = e.getMessage();

            saveSaleMovement(companyId, r, qty.negate()); // ✅ NEGATIVO
        }

        // resposta
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("ok", true);
        resp.put("saleId", saleId);

        if (warning != null) {
            resp.put("warning", warning);
            resp.put("dbError", dbError);
        }

        return resp;
    }

    private void saveSaleMovement(Long companyId, SaleRequest r, BigDecimal signedQty) {
        Company c = new Company();
        c.setId(companyId);

        StockMovement mv = new StockMovement();
        mv.setType("SALE");
        mv.setLotId(null);
        mv.setMaterialId(r.materialId());
        mv.setWarehouseId(r.warehouseId());
        mv.setLocationId(r.locationId());
        mv.setQty(signedQty); // ✅ negativo
        mv.setNote(r.note());
        mv.setReason("SALE");
        mv.setCreatedAt(LocalDateTime.now());
        mv.setCompany(c);

        movementRepository.save(mv);
    }
}
