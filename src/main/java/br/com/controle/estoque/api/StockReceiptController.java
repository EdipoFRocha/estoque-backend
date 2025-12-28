package br.com.controle.estoque.api;

import br.com.controle.estoque.dto.StockReceiptRequestDTO;
import br.com.controle.estoque.service.StockReceiptService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock/receipt")
public class StockReceiptController {

    private final StockReceiptService service;

    public StockReceiptController(StockReceiptService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void receipt(@RequestBody @Valid StockReceiptRequestDTO dto) {
        service.receipt(dto);
    }
}
