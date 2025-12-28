package br.com.controle.estoque.api;

import br.com.controle.estoque.dto.*;
import br.com.controle.estoque.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService service;

    public LocationController(LocationService service) {
        this.service = service;
    }

    @GetMapping
    public List<LocationResponseDTO> list(@RequestParam Long warehouseId) {
        return service.listByWarehouse(warehouseId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationResponseDTO create(@RequestBody @Valid LocationRequestDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public LocationResponseDTO update(@PathVariable Long id, @RequestBody @Valid LocationUpdateDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        service.deactivate(id);
    }
}
