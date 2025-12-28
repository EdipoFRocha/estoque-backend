package br.com.controle.estoque.security;

import org.springframework.http.ResponseCookie;

public class CookieUtil {

    public static ResponseCookie httpOnlyCookie(String name, String value, long maxAgeSeconds, String path) {
        String p = (path == null || path.isBlank()) ? "/" : path;
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(false) // HTTPS -> true
                .path(p)
                .maxAge(maxAgeSeconds)
                .sameSite("Lax")
                .build();
    }

    public static ResponseCookie clear(String name, String path) {
        String p = (path == null || path.isBlank()) ? "/" : path;
        return ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(false)
                .path(p)
                .maxAge(0)
                .sameSite("Lax")
                .build();
    }
}
