package br.com.controle.estoque.service;

import br.com.controle.estoque.dto.CompanyMeResponse;
import br.com.controle.estoque.dto.MaterialRequestDTO;
import br.com.controle.estoque.dto.MaterialResponseDTO;
import br.com.controle.estoque.dto.MaterialUpdateDTO;
import br.com.controle.estoque.model.Company;
import br.com.controle.estoque.model.Material;
import br.com.controle.estoque.repository.MaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MaterialService {

    private final MaterialRepository repository;
    private final CompanyService companyService;

    public MaterialService(MaterialRepository repository, CompanyService companyService) {
        this.repository = repository;
        this.companyService = companyService;
    }

    public List<MaterialResponseDTO> list() {
        CompanyMeResponse me = companyService.getCurrentCompany();
        Long companyId = me.getId();

        return repository.findByCompany_IdAndActiveTrueOrderByNameAsc(companyId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public MaterialResponseDTO create(MaterialRequestDTO dto) {
        CompanyMeResponse me = companyService.getCurrentCompany();
        Long companyId = me.getId();

        String code = dto.code().trim();
        if (repository.existsByCompany_IdAndCodeIgnoreCase(companyId, code)) {
            throw new IllegalArgumentException("Já existe material com esse código.");
        }

        Material material = new Material();
        material.setCode(code);
        material.setName(dto.name().trim());
        material.setUnit(dto.unit().trim());
        material.setDescription(dto.description());
        material.setActive(true);

        material.setSalePrice(dto.salePrice());

        Company company = new Company();
        company.setId(companyId);
        material.setCompany(company);

        String sku = "SKU-" + companyId + "-" +
                UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
        material.setSku(sku);

        material = repository.save(material);
        return toDTO(material);
    }

    @Transactional
    public MaterialResponseDTO update(Long id, MaterialUpdateDTO dto) {
        CompanyMeResponse me = companyService.getCurrentCompany();
        Long companyId = me.getId();

        Material material = repository.findByIdAndCompany_Id(id, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Material não encontrado."));

        // code (se vier, valida duplicidade)
        if (dto.code() != null && !dto.code().isBlank()) {
            String newCode = dto.code().trim();
            if (repository.existsByCompany_IdAndCodeIgnoreCaseAndIdNot(companyId, newCode, id)) {
                throw new IllegalArgumentException("Já existe material com esse código.");
            }
            material.setCode(newCode);
        }

        // name
        if (dto.name() != null && !dto.name().isBlank()) {
            material.setName(dto.name().trim());
        }

        // unit
        if (dto.unit() != null && !dto.unit().isBlank()) {
            material.setUnit(dto.unit().trim());
        }

        // description (pode zerar também)
        if (dto.description() != null) {
            material.setDescription(dto.description().trim());
        }

        // active
        if (dto.active() != null) {
            material.setActive(dto.active());
        }

        // salePrice (pode setar e também pode "zerar" mandando 0)
        if (dto.salePrice() != null) {
            BigDecimal v = dto.salePrice();
            material.setSalePrice(v);
        }

        return toDTO(repository.save(material));
    }

    @Transactional
    public void deactivate(Long id) {
        CompanyMeResponse me = companyService.getCurrentCompany();
        Long companyId = me.getId();

        Material material = repository.findByIdAndCompany_Id(id, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Material não encontrado."));

        material.setActive(false);
        repository.save(material);
    }

    private MaterialResponseDTO toDTO(Material m) {
        return new MaterialResponseDTO(
                m.getId(),
                m.getSku(),
                m.getCode(),
                m.getName(),
                m.getUnit(),
                m.getDescription(),
                m.getActive(),
                m.getSalePrice()
        );
    }
}
