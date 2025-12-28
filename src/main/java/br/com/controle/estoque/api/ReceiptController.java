package br.com.controle.estoque.api;

import br.com.controle.estoque.dto.ReceiptRequest;
import br.com.controle.estoque.service.ReceiptService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/receipts")
@CrossOrigin
public class ReceiptController {

    private final ReceiptService service;

    public ReceiptController(ReceiptService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody @Valid ReceiptRequest req) {
        Long movementId = service.registerReceipt(req);
        return ResponseEntity.ok().body(java.util.Map.of("movementId", movementId));
    }
}
