package br.com.controle.estoque.api;

import br.com.controle.estoque.dto.CustomerRequest;
import br.com.controle.estoque.dto.CustomerResponse;
import br.com.controle.estoque.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.controle.estoque.dto.CustomerUpdateRequest;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@RequestBody CustomerRequest request) {
        CustomerResponse resp = service.create(request);
        return ResponseEntity.ok(resp);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }

    @GetMapping("/by-document")
    public ResponseEntity<CustomerResponse> findByDocument(@RequestParam String document) {
        return ResponseEntity.ok(service.findByDocument(document));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(
            @PathVariable Long id,
            @RequestBody CustomerUpdateRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }
}