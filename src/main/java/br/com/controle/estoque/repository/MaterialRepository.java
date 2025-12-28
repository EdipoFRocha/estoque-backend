package br.com.controle.estoque.repository;

import br.com.controle.estoque.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    boolean existsByCompany_IdAndCodeIgnoreCase(Long companyId, String code);

    List<Material> findByCompany_IdAndActiveTrueOrderByNameAsc(Long companyId);

    Optional<Material> findByIdAndCompany_Id(Long id, Long companyId);

    boolean existsByCompany_IdAndCodeIgnoreCaseAndIdNot(Long companyId, String code, Long id);
}
