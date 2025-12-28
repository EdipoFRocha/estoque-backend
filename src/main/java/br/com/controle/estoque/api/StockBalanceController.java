package br.com.controle.estoque.api;

import br.com.controle.estoque.dto.StockBalanceResponseDTO;
import br.com.controle.estoque.service.StockBalanceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock/balances")
public class StockBalanceController {

    private final StockBalanceService service;

    public StockBalanceController(StockBalanceService service) {
        this.service = service;
    }

    @GetMapping
    public List<StockBalanceResponseDTO> list(
            @RequestParam(required = false) Long materialId,
            @RequestParam(required = false) Long locationId
    ) {
        if (materialId != null) return service.listByMaterial(materialId);
        if (locationId != null) return service.listByLocation(locationId);
        return service.listAll();
    }
}
