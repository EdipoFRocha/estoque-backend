package br.com.controle.estoque.service;

import br.com.controle.estoque.dto.CompanyMeResponse;
import br.com.controle.estoque.dto.CompanyRequest;
import br.com.controle.estoque.dto.CompanyResponse;
import br.com.controle.estoque.model.Company;
import br.com.controle.estoque.repository.CompanyRepository;
import br.com.controle.estoque.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CurrentUserService currentUserService;

    public CompanyService(CompanyRepository companyRepository,
                          CurrentUserService currentUserService) {
        this.companyRepository = companyRepository;
        this.currentUserService = currentUserService;
    }

    public Long getCurrentCompanyId() {
        return getCurrentCompany().getId();
    }


    @Transactional(readOnly = true)
    public CompanyMeResponse getCurrentCompany() {
        Long companyId = currentUserService.getCompanyId();

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Empresa não encontrada para o usuário logado. ID = " + companyId
                ));

        return toMeResponse(company);
    }

    @Transactional(readOnly = true)
    public List<CompanyResponse> listAll() {
        return companyRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CompanyResponse create(CompanyRequest req) {
        String name = req.name().trim();

        if (companyRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException("Já existe uma empresa com esse nome.");
        }

        Company c = new Company();
        c.setName(name);
        c.setTradeName(req.tradeName());
        c.setDocument(req.document());
        c.setActive(true);

        Company saved = companyRepository.save(c);
        return toResponse(saved);
    }

    @Transactional
    public CompanyResponse setActive(Long companyId, boolean value) {
        Company c = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada."));

        c.setActive(value);
        return toResponse(c);
    }

    private CompanyMeResponse toMeResponse(Company c) {
        CompanyMeResponse dto = new CompanyMeResponse();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setTradeName(c.getTradeName());
        dto.setDocument(c.getDocument());
        dto.setCreatedAt(c.getCreatedAt());
        return dto;
    }

    private CompanyResponse toResponse(Company c) {
        return new CompanyResponse(
                c.getId(),
                c.getName(),
                c.getTradeName(),
                c.getDocument(),
                c.getActive(),
                c.getCreatedAt()
        );
    }
}
