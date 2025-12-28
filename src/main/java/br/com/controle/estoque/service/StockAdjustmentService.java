package br.com.controle.estoque.service;

import br.com.controle.estoque.dto.StockAdjustmentRequest;
import br.com.controle.estoque.repository.StockMovementRepository;
import br.com.controle.estoque.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class StockAdjustmentService {

    private final StockMovementRepository stockMovementRepository;
    private final StockBalanceService stockBalanceService;
    private final CurrentUserService currentUserService;

    public StockAdjustmentService(StockMovementRepository stockMovementRepository,
                                  StockBalanceService stockBalanceService,
                                  CurrentUserService currentUserService) {
        this.stockMovementRepository = stockMovementRepository;
        this.stockBalanceService = stockBalanceService;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public void adjustStock(StockAdjustmentRequest req) {

        if (req.qty() == null || req.qty().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }

        boolean isOut = "OUT".equalsIgnoreCase(req.direction());

        // Atualiza saldo
        if (isOut) {
            stockBalanceService.removeQty(req.materialId(), req.locationId(), req.qty());
        } else {
            stockBalanceService.addQty(req.materialId(), req.locationId(), req.qty());
        }

        // Registra movimento
        BigDecimal signedQty = isOut ? req.qty().negate() : req.qty();
        Long companyId = currentUserService.getCompanyId();

        stockMovementRepository.insertAdjustment(
                req.materialId(),
                req.warehouseId(),
                req.locationId(),
                signedQty,
                req.reason(),
                req.note(),
                companyId
        );
    }
}
