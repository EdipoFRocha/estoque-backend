package br.com.controle.estoque.service;

import br.com.controle.estoque.auth.AppRole;
import br.com.controle.estoque.auth.AppUser;
import br.com.controle.estoque.auth.AppRoleRepository;
import br.com.controle.estoque.auth.AppUserRepository;
import br.com.controle.estoque.dto.UserCreateRequest;
import br.com.controle.estoque.dto.UserResponse;
import br.com.controle.estoque.exception.DuplicateUsernameException;
import br.com.controle.estoque.model.Company;
import br.com.controle.estoque.repository.CompanyRepository;
import br.com.controle.estoque.security.CurrentUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Service
public class UserAdminService {

    private final AppUserRepository userRepository;
    private final AppRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserService currentUserService;
    private final CompanyRepository companyRepository;

    public UserAdminService(AppUserRepository userRepository,
                            AppRoleRepository roleRepository,
                            PasswordEncoder passwordEncoder,
                            CurrentUserService currentUserService,
                            CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.currentUserService = currentUserService;
        this.companyRepository = companyRepository;
    }

    //  LISTAR USUÁRIOS
    public List<UserResponse> listAll(Long companyIdParam) {

        Long companyId;

        if (companyIdParam != null) {
            // pediu companyId explicitamente
            if (!hasRole("MASTER_ADMIN")) {
                throw new org.springframework.security.access.AccessDeniedException(
                        "Apenas MASTER_ADMIN pode listar usuários de outra empresa."
                );
            }
            companyId = companyIdParam;
        } else {
            // sem parâmetro: usa empresa do logado
            companyId = currentUserService.getCompanyId();
        }

        return userRepository.findByCompany_Id(companyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }
    @Transactional
    public UserResponse create(UserCreateRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new DuplicateUsernameException("Já existe um usuário com esse login.");
        }

        // Busca o papel (GERENTE, SUPERVISAO, etc.) na tabela app_role
        AppRole role = roleRepository.findByName(req.getRole())
                .orElseThrow(() ->
                        new IllegalArgumentException("Papel não encontrado: " + req.getRole())
                );

        // NÃO PERMITE CRIAR USUÁRIO MASTER
        if (isMasterRole(role.getName())) {
            throw new IllegalArgumentException(
                    "Usuário master só pode ser criado pela TI / configuração do sistema."
            );
        }

        // ======= Decide a empresa do novo usuário =======
        Long companyId;
        if (hasRole("MASTER_ADMIN")) {
            if (req.getCompanyId() == null) {
                throw new IllegalArgumentException("Empresa é obrigatória para criar usuário (MASTER_ADMIN).");
            }
            companyId = req.getCompanyId();
        } else {
            // GERENTE / SUPERVISAO sempre criam na própria empresa
            companyId = currentUserService.getCompanyId();
        }

        // Valida empresa existe e está ativa
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada: id=" + companyId));

        if (Boolean.FALSE.equals(company.getActive())) {
            throw new IllegalArgumentException("Empresa inativa. Não é possível criar usuários nela.");
        }

        // ======= Cria usuário =======
        AppUser user = new AppUser();
        user.setUsername(req.getUsername());
        user.setFullName(req.getFullName());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setIsActive(true);

        // Vincula empresa validada
        user.setCompany(company);

        user.getRoles().clear();
        user.getRoles().add(role);

        AppUser saved = userRepository.save(user);
        return toResponse(saved);
    }

    // ATIVAR/DESATIVAR USUÁRIO DENTRO DA MESMA EMPRESA
    @Transactional
    public UserResponse toggleActive(Long userId, Long companyIdParam) {

        Long companyId;

        // ===== Define empresa alvo =====
        if (companyIdParam != null) {
            if (!hasRole("MASTER_ADMIN")) {
                throw new org.springframework.security.access.AccessDeniedException(
                        "Apenas MASTER_ADMIN pode alterar usuários de outra empresa."
                );
            }
            companyId = companyIdParam;
        } else {
            companyId = currentUserService.getCompanyId();
        }

        // ===== Busca usuário =====
        AppUser user = userRepository.findByIdAndCompany_Id(userId, companyId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Usuário não encontrado para esta empresa: id = " + userId
                ));

        // ===== TRAVAS DE SEGURANÇA =====

        //  1. Não permitir desativar o próprio usuário
        Long currentUserId = currentUserService.getUserId();
        if (user.getId().equals(currentUserId)) {
            throw new IllegalArgumentException(
                    "Você não pode desativar o próprio usuário."
            );
        }

        //  2. Não permitir desativar usuário MASTER_ADMIN
        boolean isTargetMaster = user.getRoles()
                .stream()
                .anyMatch(r -> "MASTER_ADMIN".equals(r.getName()));

        if (isTargetMaster) {
            throw new IllegalArgumentException(
                    "Usuário MASTER_ADMIN não pode ser desativado."
            );
        }

        // ===== Alterna status =====
        Boolean current = user.getIsActive();
        boolean newValue = !Boolean.TRUE.equals(current);
        user.setIsActive(newValue);

        AppUser saved = userRepository.save(user);
        return toResponse(saved);
    }


    // Converte AppUser -> UserResponse (DTO pra mandar pro front)
    private UserResponse toResponse(AppUser u) {
        UserResponse r = new UserResponse();
        r.setId(u.getId());
        r.setUsername(u.getUsername());
        r.setFullName(u.getFullName());
        r.setIsActive(u.getIsActive());
        r.setCreatedAt(u.getCreatedAt());
        r.setRoles(
                u.getRoles()
                        .stream()
                        .map(AppRole::getName)
                        .toList()
        );
        return r;
    }

    // ============ BLOQUEIO DE ROLE MASTER ============

    private boolean isMasterRole(String roleName) {
        if (roleName == null) return false;

        return switch (roleName) {
            case "MASTER",
                 "MASTER_ADMIN",
                 "GERENTE_GLOBAL",
                 "ADMIN_GLOBAL" -> true;
            default -> false;
        };
    }

    private boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;

        return auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_" + role));
    }
}
