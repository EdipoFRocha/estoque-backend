package br.com.controle.estoque.api;

import br.com.controle.estoque.dto.ReturnRequest;
import br.com.controle.estoque.dto.TransferCodeRequest;
import br.com.controle.estoque.dto.TransferRequest;
import br.com.controle.estoque.service.StockOperationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")   // <<< prefixo base ÚNICO
@CrossOrigin
public class StockOperationsController {

    private final StockOperationsService service;

    public StockOperationsController(StockOperationsService service) {
        this.service = service;
    }

    // POST /api/returns   e   POST /api/operations/returns
    @PostMapping({"/returns", "/operations/returns"})
    public ResponseEntity<Map<String, Object>> registerReturn(@RequestBody ReturnRequest req) {
        Long id = service.registerReturn(req);
        return ResponseEntity.ok(Map.of("returnId", id));
    }

    // POST /api/transfers  e  POST /api/operations/transfers
    @PostMapping({"/transfers", "/operations/transfers"})
    public ResponseEntity<Map<String, Object>> transfer(@RequestBody TransferRequest req) {
        Long id = service.transfer(req);
        return ResponseEntity.ok(Map.of("transferId", id));
    }

    // POST /api/transfers/by-code  e  POST /api/operations/transfers/by-code
    @PostMapping({"/transfers/by-code", "/operations/transfers/by-code"})
    public ResponseEntity<Map<String, Object>> transferByCode(@RequestBody TransferCodeRequest req) {
        Long id = service.transferByCode(req);
        return ResponseEntity.ok(Map.of("transferId", id));
    }
}
