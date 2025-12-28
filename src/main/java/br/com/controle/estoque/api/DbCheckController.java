package br.com.controle.estoque.api;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbCheckController {
    private final JdbcTemplate jdbc;
    public DbCheckController(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @GetMapping("/dbcheck")
    public String dbcheck() {
        String v = jdbc.queryForObject("select version()", String.class);
        return "OK - " + v;
    }
}
