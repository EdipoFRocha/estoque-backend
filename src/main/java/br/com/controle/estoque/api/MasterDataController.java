package br.com.controle.estoque.api;

import br.com.controle.estoque.dto.LocationDto;
import br.com.controle.estoque.dto.MaterialDto;
import br.com.controle.estoque.dto.WarehouseDto;
import br.com.controle.estoque.service.MasterDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master-data")
public class MasterDataController {

    private static final Logger log = LoggerFactory.getLogger(MasterDataController.class);

    private final MasterDataService service;

    public MasterDataController(MasterDataService service) {
        this.service = service;
    }

    @GetMapping("/materials")
    public List<MaterialDto> materials() {
        return service.materials();
    }

    @GetMapping("/warehouses")
    public List<WarehouseDto> warehouses() {
        return service.warehouses();
    }

    @GetMapping("/locations")
    public ResponseEntity<?> locations(@RequestParam Long warehouseId) {
        try {
            return ResponseEntity.ok(service.locationsByWarehouse(warehouseId));
        } catch (Exception e) {
            log.error("Erro em GET /api/master-data/locations", e);
            return ResponseEntity.internalServerError()
                    .body("Erro ao listar locais: " + e.getMessage());
        }
    }
}
