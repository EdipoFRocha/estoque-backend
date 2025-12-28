package br.com.controle.estoque.service;

import br.com.controle.estoque.dto.StockMovementResponseDTO;
import br.com.controle.estoque.model.StockMovement;
import br.com.controle.estoque.repository.StockMovementRepository;
import br.com.controle.estoque.security.CurrentUserService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class StockMovementService {

    private final StockMovementRepository repository;
    private final CurrentUserService currentUserService;

    public StockMovementService(StockMovementRepository repository,
                                CurrentUserService currentUserService) {
        this.repository = repository;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public Page<StockMovementResponseDTO> list(
            Long materialId,
            Long warehouseId,
            Long locationId,
            String type,
            LocalDateTime start,
            LocalDateTime end,
            int page,
            int size
    ) {
        Long companyId = currentUserService.getCompanyId();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<StockMovement> result;

        if (start != null && end != null) {
            result = repository.findByCompany_IdAndCreatedAtBetweenOrderByCreatedAtDesc(
                    companyId, start, end, pageable
            );
        } else if (materialId != null) {
            result = repository.findByCompany_IdAndMaterialIdOrderByCreatedAtDesc(
                    companyId, materialId, pageable
            );
        } else if (warehouseId != null) {
            result = repository.findByCompany_IdAndWarehouseIdOrderByCreatedAtDesc(
                    companyId, warehouseId, pageable
            );
        } else if (locationId != null) {
            result = repository.findByCompany_IdAndLocationIdOrderByCreatedAtDesc(
                    companyId, locationId, pageable
            );
        } else if (type != null && !type.isBlank()) {
            result = repository.findByCompany_IdAndTypeOrderByCreatedAtDesc(
                    companyId, type.trim(), pageable
            );
        } else {
            result = repository.findByCompany_IdOrderByCreatedAtDesc(companyId, pageable);
        }

        return result.map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public StockMovementResponseDTO getById(Long id) {
        Long companyId = currentUserService.getCompanyId();

        StockMovement mv = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movimento não encontrado"));

        if (mv.getCompany() == null || !mv.getCompany().getId().equals(companyId)) {
            throw new IllegalArgumentException("Movimento não pertence à empresa");
        }

        return toDTO(mv);
    }

    private StockMovementResponseDTO toDTO(StockMovement mv) {
        return new StockMovementResponseDTO(
                mv.getId(),
                mv.getType(),
                mv.getLotId(),
                mv.getMaterialId(),
                mv.getWarehouseId(),
                mv.getLocationId(),
                mv.getQty(),
                mv.getReason(),
                mv.getNote(),
                mv.getCreatedAt()
        );
    }
}
