package br.com.controle.estoque.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    private final Key key;
    private final long accessMs;
    private final long refreshMs;
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-ms}") long accessMs,
            @Value("${app.jwt.refresh-ms}") long refreshMs

    ) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        log.info("[JWT] secret bytes length = {}", keyBytes.length);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessMs = accessMs;
        this.refreshMs = refreshMs;

    }

    public String genAccess(String username, String role) {
        return build(username, role, accessMs);
    }

    public String genRefresh(String username, String role) {
        return build(username, role, refreshMs);
    }

    private String build(String username, String role, long ttlMs) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .addClaims(Map.of("role", role))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ttlMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
}
