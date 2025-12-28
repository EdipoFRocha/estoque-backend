package br.com.controle.estoque.service;

import br.com.controle.estoque.dto.StockBalanceResponseDTO;
import br.com.controle.estoque.model.*;
import br.com.controle.estoque.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StockBalanceService {

    private final StockBalanceRepository repository;
    private final CompanyService companyService;
    private final MaterialRepository materialRepository;
    private final LocationRepository locationRepository;

    public StockBalanceService(StockBalanceRepository repository,
                               CompanyService companyService,
                               MaterialRepository materialRepository,
                               LocationRepository locationRepository) {
        this.repository = repository;
        this.companyService = companyService;
        this.materialRepository = materialRepository;
        this.locationRepository = locationRepository;
    }

    @Transactional(readOnly = true)
    public List<StockBalanceResponseDTO> listAll() {
        Long companyId = companyService.getCurrentCompany().getId();
        return repository.findByCompany_IdOrderByIdAsc(companyId)
                .stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<StockBalanceResponseDTO> listByMaterial(Long materialId) {
        Long companyId = companyService.getCurrentCompany().getId();
        return repository.findByCompany_IdAndMaterial_IdOrderByIdAsc(companyId, materialId)
                .stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<StockBalanceResponseDTO> listByLocation(Long locationId) {
        Long companyId = companyService.getCurrentCompany().getId();
        return repository.findByCompany_IdAndLocation_IdOrderByIdAsc(companyId, locationId)
                .stream().map(this::toDTO).toList();
    }

    @Transactional
    public StockBalance getOrCreate(Long materialId, Long locationId) {
        Long companyId = companyService.getCurrentCompany().getId();

        Material material = materialRepository.findByIdAndCompany_Id(materialId, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Material não encontrado para esta empresa."));

        Location location = locationRepository.findByIdAndCompany_Id(locationId, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Local não encontrado para esta empresa."));

        return repository.findByCompany_IdAndMaterial_IdAndLocation_Id(companyId, materialId, locationId)
                .orElseGet(() -> {
                    StockBalance sb = new StockBalance();

                    Company c = new Company();
                    c.setId(companyId);
                    sb.setCompany(c);

                    sb.setMaterial(material);
                    sb.setLocation(location);
                    sb.setQty(BigDecimal.ZERO);

                    return repository.save(sb);
                });
    }

    @Transactional
    public StockBalance addQty(Long materialId, Long locationId, BigDecimal delta) {
        if (delta == null || delta.signum() <= 0) {
            throw new IllegalArgumentException("Quantidade para adicionar deve ser maior que zero.");
        }
        StockBalance sb = getOrCreate(materialId, locationId);
        sb.setQty(sb.getQty().add(delta));
        return repository.save(sb);
    }

    @Transactional
    public StockBalance removeQty(Long materialId, Long locationId, BigDecimal delta) {
        if (delta == null || delta.signum() <= 0) {
            throw new IllegalArgumentException("Quantidade para remover deve ser maior que zero.");
        }
        StockBalance sb = getOrCreate(materialId, locationId);
        BigDecimal newQty = sb.getQty().subtract(delta);
        if (newQty.signum() < 0) {
            throw new IllegalArgumentException("Saldo insuficiente no local.");
        }
        sb.setQty(newQty);
        return repository.save(sb);
    }

    private StockBalanceResponseDTO toDTO(StockBalance sb) {
        return new StockBalanceResponseDTO(
                sb.getId(),
                sb.getMaterial().getId(),
                sb.getMaterial().getCode(),
                sb.getMaterial().getName(),
                sb.getLocation().getId(),
                sb.getLocation().getCode(),
                sb.getLocation().getName(),
                sb.getQty()
        );
    }
}
