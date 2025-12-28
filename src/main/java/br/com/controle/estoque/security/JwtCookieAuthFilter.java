package br.com.controle.estoque.security;

import br.com.controle.estoque.auth.AppUser;
import br.com.controle.estoque.auth.AppUserRepository;
import br.com.controle.estoque.model.Company;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtCookieAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AppUserRepository userRepository;

    private final String accessCookieName = "ACCESS_TOKEN";

    public JwtCookieAuthFilter(JwtService jwtService, AppUserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {

        final String p = req.getServletPath();
        System.out.println("[JWT-FILTER] " + req.getMethod() + " " + p);

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())
                || p.equals("/api/auth/login")
                || p.equals("/api/auth/logout")
                || p.equals("/api/ping")
                || p.equals("/ping")) {
            System.out.println("[JWT-FILTER] BYPASS publico");
            chain.doFilter(req, res);
            return;
        }

        String token = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (accessCookieName.equals(c.getName())) token = c.getValue();
            }
        }
        System.out.println("[JWT-FILTER] cookie presente? " + (token != null));

        if (StringUtils.hasText(token)) {
            try {
                Jws<Claims> jws = jwtService.parse(token);
                String username = jws.getBody().getSubject();
                String role = (String) jws.getBody().get("role");
                System.out.println("[JWT-FILTER] token ok para user=" + username + " role=" + role);

                Optional<AppUser> opt = userRepository.findByUsername(username);
                if (opt.isEmpty()) {
                    SecurityContextHolder.clearContext();
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.setContentType("application/json");
                    res.getWriter().write("{\"error\":\"Usuário não encontrado.\"}");
                    return;
                }

                Boolean userActive = userRepository.findUserActiveByUsername(username).orElse(true);
                if (Boolean.FALSE.equals(userActive)) {
                    SecurityContextHolder.clearContext();
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    res.setContentType("application/json");
                    res.getWriter().write("{\"error\":\"Usuário inativo.\"}");
                    return;
                }

                Optional<Boolean> companyActiveOpt = userRepository.findCompanyActiveByUsername(username);
                if (companyActiveOpt.isPresent() && Boolean.FALSE.equals(companyActiveOpt.get())) {
                    SecurityContextHolder.clearContext();
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    res.setContentType("application/json");
                    res.getWriter().write("{\"error\":\"Empresa inativa. Entre em contato com o administrador.\"}");
                    return;
                }

                var authToken = new UsernamePasswordAuthenticationToken(
                        username, null, List.of(new SimpleGrantedAuthority("ROLE_" + role))
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);

            } catch (Exception ex) {
                System.out.println("[JWT-FILTER] token inválido: " + ex.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(req, res);
    }
}
