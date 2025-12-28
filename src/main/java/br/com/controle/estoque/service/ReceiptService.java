package br.com.controle.estoque.service;

import br.com.controle.estoque.dto.ReceiptRequest;
import br.com.controle.estoque.security.CurrentUserService;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ReceiptService {

    private final JdbcTemplate jdbc;
    private final CurrentUserService currentUserService;
    private final StockBalanceService stockBalanceService;

    public ReceiptService(JdbcTemplate jdbc,
                          CurrentUserService currentUserService,
                          StockBalanceService stockBalanceService) {
        this.jdbc = jdbc;
        this.currentUserService = currentUserService;
        this.stockBalanceService = stockBalanceService;
    }

    @Transactional
    public Long registerReceipt(ReceiptRequest r) {
        if (r == null) throw new IllegalArgumentException("Body é obrigatório");
        if (r.materialId() == null) throw new IllegalArgumentException("materialId é obrigatório");
        if (r.locationId() == null) throw new IllegalArgumentException("locationId é obrigatório");
        if (r.warehouseId() == null) throw new IllegalArgumentException("warehouseId é obrigatório");

        BigDecimal qty = toBigDecimal(r.qty());
        if (qty.signum() <= 0) throw new IllegalArgumentException("qty deve ser > 0");

        Long companyId = currentUserService.getCompanyId();

        // Atualiza saldo (entrada)
        stockBalanceService.addQty(r.materialId(), r.locationId(), qty);

        // Registra movimento via função SQL
        String sql = """
            select mvp_clean.fn_register_receipt(
                ?::text,
                ?::bigint,
                ?::bigint,
                ?::numeric,
                ?::integer,
                ?::bigint,
                ?::text,
                ?::bigint
            )
            """;

        try {
            return jdbc.queryForObject(
                    sql,
                    Long.class,
                    r.nfNumber(),
                    r.invoiceItemId(),
                    r.materialId(),
                    qty,
                    r.warehouseId().intValue(),
                    r.locationId(),
                    r.note(),
                    companyId
            );
        } catch (DataAccessException e) {
            throw e;
        }
    }

    private static BigDecimal toBigDecimal(Object v) {
        if (v == null) return BigDecimal.ZERO;
        if (v instanceof BigDecimal bd) return bd;
        if (v instanceof Integer i) return BigDecimal.valueOf(i);
        if (v instanceof Long l) return BigDecimal.valueOf(l);
        if (v instanceof Double d) return BigDecimal.valueOf(d);
        if (v instanceof Float f) return BigDecimal.valueOf(f.doubleValue());
        if (v instanceof String s) return new BigDecimal(s);
        throw new IllegalArgumentException("Tipo inválido para qty: " + v.getClass().getName());
    }
}
