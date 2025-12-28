package br.com.controle.estoque.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        MDC.put(START_TIME, String.valueOf(System.currentTimeMillis()));

        log.info("REQUEST {} {} user={} params={}",
                req.getMethod(),
                req.getRequestURI(),
                currentUser(),
                req.getQueryString());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ex) {
        long start = Long.parseLong(MDC.get(START_TIME));
        long elapsed = System.currentTimeMillis() - start;

        if (ex == null) {
            log.info("RESPONSE {} {} status={} time={}ms user={}",
                    req.getMethod(), req.getRequestURI(), res.getStatus(), elapsed, currentUser());
        } else {
            log.error("ERROR {} {} status={} time={}ms user={} msg={}",
                    req.getMethod(), req.getRequestURI(), res.getStatus(), elapsed, currentUser(), ex.getMessage(), ex);
        }

        MDC.remove(START_TIME);
    }

    private String currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "ANONYMOUS";
        }
        return auth.getName(); // username
    }
}
