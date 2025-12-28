#  Estoque – Backend

Backend do **Estoque MVP**, um sistema web de controle de estoque e operações comerciais voltado para **pequenas e médias empresas**, com foco em simplicidade, rastreabilidade e controle de acesso por perfil.

O projeto foi desenvolvido com arquitetura REST, autenticação via JWT em cookies e suporte a múltiplas empresas.

---

##  Funcionalidades

- Autenticação e autorização por perfil (RBAC)
- Multiempresa (cada usuário pertence a uma empresa)
- Controle de usuários e permissões
- Cadastro de materiais
- Cadastro de clientes
- Recebimento de materiais
- Vendas
- Ajustes de estoque (entrada e saída)
- Controle de saldo por armazém e local
- Histórico e rastreabilidade de movimentações
- Migrações de banco com Flyway

---

##  Perfis de Usuário

| Perfil        | Permissões principais |
|--------------|-----------------------|
| MASTER_ADMIN | Administração global e empresas |
| GERENTE      | Acesso total à empresa |
| SUPERVISAO   | Operações + ajustes |
| LOGISTICA    | Materiais, estoque, recebimento |
| OPERADOR     | Vendas, recebimento, consultas |
| RH           | Gestão de usuários |

> As permissões são aplicadas tanto no **frontend** quanto no **backend**.

---

##  Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
- Spring Security
- JWT (em cookies HTTP Only)
- PostgreSQL
- Flyway
- JPA / Hibernate
- Maven

---

##  Arquitetura

O projeto segue uma arquitetura em camadas:

