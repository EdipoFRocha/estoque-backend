package br.com.controle.estoque.security;

import br.com.controle.estoque.auth.AppUser;
import br.com.controle.estoque.auth.AppUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final AppUserRepository userRepository;

    public CurrentUserService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AppUser getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName() == null) {
            throw new IllegalStateException("Usuário não autenticado");
        }

        String username = auth.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado: " + username));
    }

    public Long getUserId() {
        return getCurrentUser().getId();
    }

    public Long getCompanyId() {
        return getCurrentUser().getCompany().getId();
    }
}
