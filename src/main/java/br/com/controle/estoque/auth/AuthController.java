package br.com.controle.estoque.auth;

import br.com.controle.estoque.security.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;


import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    @Value("${app.cookies.secure:false}")
    private boolean cookieSecure;

    @Value("${app.cookies.same-site:Lax}")
    private String cookieSameSite;

    public AuthController(AuthenticationManager authManager, JwtService jwtService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req, HttpServletResponse res) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );

        String username = auth.getName();
        String role = auth.getAuthorities()
                .iterator()
                .next()
                .getAuthority()
                .replace("ROLE_", "");

        String access = jwtService.genAccess(username, role);

        ResponseCookie accessCookie = ResponseCookie.from("ACCESS_TOKEN", access)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofHours(8))
                .build();

        res.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

        return ResponseEntity.ok(new LoginResponse(username, role));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse res) {
        ResponseCookie accessCookie = ResponseCookie.from("ACCESS_TOKEN", "")
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ZERO)
                .sameSite(cookieSameSite)
                .secure(cookieSecure)
                .build();

        res.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        if (auth == null
                || auth instanceof AnonymousAuthenticationToken
                || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "Não autenticado"));
        }

        String role = auth.getAuthorities()
                .iterator()
                .next()
                .getAuthority()
                .replace("ROLE_", "");

        return ResponseEntity.ok(Map.of(
                "username", auth.getName(),
                "role", role
        ));
    }

    public record LoginRequest(String username, String password) {}
    public record LoginResponse(String username, String role) {}
}
