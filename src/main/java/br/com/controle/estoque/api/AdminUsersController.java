package br.com.controle.estoque.api;

import br.com.controle.estoque.dto.UserCreateRequest;
import br.com.controle.estoque.dto.UserResponse;
import br.com.controle.estoque.service.UserAdminService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUsersController {

    private final UserAdminService userAdminService;

    public AdminUsersController(UserAdminService userAdminService) {
        this.userAdminService = userAdminService;
    }

    @GetMapping
    public List<UserResponse> listAll(@RequestParam(required = false) Long companyId) {
        return userAdminService.listAll(companyId);
    }

    // CRIAR USUÁRIO (o front chama POST /api/admin/users)
    @PostMapping
    public UserResponse create(@RequestBody @Valid UserCreateRequest request) {
        return userAdminService.create(request);
    }

    // ATIVAR/DESATIVAR (o front chama PATCH /api/admin/users/{id}/toggle-active)
    @PatchMapping("/{id}/toggle-active")
    public UserResponse toggleActive(
            @PathVariable Long id,
            @RequestParam(required = false) Long companyId
    ) {
        return userAdminService.toggleActive(id, companyId);
    }
}
