package br.com.controle.estoque.api;

import br.com.controle.estoque.dto.CompanyMeResponse;
import br.com.controle.estoque.service.CompanyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public CompanyMeResponse getMyCompany() {
        return companyService.getCurrentCompany();
    }
}
