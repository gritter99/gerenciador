# MVP - Gerenciador Financeiro PJ
## Versão Simplificada para 1 Semana

**Objetivo:** Criar um MVP funcional em 7 dias para treinar Java 21, Quarkus e Angular.

---

## 🎯 Escopo do MVP

### Funcionalidades Mínimas (Must Have)

1. **Autenticação Básica**
   - Cadastro de usuário (nome, email, senha)
   - Login (email, senha)
   - Token JWT simples

2. **CRUD de Transações**
   - Cadastrar receita (data, valor, descrição)
   - Cadastrar despesa (data, valor, descrição)
   - Listar transações
   - Excluir transação

3. **Dashboard Mínimo**
   - Total de receitas
   - Total de despesas
   - Saldo (receitas - despesas)

### Fora do Escopo (Nice to Have - Depois)

- Edição de transações
- Filtros e busca
- Validações complexas
- Recuperação de senha
- Interface polida
- Deploy em produção

---

## 📅 Cronograma de 1 Semana

### Dia 1: Setup e Backend Básico
**Objetivo:** Ter o backend Quarkus rodando com endpoints básicos

**Tarefas:**
- [ ] Criar projeto Quarkus (via code.quarkus.io)
- [ ] Configurar dependências: REST, Hibernate Panache, PostgreSQL Driver, JWT
- [ ] Criar entidade `Usuario` (id, nome, email, senha_hash)
- [ ] Criar entidade `Transacao` (id, tipo, data, valor, descricao, usuario_id)
- [ ] Configurar banco H2 (desenvolvimento) ou PostgreSQL local
- [ ] Criar repository básico para Usuario e Transacao
- [ ] Testar endpoints com Postman/Insomnia (sem autenticação ainda)

**Entregável:** Backend rodando com endpoints GET/POST funcionais

---

### Dia 2: Autenticação JWT
**Objetivo:** Implementar login e proteção de rotas

**Tarefas:**
- [ ] Instalar SmallRye JWT no Quarkus
- [ ] Criar endpoint `/auth/register` (cadastro)
- [ ] Criar endpoint `/auth/login` (retorna JWT)
- [ ] Implementar hash de senha (bcrypt)
- [ ] Criar filtro/interceptor para validar JWT
- [ ] Proteger endpoints de transações com JWT
- [ ] Testar fluxo completo: cadastro → login → acesso protegido

**Entregável:** Autenticação funcionando end-to-end

---

### Dia 3: CRUD de Transações
**Objetivo:** Completar operações de transações

**Tarefas:**
- [ ] Endpoint `POST /transacoes` (criar receita/despesa)
- [ ] Endpoint `GET /transacoes` (listar do usuário logado)
- [ ] Endpoint `DELETE /transacoes/{id}` (excluir)
- [ ] Validar que usuário só vê suas transações
- [ ] Validações básicas (valor > 0, campos obrigatórios)
- [ ] Testar todos os endpoints

**Entregável:** CRUD completo de transações funcionando

---

### Dia 4: Frontend Angular - Setup e Autenticação
**Objetivo:** Ter o frontend Angular rodando com login

**Tarefas:**
- [ ] Criar projeto Angular (`ng new`)
- [ ] Instalar dependências: Angular Material (opcional) ou Bootstrap
- [ ] Configurar HTTP Client
- [ ] Criar serviço `AuthService` (register, login, logout)
- [ ] Criar componente `LoginComponent`
- [ ] Criar componente `RegisterComponent`
- [ ] Configurar interceptor para adicionar JWT nas requisições
- [ ] Criar guard de rota (AuthGuard)
- [ ] Configurar roteamento básico
- [ ] Testar login e redirecionamento

**Entregável:** Frontend com autenticação funcionando

---

### Dia 5: Frontend - CRUD de Transações
**Objetivo:** Interface para gerenciar transações

**Tarefas:**
- [ ] Criar serviço `TransacaoService` (CRUD)
- [ ] Criar componente `TransacoesListComponent` (listagem)
- [ ] Criar componente `TransacaoFormComponent` (cadastro)
- [ ] Criar modelo TypeScript para Transacao
- [ ] Implementar formulário de receita/despesa
- [ ] Implementar tabela de listagem
- [ ] Implementar botão de exclusão
- [ ] Conectar com backend
- [ ] Testar fluxo completo

**Entregável:** Interface completa de transações funcionando

---

### Dia 6: Dashboard e Integração
**Objetivo:** Dashboard funcional e integração completa

**Tarefas:**
- [ ] Criar endpoint `GET /dashboard` no backend (retorna totais)
- [ ] Criar componente `DashboardComponent`
- [ ] Exibir cards com: Total Receitas, Total Despesas, Saldo
- [ ] Melhorar layout básico (navegação, header)
- [ ] Adicionar tratamento de erros básico
- [ ] Testar integração completa end-to-end
- [ ] Ajustes de UX básicos

**Entregável:** Sistema completo funcionando

---

### Dia 7: Testes, Ajustes e Documentação
**Objetivo:** Polir e documentar

**Tarefas:**
- [ ] Testar todos os fluxos principais
- [ ] Corrigir bugs encontrados
- [ ] Adicionar mensagens de feedback (sucesso/erro)
- [ ] Melhorar validações de formulário
- [ ] Criar README.md com instruções de setup
- [ ] Documentar endpoints da API
- [ ] Preparar apresentação/demo

**Entregável:** MVP funcional e documentado

---

## 🛠️ Stack Técnica Simplificada

### Backend
```xml
<!-- Dependências Quarkus essenciais -->
- quarkus-resteasy-reactive
- quarkus-hibernate-orm-panache
- quarkus-jdbc-postgresql (ou h2)
- quarkus-smallrye-jwt
- quarkus-smallrye-jwt-build
- quarkus-arc (CDI)
```

### Frontend
```json
// Dependências Angular essenciais
- @angular/core
- @angular/router
- @angular/common/http
- @angular/forms
```

### Banco de Dados
- **Desenvolvimento:** H2 (em memória) - mais rápido para começar
- **Produção (futuro):** PostgreSQL

---

## 📋 Checklist Rápido de Implementação

### Backend (Quarkus)

#### Estrutura de Pastas
```
src/main/java/com/gerenciador/
├── entity/
│   ├── Usuario.java
│   └── Transacao.java
├── repository/
│   ├── UsuarioRepository.java
│   └── TransacaoRepository.java
├── resource/
│   ├── AuthResource.java
│   ├── TransacaoResource.java
│   └── DashboardResource.java
├── service/
│   └── JwtService.java
└── security/
    └── JwtSecurityFilter.java
```

#### Endpoints Mínimos
```
POST   /auth/register     - Cadastrar usuário
POST   /auth/login        - Login (retorna JWT)
GET    /transacoes        - Listar transações do usuário
POST   /transacoes        - Criar transação
DELETE /transacoes/{id}   - Excluir transação
GET    /dashboard         - Obter totais
```

### Frontend (Angular)

#### Estrutura de Pastas
```
src/app/
├── components/
│   ├── login/
│   ├── register/
│   ├── dashboard/
│   ├── transacoes/
│   └── transacao-form/
├── services/
│   ├── auth.service.ts
│   └── transacao.service.ts
├── guards/
│   └── auth.guard.ts
├── models/
│   └── transacao.model.ts
└── app-routing.module.ts
```

#### Rotas Mínimas
```
/login          - Tela de login
/register       - Tela de cadastro
/dashboard      - Dashboard (protegida)
/transacoes     - Lista de transações (protegida)
```

---

## 🎨 Interface Mínima

### Tela de Login
- Campo email
- Campo senha
- Botão "Entrar"
- Link "Não tem conta? Cadastre-se"

### Dashboard
- Card: Total Receitas (verde)
- Card: Total Despesas (vermelho)
- Card: Saldo (azul)
- Botão "Nova Receita"
- Botão "Nova Despesa"
- Link para lista de transações

### Lista de Transações
- Tabela com: Data, Tipo, Valor, Descrição
- Botão "Excluir" em cada linha
- Botão "Nova Transação"

### Formulário de Transação
- Campo Data (date picker)
- Campo Valor (number)
- Campo Descrição (text)
- Radio/Toggle: Receita / Despesa
- Botão "Salvar"
- Botão "Cancelar"

---

## 🚀 Setup Rápido

### Backend (Quarkus)
```bash
# Criar projeto
curl https://code.quarkus.io/api/download \
  -d groupId=com.gerenciador \
  -d artifactId=gerenciador-backend \
  -d buildTool=maven \
  -d extensions=resteasy-reactive,hibernate-orm-panache,jdbc-h2,smallrye-jwt

# Rodar
cd gerenciador-backend
./mvnw quarkus:dev
```

### Frontend (Angular)
```bash
# Criar projeto
ng new gerenciador-frontend
cd gerenciador-frontend

# Rodar
ng serve
```

### Banco de Dados
```properties
# application.properties (Quarkus)
quarkus.datasource.db-kind=h2
quarkus.datasource.jdbc.url=jdbc:h2:mem:gerenciador
quarkus.hibernate-orm.database.generation=drop-and-create
```

---

## ✅ Critérios de Sucesso do MVP

O MVP será considerado completo quando:

1. ✅ Usuário consegue se cadastrar
2. ✅ Usuário consegue fazer login
3. ✅ Usuário consegue cadastrar receita
4. ✅ Usuário consegue cadastrar despesa
5. ✅ Usuário consegue ver lista de transações
6. ✅ Usuário consegue excluir transação
7. ✅ Dashboard mostra totais corretos
8. ✅ Sistema funciona end-to-end sem erros críticos

---

## 📝 Notas Importantes

### Priorização
- **Dia 1-3:** Foco total no backend funcionando
- **Dia 4-5:** Foco total no frontend funcionando
- **Dia 6:** Integração e dashboard
- **Dia 7:** Polimento e documentação

### Dicas
- Use H2 em memória para desenvolvimento (mais rápido)
- Não se preocupe com design bonito (foco em funcionalidade)
- Validações básicas apenas (não precisa ser perfeito)
- Teste manualmente (não precisa de testes automatizados no MVP)
- Documente conforme avança (não deixe tudo para o final)

---

## 🎓 Objetivos de Aprendizado

1. **Quarkus:**
   - Criar APIs REST
   - Integrar com banco de dados
   - Implementar autenticação JWT
   - Validações básicas

2. **Angular:**
   - Componentes e serviços
   - Roteamento e guards
   - HTTP Client e interceptors
   - Formulários

3. **Integração:**
   - Backend + Frontend funcionando juntos
   - Autenticação end-to-end
   - CRUD completo

---

