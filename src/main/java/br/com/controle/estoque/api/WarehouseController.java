package br.com.controle.estoque.api;

import br.com.controle.estoque.dto.*;
import br.com.controle.estoque.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseService service;

    public WarehouseController(WarehouseService service) {
        this.service = service;
    }

    @GetMapping
    public List<WarehouseResponseDTO> list() {
        return service.list();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WarehouseResponseDTO create(@RequestBody @Valid WarehouseRequestDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public WarehouseResponseDTO update(@PathVariable Long id, @RequestBody @Valid WarehouseUpdateDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        service.deactivate(id);
    }
}
