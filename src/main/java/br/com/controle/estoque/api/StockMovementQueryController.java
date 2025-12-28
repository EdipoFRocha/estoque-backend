package br.com.controle.estoque.api;

import br.com.controle.estoque.dto.StockDTO;
import br.com.controle.estoque.dto.StockMovementView;
import br.com.controle.estoque.service.MovementQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movements") // <— prefixo LITERAL
@CrossOrigin
public class StockMovementQueryController {

    private final MovementQueryService service;

    public StockMovementQueryController(MovementQueryService service) {
        this.service = service;
    }

    // GET /api/movements?types=ADJUSTMENT_IN,ADJUSTMENT_OUT&limit=20
    @GetMapping
    public ResponseEntity<List<StockMovementView>> lastMovements(
            @RequestParam(name = "types", required = false) String typesCsv,
            @RequestParam(name = "limit", required = false) Integer limit
    ) {
        return ResponseEntity.ok(service.lastAdjustments(typesCsv, limit));
    }

    // GET /api/movements/material/{materialId}/stock
    @GetMapping("/material/{materialId}/stock")
    public ResponseEntity<List<StockDTO>> stockByMaterial(@PathVariable Long materialId) {
        return ResponseEntity.ok(service.stockByMaterial(materialId));
    }
}
