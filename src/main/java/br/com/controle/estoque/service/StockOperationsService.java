package br.com.controle.estoque.service;

import br.com.controle.estoque.dto.ReturnRequest;
import br.com.controle.estoque.dto.TransferRequest;
import br.com.controle.estoque.dto.TransferCodeRequest;
import br.com.controle.estoque.repository.MovementQueryRepository;
import br.com.controle.estoque.security.CurrentUserService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;

@Service
public class StockOperationsService {

    private final JdbcTemplate jdbc;
    private final MovementQueryRepository repo;
    private final CurrentUserService currentUserService;

    public StockOperationsService(JdbcTemplate jdbc,
                                  MovementQueryRepository repo,
                                  CurrentUserService currentUserService) {
        this.jdbc = jdbc;
        this.repo = repo;
        this.currentUserService = currentUserService;
    }

    public Long registerReturn(ReturnRequest req) {
        System.out.println("DEBUG RETURN REQUEST: " + req);

        validateNotNull(req.materialId(), "materialId");
        validateNotNull(req.warehouseId(), "warehouseId");
        validateNotNull(req.locationId(), "locationId");

        validateNotNull(req.qty(), "qty");
        if (req.qty().compareTo(BigDecimal.ZERO) == 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "qty deve ser diferente de 0"
            );
        }

        Long companyId = currentUserService.getCompanyId();

        try {
            return jdbc.queryForObject(
                    "SELECT mvp_clean.fn_register_return(?, ?, ?, ?, ?, ?)",
                    Long.class,
                    req.materialId(),
                    req.qty(),
                    req.warehouseId(),
                    req.locationId(),
                    req.note(),
                    companyId
            );
        } catch (DataAccessException ex) {
            String msg = ex.getMostSpecificCause() != null
                    ? ex.getMostSpecificCause().getMessage()
                    : ex.getMessage();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, msg, ex);
        }
    }


    // TRANSFERÊNCIA
    public Long transfer(TransferRequest req) {
        System.out.println("DEBUG TRANSFER REQUEST: " + req);

        validateNotNull(req.materialId(), "materialId");
        validateNotNull(req.warehouseId(), "warehouseId");
        validateNotNull(req.fromLocationId(), "fromLocationId");
        validateNotNull(req.toLocationId(), "toLocationId");
        validatePositive(req.qty(), "qty");

        Long companyId = currentUserService.getCompanyId();

        try {
            return jdbc.queryForObject(
                    "SELECT mvp_clean.fn_register_transfer(?, ?, ?, ?, ?, ?, ?)",
                    Long.class,
                    req.materialId(),
                    req.qty(),
                    req.warehouseId(),
                    req.fromLocationId(),
                    req.toLocationId(),
                    req.note(),
                    companyId   // 🔹 novo parâmetro
            );
        } catch (DataAccessException ex) {
            String msg = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
            System.err.println("TRANSFER DB ERROR: " + msg);
            if (msg != null && msg.toLowerCase().contains("insufficient stock")) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, msg, ex);
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, msg, ex);
        }
    }

    // TRANSFERÊNCIA POR CÓDIGO
    public Long transferByCode(TransferCodeRequest req) {
        System.out.println("DEBUG TRANSFER BY CODE: " + req);

        validateNotNull(req.materialId(), "materialId");
        validateNotNull(req.warehouseId(), "warehouseId");
        validateNotNull(req.fromLocationCode(), "fromLocationCode");
        validateNotNull(req.toLocationCode(), "toLocationCode");
        validatePositive(req.qty(), "qty");

        Long fromId = repo.findLocationId(req.warehouseId(), req.fromLocationCode());
        Long toId   = repo.findLocationId(req.warehouseId(), req.toLocationCode());

        if (fromId.equals(toId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fromLocationCode e toLocationCode devem ser diferentes");
        }

        Long companyId = currentUserService.getCompanyId();

        try {
            return jdbc.queryForObject(
                    "SELECT mvp_clean.fn_register_transfer(?, ?, ?, ?, ?, ?, ?)",
                    Long.class,
                    req.materialId(),
                    req.qty(),
                    req.warehouseId(),
                    fromId,
                    toId,
                    req.note(),
                    companyId   // 🔹 novo parâmetro
            );
        } catch (DataAccessException ex) {
            String msg = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
            System.err.println("TRANSFER BY CODE DB ERROR: " + msg);
            if (msg != null && msg.toLowerCase().contains("insufficient stock")) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, msg, ex);
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, msg, ex);
        }
    }

    // VALIDAÇÕES
    private static void validateNotNull(Object v, String field) {
        System.out.println("VALIDATING NOT NULL: " + field + " = " + v);
        if (v == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, field + " é obrigatório");
    }

    private static void validatePositive(java.math.BigDecimal v, String field) {
        System.out.println("VALIDATING POSITIVE: " + field + " = " + v);
        if (v == null || v.signum() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, field + " deve ser > 0");
        }
    }
}
