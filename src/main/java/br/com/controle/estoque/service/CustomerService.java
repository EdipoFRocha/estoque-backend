package br.com.controle.estoque.service;

import br.com.controle.estoque.dto.CustomerRequest;
import br.com.controle.estoque.dto.CustomerResponse;
import br.com.controle.estoque.dto.CustomerUpdateRequest;
import br.com.controle.estoque.model.Company;
import br.com.controle.estoque.model.Customer;
import br.com.controle.estoque.repository.CustomerRepository;
import br.com.controle.estoque.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final CurrentUserService currentUserService;

    public CustomerService(CustomerRepository repository,
                           CurrentUserService currentUserService) {
        this.repository = repository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public CustomerResponse create(CustomerRequest request) {
        Long companyId = currentUserService.getCompanyId();

        repository.findByDocumentAndCompany_Id(request.getDocument(), companyId)
                .ifPresent(c -> {
                    throw new RuntimeException("Já existe cliente com este documento nesta empresa.");
                });

        Customer c = new Customer();
        c.setIsActive(true);
        c.setName(request.getName());
        c.setDocument(request.getDocument());
        c.setType(request.getType() != null ? request.getType() : "PJ");
        c.setStateRegistration(request.getStateRegistration());
        c.setEmail(request.getEmail());
        c.setPhone(request.getPhone());
        c.setAddressLine(request.getAddressLine());
        c.setNumber(request.getNumber());
        c.setComplement(request.getComplement());
        c.setDistrict(request.getDistrict());
        c.setCity(request.getCity());
        c.setState(request.getState());
        c.setZipCode(request.getZipCode());

        Company companyRef = new Company();
        companyRef.setId(companyId);
        c.setCompany(companyRef);

        Customer saved = repository.save(c);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> listAll() {
        Long companyId = currentUserService.getCompanyId();

        return repository.findByCompany_Id(companyId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomerResponse findByDocument(String document) {
        Long companyId = currentUserService.getCompanyId();

        Customer c = repository.findByDocumentAndCompany_Id(document, companyId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado para o documento informado nesta empresa."));
        return toResponse(c);
    }

    @Transactional
    public CustomerResponse update(Long id, CustomerUpdateRequest request) {
        Long companyId = currentUserService.getCompanyId();

        Customer c = repository.findByIdAndCompany_Id(id, companyId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado nesta empresa."));

        if (request.getActive() != null) {
            c.setIsActive(request.getActive());
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            c.setName(request.getName().trim());
        }

        if (request.getDocument() != null && !request.getDocument().isBlank()) {
            String newDoc = request.getDocument().trim();
            if (!newDoc.equals(c.getDocument())) {
                boolean exists = repository.existsByDocumentAndCompany_IdAndIdNot(newDoc, companyId, id);
                if (exists) throw new RuntimeException("Já existe cliente com este documento nesta empresa.");
                c.setDocument(newDoc);
            }
        }

        if (request.getType() != null && !request.getType().isBlank()) c.setType(request.getType().trim());
        if (request.getStateRegistration() != null) c.setStateRegistration(request.getStateRegistration().trim());
        if (request.getEmail() != null) c.setEmail(request.getEmail().trim());
        if (request.getPhone() != null) c.setPhone(request.getPhone().trim());
        if (request.getAddressLine() != null) c.setAddressLine(request.getAddressLine().trim());
        if (request.getNumber() != null) c.setNumber(request.getNumber().trim());
        if (request.getComplement() != null) c.setComplement(request.getComplement().trim());
        if (request.getDistrict() != null) c.setDistrict(request.getDistrict().trim());
        if (request.getCity() != null) c.setCity(request.getCity().trim());
        if (request.getState() != null) c.setState(request.getState().trim());
        if (request.getZipCode() != null) c.setZipCode(request.getZipCode().trim());

        Customer saved = repository.save(c);
        return toResponse(saved);
    }

    private CustomerResponse toResponse(Customer c) {
        CustomerResponse r = new CustomerResponse();
        r.setId(c.getId());
        r.setName(c.getName());
        r.setDocument(c.getDocument());
        r.setType(c.getType());
        r.setStateRegistration(c.getStateRegistration());
        r.setEmail(c.getEmail());
        r.setPhone(c.getPhone());
        r.setAddressLine(c.getAddressLine());
        r.setNumber(c.getNumber());
        r.setComplement(c.getComplement());
        r.setDistrict(c.getDistrict());
        r.setCity(c.getCity());
        r.setState(c.getState());
        r.setZipCode(c.getZipCode());
        r.setActive(c.getIsActive());
        return r;
    }
}