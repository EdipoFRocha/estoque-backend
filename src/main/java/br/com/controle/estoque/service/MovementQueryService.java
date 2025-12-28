package br.com.controle.estoque.service;

import br.com.controle.estoque.dto.StockMovementView;
import br.com.controle.estoque.dto.StockDTO;
import br.com.controle.estoque.repository.MovementQueryRepository;
import br.com.controle.estoque.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MovementQueryService {

    private final MovementQueryRepository repo;
    private final CurrentUserService currentUserService;

    public MovementQueryService(MovementQueryRepository repo,
                                CurrentUserService currentUserService) {
        this.repo = repo;
        this.currentUserService = currentUserService;
    }

    public List<StockMovementView> lastAdjustments(String typesCsv, Integer limit) {
        List<String> types = (typesCsv == null || typesCsv.isBlank())
                ? List.of("ADJUSTMENT_IN", "ADJUSTMENT_OUT")
                : Arrays.stream(typesCsv.split(","))
                .map(String::trim)
                .toList();

        int lim = (limit == null || limit <= 0) ? 20 : limit;

        Long companyId = currentUserService.getCompanyId(); // 🔹 empresa do usuário logado

        return repo.lastMovementsByTypes(types, lim, companyId);
    }

    public List<StockDTO> stockByMaterial(Long materialId) {
        Long companyId = currentUserService.getCompanyId(); // 🔹 empresa do usuário logado
        return repo.stockByMaterial(materialId, companyId);
    }
}
