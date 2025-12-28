package br.com.controle.estoque.repository;

import br.com.controle.estoque.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findByCompany_IdAndWarehouse_IdAndActiveTrueOrderByNameAsc(Long companyId, Long warehouseId);

    Optional<Location> findByIdAndCompany_Id(Long id, Long companyId);

    boolean existsByCompany_IdAndWarehouse_IdAndCodeIgnoreCase(Long companyId, Long warehouseId, String code);

    boolean existsByCompany_IdAndWarehouse_IdAndCodeIgnoreCaseAndIdNot(Long companyId, Long warehouseId, String code, Long id);
}
