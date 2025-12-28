package br.com.controle.estoque.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);

    List<AppUser> findByCompany_Id(Long companyId);

    Optional<AppUser> findByIdAndCompany_Id(Long id, Long companyId);

    boolean existsByUsernameAndCompany_Id(String username, Long companyId);

    @Query("""
        select u.isActive
        from AppUser u
        where u.username = :username
    """)
    Optional<Boolean> findUserActiveByUsername(@Param("username") String username);

    @Query("""
        select c.active
        from AppUser u
        join u.company c
        where u.username = :username
    """)
    Optional<Boolean> findCompanyActiveByUsername(@Param("username") String username);
}
