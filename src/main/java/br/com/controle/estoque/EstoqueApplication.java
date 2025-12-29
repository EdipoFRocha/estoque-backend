package br.com.controle.estoque;

import br.com.controle.estoque.config.RequestIdFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.servlet.Filter;

@SpringBootApplication
public class EstoqueApplication {

    public static void main(String[] args) {
        SpringApplication.run(EstoqueApplication.class, args);
    }

    @Bean
    public Filter requestIdFilter() {
        return new RequestIdFilter();
    }

    // 🔐 TEMPORÁRIO — gerar hash da senha do admin
    @Bean
    CommandLineRunner gerarHashAdmin(PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println("=======================================");
            System.out.println("HASH_ADMIN = " + passwordEncoder.encode("Admin@123"));
            System.out.println("=======================================");
        };
    }
}
