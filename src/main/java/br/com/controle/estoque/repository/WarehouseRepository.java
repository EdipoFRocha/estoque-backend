package br.com.controle.estoque.repository;

import br.com.controle.estoque.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    List<Warehouse> findByCompany_IdAndActiveTrueOrderByNameAsc(Long companyId);

    Optional<Warehouse> findByIdAndCompany_Id(Long id, Long companyId);

    boolean existsByCompany_IdAndCodeIgnoreCase(Long companyId, String code);

    boolean existsByCompany_IdAndCodeIgnoreCaseAndIdNot(Long companyId, String code, Long id);
}
