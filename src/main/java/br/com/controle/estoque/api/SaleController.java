package br.com.controle.estoque.api;

import br.com.controle.estoque.dto.SaleRequest;
import br.com.controle.estoque.service.SaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "http://localhost:5173") // redundante ao CorsConfig, mas ajuda
public class SaleController {

    private final SaleService service;

    public SaleController(SaleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody SaleRequest req) {
        return ResponseEntity.ok(service.registerSale(req));
    }
}
