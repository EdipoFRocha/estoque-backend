Controle de Estoque – Backend

Backend do Sistema de Controle de Estoque, uma aplicação web desenvolvida para atender pequenas e médias empresas, oferecendo controle de estoque, operações comerciais e gestão de usuários com segurança, rastreabilidade e arquitetura escalável.
O sistema foi projetado para operar em ambiente real, com autenticação segura, separação por empresa (multi-tenant lógico) e controle de acesso por perfil.

Objetivo do Sistema
Substituir controles manuais e planilhas por um sistema centralizado que permita:
• Organização dos processos de estoque
• Redução de erros operacionais
• Rastreabilidade completa das movimentações
• Controle de acesso conforme responsabilidade do usuário

Funcionalidades

• Autenticação e autorização baseada em perfis (RBAC)
• Arquitetura multiempresa (cada usuário pertence a uma empresa)
• Gestão de usuários e permissões
• Cadastro de materiais
• Cadastro de clientes
• Recebimento de materiais
• Vendas e devoluções
• Ajustes de estoque (entrada e saída)
• Controle de saldo por armazém e local
• Histórico completo de movimentações
• Migrações versionadas de banco de dados com Flyway

Perfis de Usuário
Perfil          Permissões principais
MASTER_ADMIN	  Administração global e empresas
GERENTE        	Acesso total à empresa
SUPERVISAO    	Operações e ajustes
LOGISTICA	      Materiais, estoque e recebimento
OPERADOR      	Vendas, recebimento e consultas
RH	            Gestão de usuários

As permissões são aplicadas tanto no backend quanto no frontend.

Tecnologias Utilizadas
• Java 17
• Spring Boot
• Spring Security
• JWT (cookies HTTP Only)
• PostgreSQL
• Flyway
• JPA / Hibernate
• Maven

Arquitetura
O projeto segue uma arquitetura REST em camadas:
Controller → Service → Repository → Database

Camadas
• Controller: exposição dos endpoints REST
• Service: regras de negócio e validações
• Repository: acesso a dados via JPA
• Security: autenticação, autorização e filtros JWT
• DTOs: separação entre modelo interno e dados expostos

Essa abordagem garante manutenibilidade, segurança e escalabilidade.

Segurança
• Autenticação via JWT armazenado em cookies HTTP Only
• Filtros de segurança centralizados no Spring Security
• Proteção de rotas por perfil de usuário
• Isolamento de dados por empresa

Configuração do Ambiente
Pré-requisitos
• Java 17+
• Maven
• PostgreSQL
• Variáveis de Ambiente

Este projeto não versiona dados sensíveis.

Utilize um arquivo de configuração local baseado no exemplo:

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/estoque_mvp
    username: postgres
    password: SUA_SENHA

app:
  jwt:
    secret: SUA_CHAVE_SECRETA

Status do Projeto
• Backend estável
• Em produção
• Melhorias planejadas (relatórios, performance e novos módulos)

Autor
Édipo Ferreira da Rocha
Graduando em Engenharia da Computação
GitHub : https://github.com/EdipoFRocha
LinkedIn : linkedin.com/in/edipo-ferreira90021511
