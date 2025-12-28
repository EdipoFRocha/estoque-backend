package br.com.controle.estoque;

import br.com.controle.estoque.config.RequestIdFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
}
