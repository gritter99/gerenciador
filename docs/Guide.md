# Guia de Início - Gerenciador Financeiro PJ

## Visão Geral da Equipe

**Integrantes:** Backend Developer, Frontend Developer, Segurança Developer  
**Metodologia:** Trabalho conjunto com revisões cruzadas  
**GitFlow:** `main` → `dev` → `feature/*`

---

## Pré-requisitos

### Backend Developer
- JDK 21 instalado
- Maven 3.8+ ou usar Maven Wrapper (incluído no projeto Quarkus)
- IDE (IntelliJ IDEA, VS Code com extensões Java, ou Eclipse)
- Postman ou Insomnia para testes de API

### Frontend Developer
- Node.js 18+ e npm
- Angular CLI: `npm install -g @angular/cli`
- IDE (VS Code recomendado com extensões Angular)

### Segurança Developer
- Mesmos pré-requisitos do Backend
- Conhecimento de JWT, bcrypt, OWASP Top 10
- Ferramentas: Postman/Insomnia para testes de segurança

---

## Sprint 1: Setup e Autenticação (Dias 1-2)

### Objetivo
Ter autenticação funcionando end-to-end (cadastro + login com JWT).

---

### Tarefas Backend Developer

#### 1. Criar Projeto Quarkus

**Direcionamento:**
- Acessar https://code.quarkus.io
- Preencher: Group `com.gerenciador`, Artifact `gerenciador-backend`, Build tool `Maven`, Java `21`
- Selecionar extensões essenciais:
  - RESTEasy Reactive
  - Hibernate ORM with Panache
  - JDBC Driver - H2 (para desenvolvimento)
  - SmallRye JWT
  - SmallRye JWT Build
  - Bean Validation
- Baixar ZIP e extrair na pasta `backend/`

#### 2. Configurar application.properties

**Direcionamento:**
- Criar/editar `backend/src/main/resources/application.properties`
- Configurar banco H2 para desenvolvimento (em memória)
- Configurar JWT (chaves serão geradas por Segurança)
- Configurar CORS para permitir `http://localhost:4200` (frontend)

**Configurações necessárias:**
- Database: H2 em memória, `drop-and-create` para desenvolvimento
- JWT: paths para chaves pública/privada (serão criadas por Segurança)
- CORS: habilitado para desenvolvimento local

#### 3. Criar Entidade Usuario

**Direcionamento:**
- Criar `backend/src/main/java/com/gerenciador/entity/Usuario.java`
- Campos: `id` (herdado de PanacheEntity), `nome`, `email` (único), `senhaHash`
- Adicionar validações Bean Validation:
  - Nome: obrigatório, mínimo 3 caracteres
  - Email: obrigatório, formato válido, único no banco
  - SenhaHash: obrigatório, mínimo 8 caracteres (será hash, não senha em texto)

#### 4. Criar Repository

**Direcionamento:**
- Criar `backend/src/main/java/com/gerenciador/repository/UsuarioRepository.java`
- Implementar `PanacheRepository<Usuario>`
- Adicionar método `findByEmail(String email)` para buscar por email

#### 5. Criar DTOs

**Direcionamento:**
- Criar `RegisterRequest.java`: nome, email, senha (com validações)
- Criar `LoginRequest.java`: email, senha (com validações)
- Criar `AuthResponse.java`: token (String), usuario (objeto com id, nome, email)

**Validações necessárias:**
- Nome: obrigatório, mínimo 3 caracteres
- Email: obrigatório, formato válido
- Senha: obrigatório, mínimo 8 caracteres

#### 6. Criar Endpoint de Registro (estrutura básica)

**Direcionamento:**
- Criar `backend/src/main/java/com/gerenciador/resource/AuthResource.java`
- Endpoint `POST /auth/register`:
  - Validar DTO de entrada
  - Verificar se email já existe (retornar 409 se existir)
  - Criar usuário (por enquanto, armazenar senha em texto - será substituído por hash)
  - Retornar resposta com token temporário (será substituído por JWT real)
- Endpoint `POST /auth/login`:
  - Validar DTO de entrada
  - Buscar usuário por email
  - Verificar senha (por enquanto comparação simples - será substituído por bcrypt)
  - Retornar resposta com token temporário

**Nota:** Os endpoints devem funcionar, mas ainda sem segurança real. Segurança implementará hash e JWT depois.

#### 7. Testar Endpoints

**Direcionamento:**
- Rodar aplicação: `cd backend && ./mvnw quarkus:dev`
- Testar com Postman/Insomnia:
  - POST `/auth/register` com JSON válido
  - POST `/auth/login` com credenciais cadastradas
  - Verificar respostas HTTP corretas

**Checklist Backend - Sprint 1:**
- [ ] Projeto Quarkus criado e rodando
- [ ] Entidade Usuario criada com validações
- [ ] Repository criado com método findByEmail
- [ ] DTOs criados (RegisterRequest, LoginRequest, AuthResponse)
- [ ] Endpoint /auth/register funcionando (estrutura básica)
- [ ] Endpoint /auth/login funcionando (estrutura básica)
- [ ] Testes manuais realizados com Postman/Insomnia

---

### Tarefas Segurança Developer

#### 1. Adicionar Dependência Bcrypt

**Direcionamento:**
- Editar `backend/pom.xml`
- Adicionar dependência `jbcrypt` (versão 0.4 ou superior)
- Rodar `./mvnw clean install` para baixar dependências

#### 2. Criar Serviço de Hash de Senha

**Direcionamento:**
- Criar `backend/src/main/java/com/gerenciador/service/PasswordService.java`
- Implementar método `hash(String senha)` usando BCrypt
- Implementar método `verify(String senha, String hash)` para verificar senha
- Usar cost factor de 12 (balance entre segurança e performance)

**Funcionalidades:**
- Hash de senha com salt automático
- Verificação de senha contra hash armazenado

#### 3. Gerar Chaves JWT

**Direcionamento:**
- Criar diretório `backend/src/main/resources/` se não existir
- Gerar chave privada RSA 2048 bits: `openssl genrsa -out privateKey.pem 2048`
- Gerar chave pública: `openssl rsa -in privateKey.pem -pubout -out publicKey.pem`
- Adicionar `*.pem` ao `.gitignore` (não commitar chaves)

**Nota:** Em produção, usar AWS Secrets Manager ou variáveis de ambiente.

#### 4. Criar Serviço JWT

**Direcionamento:**
- Criar `backend/src/main/java/com/gerenciador/service/JwtService.java`
- Implementar método `generateToken(Long userId, String email)`:
  - Ler chave privada do arquivo
  - Criar claims: issuer, subject (userId), email, userId, groups
  - Definir expiração (ex: 24 horas)
  - Assinar token com chave privada
  - Retornar token como String

**Claims necessários no token:**
- `sub`: userId
- `email`: email do usuário
- `userId`: id do usuário (para facilitar acesso)
- `groups`: ["user"] (para autorização)

#### 5. Criar Filtro de Segurança JWT

**Direcionamento:**
- Criar `backend/src/main/java/com/gerenciador/security/JwtSecurityFilter.java`
- Implementar `ContainerRequestFilter`
- Extrair token do header `Authorization: Bearer <token>`
- Validar presença do token (retornar 401 se ausente)
- Validação automática do token será feita pelo Quarkus (configurado no application.properties)

#### 6. Integrar com Backend

**Direcionamento:**
- Atualizar `AuthResource.java`:
  - Injetar `PasswordService` e `JwtService`
  - No método `register`: usar `passwordService.hash()` antes de salvar
  - No método `login`: usar `passwordService.verify()` para comparar senha
  - Em ambos: usar `jwtService.generateToken()` para gerar token real
- Remover código temporário (senha em texto, token fake)

#### 7. Proteger Rotas

**Direcionamento:**
- Adicionar anotação `@RolesAllowed("user")` nos endpoints que precisam autenticação
- Testar que rotas protegidas retornam 401 sem token
- Testar que rotas protegidas funcionam com token válido

**Checklist Segurança - Sprint 1:**
- [ ] Dependência bcrypt adicionada
- [ ] PasswordService implementado e testado
- [ ] Chaves JWT geradas (privada e pública)
- [ ] JwtService implementado
- [ ] JwtSecurityFilter criado
- [ ] Integração com AuthResource concluída
- [ ] Rotas protegidas funcionando
- [ ] Testes de segurança realizados (hash funciona, JWT válido)

---

### Integração Backend + Segurança

**Reunião de Integração:**
1. Backend faz merge da branch `feature/security-jwt` na sua branch
2. Backend atualiza `AuthResource` para usar serviços de Segurança
3. Testes conjuntos: cadastro → login → validação de token
4. Criar PR conjunto: `feature/backend-auth` → `dev`

**Teste End-to-End:**
1. Registrar usuário via POST `/auth/register`
   - Verificar que senha está hasheada no banco
   - Verificar que resposta contém token JWT válido
2. Fazer login via POST `/auth/login`
   - Verificar que token JWT é gerado
   - Verificar que token contém claims corretos
3. Usar token em requisição protegida
   - Fazer GET em endpoint protegido com header `Authorization: Bearer <token>`
   - Verificar que acesso é permitido
   - Testar sem token (deve retornar 401)

---

## Sprint 2: CRUD de Transações (Dia 3)

### Tarefas Backend Developer

#### 1. Criar Entidade Transacao

**Direcionamento:**
- Criar `backend/src/main/java/com/gerenciador/entity/Transacao.java`
- Campos: `id`, `tipo` (enum: RECEITA/DESPESA), `data` (LocalDate), `valor` (BigDecimal), `descricao` (String), `usuario` (ManyToOne com Usuario)
- Validações:
  - Tipo: obrigatório
  - Data: obrigatória
  - Valor: obrigatório, mínimo 0.01
  - Descrição: obrigatória, máximo 255 caracteres

#### 2. Criar Repository

**Direcionamento:**
- Criar `backend/src/main/java/com/gerenciador/repository/TransacaoRepository.java`
- Implementar `PanacheRepository<Transacao>`
- Adicionar método `findByUsuarioId(Long usuarioId)` para filtrar por usuário

#### 3. Criar DTOs

**Direcionamento:**
- Criar `TransacaoRequest.java`: tipo, data, valor, descricao (com validações)
- Criar `TransacaoResponse.java` (opcional): id, tipo, data, valor, descricao

#### 4. Criar Endpoints CRUD

**Direcionamento:**
- Criar `backend/src/main/java/com/gerenciador/resource/TransacaoResource.java`
- Endpoint `POST /transacoes`:
  - Validar DTO de entrada
  - Extrair userId do token JWT
  - Buscar usuário no banco
  - Criar transação vinculada ao usuário
  - Retornar 201 Created com transação criada
- Endpoint `GET /transacoes`:
  - Extrair userId do token JWT
  - Buscar apenas transações do usuário logado
  - Retornar lista de transações
- Endpoint `DELETE /transacoes/{id}`:
  - Extrair userId do token JWT
  - Buscar transação por id
  - Validar que transação pertence ao usuário logado
  - Excluir transação
  - Retornar 204 No Content

**Importante:** Sempre filtrar por `usuarioId` do token para garantir isolamento de dados.

#### 5. Testar Endpoints

**Direcionamento:**
- Testar criação de transação (receita e despesa)
- Testar listagem (deve retornar apenas transações do usuário logado)
- Testar exclusão (deve validar propriedade)
- Testar tentativa de acessar transação de outro usuário (deve retornar 404)

**Checklist Backend - Sprint 2:**
- [ ] Entidade Transacao criada com validações
- [ ] Repository criado com método findByUsuarioId
- [ ] DTOs criados
- [ ] Endpoints CRUD implementados (POST, GET, DELETE)
- [ ] Isolamento por usuário funcionando
- [ ] Validações aplicadas
- [ ] Testes manuais realizados

---

### Tarefas Segurança Developer

#### 1. Revisar Isolamento de Dados

**Direcionamento:**
- Revisar todos os endpoints de transações
- Garantir que todas as queries filtram por `usuarioId` do token
- Testar tentativas de acesso a dados de outros usuários
- Validar que não há vazamento de dados entre usuários

#### 2. Adicionar Validações Adicionais

**Direcionamento:**
- Validar que data não pode ser futura (se necessário conforme SRS)
- Validar que usuário só pode excluir próprias transações
- Adicionar validações de negócio conforme regras do SRS

#### 3. Implementar Logs de Auditoria

**Direcionamento:**
- Criar `backend/src/main/java/com/gerenciador/service/AuditService.java`
- Implementar métodos para logar:
  - Criação de transação (userId, transacaoId, timestamp)
  - Exclusão de transação (userId, transacaoId, timestamp)
  - Login (userId, timestamp, sucesso/falha)
- Usar Logger do Quarkus (org.jboss.logging.Logger)
- Integrar logs nos endpoints relevantes

**Checklist Segurança - Sprint 2:**
- [ ] Isolamento de dados validado e testado
- [ ] Validações adicionais implementadas
- [ ] Logs de auditoria funcionando
- [ ] Testes de segurança realizados (tentativas de acesso não autorizado)

---

## Sprint 3: Dashboard (Dia 4)

### Tarefas Backend Developer

#### 1. Criar Endpoint Dashboard

**Direcionamento:**
- Criar `backend/src/main/java/com/gerenciador/resource/DashboardResource.java`
- Endpoint `GET /dashboard`:
  - Extrair userId do token JWT
  - Buscar todas as transações do usuário
  - Calcular total de receitas (soma de valores tipo RECEITA)
  - Calcular total de despesas (soma de valores tipo DESPESA)
  - Calcular saldo (receitas - despesas)
  - Retornar objeto JSON com os três valores

**Estrutura de resposta:**
```json
{
  "totalReceitas": 1000.00,
  "totalDespesas": 500.00,
  "saldo": 500.00
}
```

#### 2. Testar Endpoint

**Direcionamento:**
- Criar algumas transações de teste (receitas e despesas)
- Chamar endpoint `/dashboard`
- Verificar que cálculos estão corretos
- Verificar que retorna apenas dados do usuário logado

**Checklist Backend - Sprint 3:**
- [ ] Endpoint /dashboard criado
- [ ] Cálculos corretos (receitas - despesas = saldo)
- [ ] Filtro por usuário funcionando
- [ ] Testes manuais realizados

---

## Checklist Geral de Integração

### Antes de Fazer Merge em `dev`

- [ ] Todos os testes manuais passando
- [ ] Código revisado por Segurança
- [ ] Documentação de endpoints atualizada
- [ ] Sem dados sensíveis no código (secrets em env)
- [ ] Logs de auditoria funcionando
- [ ] Isolamento de dados validado
- [ ] Validações de entrada aplicadas
- [ ] Tratamento de erros adequado

---

## Próximos Passos

Após concluir Sprint 1-3:
1. **Frontend Developer** inicia desenvolvimento (Sprint 4)
2. Integração Backend + Frontend (Sprint 5)
3. Testes end-to-end completos (Sprint 6)
4. Documentação final (Sprint 7)

---

## Comandos Úteis

```bash
# Backend - Rodar em desenvolvimento
cd backend
./mvnw quarkus:dev

# Backend - Compilar
./mvnw clean package

# Backend - Rodar testes (quando implementados)
./mvnw test

# Frontend - Criar projeto (quando iniciar)
ng new gerenciador-frontend
cd gerenciador-frontend
ng serve

# Git - Criar branch de feature
git checkout -b feature/backend-auth
git checkout -b feature/security-jwt
git checkout -b feature/frontend-setup

# Git - Workflow básico
git checkout dev
git pull origin dev
git checkout -b feature/nome-da-feature
# ... fazer alterações ...
git add .
git commit -m "feat: descrição da feature"
git push origin feature/nome-da-feature
# Criar PR no GitHub/GitLab
```

---

## Dicas Importantes

### Para Backend Developer
- Sempre validar entrada com Bean Validation
- Sempre filtrar por `usuarioId` do token em queries
- Usar transações (`@Transactional`) em operações de escrita
- Retornar status HTTP adequados (201 Created, 204 No Content, etc.)
- Tratar erros com mensagens claras (sem expor detalhes internos)

### Para Segurança Developer
- Revisar código de Backend e Frontend focando em vulnerabilidades
- Testar cenários de ataque (SQL Injection, XSS, acesso não autorizado)
- Validar que secrets não estão no código
- Verificar que logs não expõem dados sensíveis
- Garantir que CORS está configurado corretamente

### Para Frontend Developer (quando iniciar)
- Validar formulários antes de enviar
- Armazenar token de forma segura (localStorage no MVP, cookie httpOnly em produção)
- Tratar erros da API adequadamente
- Não expor tokens em logs ou console
- Implementar feedback visual para todas as ações

---

## Contatos e Suporte

- **Backend:** [nome/email]
- **Frontend:** [nome/email]
- **Segurança:** [nome/email]

**Canal de comunicação:** [Slack/Discord/Teams]

---

## Referências

- Documentação Quarkus: https://quarkus.io/guides/
- Documentação Angular: https://angular.io/docs
- OWASP Top 10: https://owasp.org/www-project-top-ten/
- JWT Best Practices: https://datatracker.ietf.org/doc/html/rfc8725

