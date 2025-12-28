package br.com.controle.estoque.api;

import br.com.controle.estoque.dto.StockAdjustmentRequest;
import br.com.controle.estoque.service.StockAdjustmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock/adjustments")
@CrossOrigin
public class StockAdjustmentController {

    private final StockAdjustmentService service;

    public StockAdjustmentController(StockAdjustmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> createAdjustment(@RequestBody @Valid StockAdjustmentRequest req) {
        service.adjustStock(req);
        return ResponseEntity.ok().build();
    }
}
