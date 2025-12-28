package br.com.controle.estoque.service;

import br.com.controle.estoque.dto.StockReceiptRequestDTO;
import br.com.controle.estoque.model.Company;
import br.com.controle.estoque.model.StockMovement;
import br.com.controle.estoque.repository.StockMovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class StockReceiptService {

    private final StockMovementRepository movementRepository;
    private final StockBalanceService stockBalanceService;
    private final CompanyService companyService;

    public StockReceiptService(StockMovementRepository movementRepository,
                               StockBalanceService stockBalanceService,
                               CompanyService companyService) {
        this.movementRepository = movementRepository;
        this.stockBalanceService = stockBalanceService;
        this.companyService = companyService;
    }

    @Transactional
    public void receipt(StockReceiptRequestDTO dto) {

        Long companyId = companyService.getCurrentCompany().getId();

        Company c = new Company();
        c.setId(companyId);

        StockMovement mv = new StockMovement();
        mv.setType("RECEIPT");
        mv.setLotId(null);
        mv.setMaterialId(dto.materialId());
        mv.setLocationId(dto.locationId());
        mv.setQty(dto.quantity());
        mv.setNote(buildNote(dto.documentRef(), dto.notes()));
        mv.setReason("RECEIPT");
        mv.setCreatedAt(LocalDateTime.now());
        mv.setCompany(c);

        movementRepository.save(mv);

        stockBalanceService.addQty(
                dto.materialId(),
                dto.locationId(),
                dto.quantity()
        );
    }

    private String buildNote(String documentRef, String notes) {
        String dr = (documentRef == null || documentRef.isBlank()) ? null : documentRef.trim();
        String nt = (notes == null || notes.isBlank()) ? null : notes.trim();

        if (dr == null && nt == null) return null;
        if (dr != null && nt == null) return "DOC: " + dr;
        if (dr == null) return nt;
        return "DOC: " + dr + " | " + nt;
    }
}
