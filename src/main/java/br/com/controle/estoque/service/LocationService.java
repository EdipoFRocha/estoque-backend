package br.com.controle.estoque.service;

import br.com.controle.estoque.dto.*;
import br.com.controle.estoque.model.Company;
import br.com.controle.estoque.model.Location;
import br.com.controle.estoque.model.Warehouse;
import br.com.controle.estoque.repository.LocationRepository;
import br.com.controle.estoque.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository repository;
    private final WarehouseRepository warehouseRepository;
    private final CompanyService companyService;

    public LocationService(LocationRepository repository,
                           WarehouseRepository warehouseRepository,
                           CompanyService companyService) {
        this.repository = repository;
        this.warehouseRepository = warehouseRepository;
        this.companyService = companyService;
    }

    public List<LocationResponseDTO> listByWarehouse(Long warehouseId) {
        Long companyId = companyService.getCurrentCompany().getId();

        warehouseRepository.findByIdAndCompany_Id(warehouseId, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Almoxarifado não encontrado para esta empresa."));

        return repository.findByCompany_IdAndWarehouse_IdAndActiveTrueOrderByNameAsc(companyId, warehouseId)
                .stream().map(this::toDTO).toList();
    }

    public LocationResponseDTO create(LocationRequestDTO dto) {
        Long companyId = companyService.getCurrentCompany().getId();

        Warehouse warehouse = warehouseRepository.findByIdAndCompany_Id(dto.warehouseId(), companyId)
                .orElseThrow(() -> new IllegalArgumentException("Almoxarifado não encontrado para esta empresa."));

        String code = dto.code().trim();
        if (repository.existsByCompany_IdAndWarehouse_IdAndCodeIgnoreCase(companyId, warehouse.getId(), code)) {
            throw new IllegalArgumentException("Já existe local com esse código neste almoxarifado.");
        }

        Location loc = new Location();
        loc.setCode(code);
        loc.setName(dto.name().trim());
        loc.setActive(true);

        Company c = new Company();
        c.setId(companyId);
        loc.setCompany(c);

        loc.setWarehouse(warehouse);

        return toDTO(repository.save(loc));
    }

    @Transactional
    public LocationResponseDTO update(Long id, LocationUpdateDTO dto) {
        Long companyId = companyService.getCurrentCompany().getId();

        Location loc = repository.findByIdAndCompany_Id(id, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Local não encontrado."));

        Warehouse warehouse = loc.getWarehouse();

        if (dto.warehouseId() != null && !dto.warehouseId().equals(warehouse.getId())) {
            warehouse = warehouseRepository.findByIdAndCompany_Id(dto.warehouseId(), companyId)
                    .orElseThrow(() -> new IllegalArgumentException("Almoxarifado não encontrado para esta empresa."));
            loc.setWarehouse(warehouse);
        }

        if (dto.code() != null && !dto.code().isBlank()) {
            String newCode = dto.code().trim();
            if (repository.existsByCompany_IdAndWarehouse_IdAndCodeIgnoreCaseAndIdNot(companyId, warehouse.getId(), newCode, id)) {
                throw new IllegalArgumentException("Já existe local com esse código neste almoxarifado.");
            }
            loc.setCode(newCode);
        }

        if (dto.name() != null && !dto.name().isBlank()) {
            loc.setName(dto.name().trim());
        }

        if (dto.active() != null) {
            loc.setActive(dto.active());
        }

        return toDTO(repository.save(loc));
    }

    @Transactional
    public void deactivate(Long id) {
        Long companyId = companyService.getCurrentCompany().getId();

        Location loc = repository.findByIdAndCompany_Id(id, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Local não encontrado."));

        loc.setActive(false);
        repository.save(loc);
    }

    private LocationResponseDTO toDTO(Location loc) {
        return new LocationResponseDTO(
                loc.getId(),
                loc.getWarehouse().getId(),
                loc.getWarehouse().getName(),
                loc.getCode(),
                loc.getName(),
                loc.getActive()
        );
    }
}
