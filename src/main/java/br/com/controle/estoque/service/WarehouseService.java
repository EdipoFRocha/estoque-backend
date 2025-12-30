package br.com.controle.estoque.service;

import br.com.controle.estoque.dto.*;
import br.com.controle.estoque.model.Company;
import br.com.controle.estoque.model.Warehouse;
import br.com.controle.estoque.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WarehouseService {

    private final WarehouseRepository repository;
    private final CompanyService companyService;

    public WarehouseService(WarehouseRepository repository, CompanyService companyService) {
        this.repository = repository;
        this.companyService = companyService;
    }

    @Transactional(readOnly = true)
    public List<WarehouseResponseDTO> list() {
        Long companyId = companyService.getCurrentCompanyId();

        return repository.findByCompany_IdAndActiveTrueOrderByNameAsc(companyId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public WarehouseResponseDTO create(WarehouseRequestDTO dto) {
        Long companyId = companyService.getCurrentCompanyId();

        String code = dto.code().trim();
        if (repository.existsByCompany_IdAndCodeIgnoreCase(companyId, code)) {
            throw new IllegalArgumentException("Já existe almoxarifado com esse código.");
        }

        Warehouse w = new Warehouse();
        w.setCode(code);
        w.setName(dto.name().trim());
        w.setActive(true);

        // referência por id (ok)
        Company c = new Company();
        c.setId(companyId);
        w.setCompany(c);

        return toDTO(repository.save(w));
    }

    @Transactional
    public WarehouseResponseDTO update(Long id, WarehouseUpdateDTO dto) {
        Long companyId = companyService.getCurrentCompanyId();

        Warehouse w = repository.findByIdAndCompany_Id(id, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Almoxarifado não encontrado."));

        if (dto.code() != null && !dto.code().isBlank()) {
            String newCode = dto.code().trim();
            if (repository.existsByCompany_IdAndCodeIgnoreCaseAndIdNot(companyId, newCode, id)) {
                throw new IllegalArgumentException("Já existe almoxarifado com esse código.");
            }
            w.setCode(newCode);
        }

        if (dto.name() != null && !dto.name().isBlank()) {
            w.setName(dto.name().trim());
        }

        if (dto.active() != null) {
            w.setActive(dto.active());
        }

        return toDTO(repository.save(w));
    }

    @Transactional
    public void deactivate(Long id) {
        Long companyId = companyService.getCurrentCompanyId();

        Warehouse w = repository.findByIdAndCompany_Id(id, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Almoxarifado não encontrado."));

        w.setActive(false);
        repository.save(w);
    }

    private WarehouseResponseDTO toDTO(Warehouse w) {
        return new WarehouseResponseDTO(w.getId(), w.getCode(), w.getName(), w.getActive());
    }
}
