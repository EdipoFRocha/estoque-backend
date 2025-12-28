package br.com.controle.estoque.repository;

import br.com.controle.estoque.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByDocumentAndCompany_Id(String document, Long companyId);

    List<Customer> findByCompany_Id(Long companyId);

    // necessário pro update com segurança por empresa
    Optional<Customer> findByIdAndCompany_Id(Long id, Long companyId);

    // necessário pra checar documento duplicado ignorando o próprio registro
    boolean existsByDocumentAndCompany_IdAndIdNot(String document, Long companyId, Long id);
}
