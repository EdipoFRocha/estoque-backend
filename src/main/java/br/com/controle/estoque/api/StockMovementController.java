package br.com.controle.estoque.api;

import br.com.controle.estoque.dto.StockMovementResponseDTO;
import br.com.controle.estoque.service.StockMovementService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/stock/movements")
@CrossOrigin
public class StockMovementController {

    private final StockMovementService service;

    public StockMovementController(StockMovementService service) {
        this.service = service;
    }

    // GET /api/stock/movements?page=0&size=20
    @GetMapping
    public Page<StockMovementResponseDTO> list(
            @RequestParam(required = false) Long materialId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) String type,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return service.list(materialId, warehouseId, locationId, type, start, end, page, size);
    }

    // GET /api/stock/movements/{id}
    @GetMapping("/{id}")
    public StockMovementResponseDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }
}
