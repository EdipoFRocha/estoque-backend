package br.com.controle.estoque.api;

import br.com.controle.estoque.dto.MaterialRequestDTO;
import br.com.controle.estoque.dto.MaterialResponseDTO;
import br.com.controle.estoque.dto.MaterialUpdateDTO;
import br.com.controle.estoque.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService service;

    public MaterialController(MaterialService service) {
        this.service = service;
    }

    @GetMapping
    public List<MaterialResponseDTO> list() {
        return service.list();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MaterialResponseDTO create(@RequestBody @Valid MaterialRequestDTO dto) {
        return service.create(dto);
    }

    // PUT /api/materials/{id}
    @PutMapping("/{id}")
    public MaterialResponseDTO update(@PathVariable Long id, @RequestBody @Valid MaterialUpdateDTO dto) {
        return service.update(id, dto);
    }

    // DELETE lógico /api/materials/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        service.deactivate(id);
    }
}
