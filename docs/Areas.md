# Plano por Área - Gerenciador Financeiro PJ

## Objetivo
Documentar, de forma prática, o que fazer em cada área (backend, frontend e segurança) para entregar o MVP descrito em `docs/MVP.md` e alinhado aos requisitos de `docs/SRS.md`.

---

## Visão Geral
- Stack: Quarkus (Java 21) com RESTEasy Reactive, Hibernate Panache, SmallRye JWT; banco H2 (dev) / PostgreSQL (prod). Frontend em Angular; UI opcional com Angular Material ou Bootstrap.
- Escopo MVP: cadastro/login com JWT, CRUD básico de transações (criar/listar/excluir), dashboard com totais.

---

## Backend (Quarkus)

### Setup e Infra
- Criar projeto Quarkus (code.quarkus.io) com extensões: `resteasy-reactive`, `hibernate-orm-panache`, `jdbc-h2` (dev) / `jdbc-postgresql` (prod), `smallrye-jwt`, `smallrye-jwt-build`, `arc`.
- Configurar `application.properties`: perfil dev com H2 em memória; prod com PostgreSQL. Em dev, `quarkus.hibernate-orm.database.generation=drop-and-create`.
- Usar Maven Wrapper incluso no projeto gerado.

### Modelagem e Persistência
- Entidades mínimas (MVP): `Usuario {id, nome, email único, senhaHash}`, `Transacao {id, tipo=RECEITA|DESPESA, data, valor, descricao, usuarioId}`.
- Se alinhar ao SRS completo: adicionar `Empresa {cnpj único, razaoSocial, ativo}` e `Categoria {nome único por empresa, ativo}`; relacionar `Usuario` e `Transacao` a `Empresa`, e `Transacao` a `Categoria`.
- Repositories Panache: `UsuarioRepository`, `TransacaoRepository` (e `EmpresaRepository`, `CategoriaRepository` se aplicável).
- Constraints: email único; (opcional) cnpj único; categoria única por empresa; índices para `usuarioId`/`empresaId`.

### Autenticação e Autorização
- Endpoints: `POST /auth/register`, `POST /auth/login`.
- Hash de senha: bcrypt (ex.: `BCrypt` do Quarkus/SmallRye).
- JWT: gerar token com `userId` (e `empresaId` se multi-tenant), expiração configurada; assinar com chave segura via variável de ambiente (ou Secrets Manager em produção).
- Filtro/Interceptor: validar JWT em rotas protegidas e injetar contexto de usuário (e empresa) para filtros.

### CRUD de Transações
- Rotas mínimas: `POST /transacoes`, `GET /transacoes`, `DELETE /transacoes/{id}`.
- Regras: valor > 0; (opcional, para SRS) data não futura; transação sempre vinculada ao usuário (e empresa) do token; somente o dono pode ver/excluir.
- Paginação simples no GET (page/size).

### Dashboard
- Rota: `GET /dashboard` retornando totais (receitas, despesas, saldo) filtrados por usuário/empresa.
- Opcional: retornar últimas transações para exibir na home.

### Validações e Dados
- Bean Validation: campos obrigatórios, email válido, valor positivo, (opcional) data não futura.
- Isolamento: sempre filtrar por `usuarioId` (e `empresaId` se multi-tenant).
- Segurança de dados: usar JPA/Parâmetros (protege contra SQL Injection).

### Testes e Documentação
- Testes manuais via Insomnia/Postman: registro, login, criar/listar/excluir transação, dashboard.
- Documentar endpoints em `docs/api.md` ou README (paths, payloads, respostas, erros).

---

## Frontend (Angular)

### Setup
- Criar projeto Angular (`ng new`). Dependências: `@angular/router`, `@angular/common/http`, `@angular/forms`. UI opcional: Angular Material ou Bootstrap.
- Estrutura sugerida: `services/`, `components/login/`, `components/register/`, `components/dashboard/`, `components/transacoes/`, `components/transacao-form/`, `guards/auth.guard.ts`, `models/transacao.model.ts`.

### Autenticação
- `AuthService`: métodos `register`, `login`, `logout`; armazenar token (MVP: `localStorage`; produção: considerar cookie httpOnly).
- Interceptor HTTP: anexar `Authorization: Bearer <token>`.
- `AuthGuard`: proteger rotas `/dashboard` e `/transacoes`.
- Telas: `LoginComponent`, `RegisterComponent` com formulários reativos e validação básica.

### Transações
- `TransacaoService`: `listar`, `criar`, `excluir` (e `editar` se evoluir).
- Componentes:
  - `TransacoesListComponent`: tabela com data, tipo, valor, descrição; botão excluir; estados de carregamento/erro.
  - `TransacaoFormComponent`: formulário com data (date), valor (number), descrição (text), tipo (radio/segmented receita|despesa); feedback de sucesso/erro.
- Model: `Transacao { id?, tipo, data, valor, descricao }`.

### Dashboard
- `DashboardComponent`: cards para total receitas, total despesas, saldo; opcional lista curta de últimas transações.

### UX e Validação
- Formulários reativos: campos obrigatórios, valor > 0, email válido.
- Feedback: toasts/snackbars para sucesso/erro; spinners em carregamentos.
- Responsividade básica (grid simples; Material/Bootstrap ajudam).

---

## Segurança (Cross-cutting)

### Autenticação e Tokens
- JWT com expiração; algoritmo HS256 ou RS256; chave secreta fora do código (env/Secrets Manager em prod).
- Logout: limpar token no cliente; blacklist curta no backend apenas se necessário (opcional no MVP).

### Proteção de Dados e Acesso
- Isolamento por usuário/empresa em todas as queries.
- Não logar dados sensíveis (senhas, tokens completos).
- CORS: em produção, restringir ao domínio do frontend.

### Input/Output Safety
- Backend: Bean Validation em DTOs; JPA com parâmetros (previne SQL Injection).
- Frontend: escapar/sanitizar ao renderizar; evitar `innerHTML`; CSP em produção (opcional).
- Regras de negócio: valor > 0; (opcional) data não futura; validação também no frontend.

### Senhas
- Bcrypt com cost adequado (ex.: 10–12); nunca armazenar em texto plano.

### API Hardening
- Rate limiting básico (principalmente login) via infra (NGINX/API Gateway) ou lib.
- Erros genéricos para autenticação (não revelar se email existe).
- HTTPS obrigatório em produção; redirecionar HTTP → HTTPS.

### Logs e Auditoria
- Registrar ações críticas: login (sucesso/falha), criação/remoção de transação, cadastro de usuário; incluir timestamp e userId; omitir payloads sensíveis.

### Segredos e Infra (Produção)
- Secrets em variáveis de ambiente ou Secrets Manager.
- DB em subnet privada; TLS para DB; backups automáticos (RDS).
- Monitoramento básico (CloudWatch) se houver deploy em AWS.

---

## Critérios de Pronto (Definition of Done)

### Backend
- Endpoints funcionando: `/auth/register`, `/auth/login`, `/transacoes` (POST/GET/DELETE), `/dashboard`.
- JWT validado em rotas protegidas; senhas com bcrypt; filtros por usuário (e empresa, se aplicável).
- Validações ativas; documentação básica dos endpoints.

### Frontend
- Fluxo: registro → login → dashboard → criar/listar/excluir transações, com feedback e rotas protegidas.
- Interceptor de JWT e guard ativos; formulários com validação; UI responsiva básica.

### Segurança
- JWT com expiração e chave fora do código; bcrypt; CORS restrito em prod; validação de entrada; isolamento de dados; logs mínimos de auditoria; plano de HTTPS em produção.

