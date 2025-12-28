package br.com.controle.estoque.repository;

import br.com.controle.estoque.model.StockBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockBalanceRepository extends JpaRepository<StockBalance, Long> {

    Optional<StockBalance> findByCompany_IdAndMaterial_IdAndLocation_Id(Long companyId, Long materialId, Long locationId);

    List<StockBalance> findByCompany_IdAndMaterial_IdOrderByIdAsc(Long companyId, Long materialId);

    List<StockBalance> findByCompany_IdAndLocation_IdOrderByIdAsc(Long companyId, Long locationId);

    List<StockBalance> findByCompany_IdOrderByIdAsc(Long companyId);
}
