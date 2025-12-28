package br.com.controle.estoque.service;

import br.com.controle.estoque.dto.LocationDto;
import br.com.controle.estoque.dto.MaterialDto;
import br.com.controle.estoque.dto.WarehouseDto;
import br.com.controle.estoque.repository.MasterDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MasterDataService {

    private final MasterDataRepository repo;
    private final CompanyService companyService;

    public MasterDataService(MasterDataRepository repo, CompanyService companyService) {
        this.repo = repo;
        this.companyService = companyService;
    }

    public List<MaterialDto> materials() {
        Long companyId = companyService.getCurrentCompany().getId();
        return repo.findMaterials(companyId);
    }

    public List<WarehouseDto> warehouses() {
        Long companyId = companyService.getCurrentCompany().getId();
        return repo.findWarehouses(companyId);
    }

    public List<LocationDto> locationsByWarehouse(Long warehouseId) {
        Long companyId = companyService.getCurrentCompany().getId();
        return repo.findLocationsByWarehouse(companyId, warehouseId);
    }
}
