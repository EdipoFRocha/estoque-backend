package br.com.controle.estoque.api;

import br.com.controle.estoque.dto.CompanyRequest;
import br.com.controle.estoque.dto.CompanyResponse;
import br.com.controle.estoque.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyAdminController {

    private final CompanyService companyService;

    public CompanyAdminController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PreAuthorize("hasRole('MASTER_ADMIN')")
    @GetMapping
    public List<CompanyResponse> listAll() {
        return companyService.listAll();
    }

    @PreAuthorize("hasRole('MASTER_ADMIN')")
    @PostMapping
    public CompanyResponse create(@Valid @RequestBody CompanyRequest req) {
        return companyService.create(req);
    }

    @PreAuthorize("hasRole('MASTER_ADMIN')")
    @PatchMapping("/{id}/active")
    public CompanyResponse setActive(@PathVariable Long id, @RequestParam boolean value) {
        return companyService.setActive(id, value);
    }
}