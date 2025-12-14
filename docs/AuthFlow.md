# Fluxo de Autenticação - Backend

**Projeto:** Gerenciador Financeiro PJ  
**Tecnologias:** Quarkus, SmallRye JWT, BCrypt

---

## Visão Geral

```
┌─────────────┐      ┌─────────────┐      ┌─────────────┐
│  REGISTRO   │ ───► │    LOGIN    │ ───► │   ACESSO    │
│             │      │             │      │  PROTEGIDO  │
│ BCrypt hash │      │ JWT gerado  │      │ JWT válido  │
└─────────────┘      └─────────────┘      └─────────────┘
```

---

## 1. REGISTRO (`POST /auth/register`)

### Entrada
```json
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "senha": "Senha@123"
}
```

### Fluxo Interno

```
AuthResource.register()
       │
       ▼
UsuarioService.saveUsuario()
       │
       ├──► 1. Verifica se email já existe no banco
       │         → Se existe: throw CustomException("E-mail já cadastrado")
       │
       ├──► 2. Chama PasswordService.hash(senha)
       │         → BCrypt gera salt aleatório
       │         → BCrypt aplica hash com cost factor 12
       │         → Retorna: "$2a$12$xK8f9..." (60 caracteres)
       │
       ├──► 3. Cria objeto Usuario
       │         usuario.nome = "João Silva"
       │         usuario.email = "joao@email.com"
       │         usuario.senhaHash = "$2a$12$xK8f9..."  ← Hash, NÃO a senha!
       │
       └──► 4. Persiste no banco (INSERT)
                 → UUID gerado automaticamente
```

### Banco de Dados Após Registro

```sql
| id (UUID)                            | nome       | email           | senha_hash                     |
|--------------------------------------|------------|-----------------|--------------------------------|
| 0d5904e7-bc71-437e-895e-b55e86e683a2 | João Silva | joao@email.com  | $2a$12$xK8f9sLmN3pQrStUvWx... |
```

### Resposta
- **Status:** `201 Created`
- **Body:** vazio

---

## 2. LOGIN (`POST /auth/login`)

### Entrada
```json
{
  "email": "joao@email.com",
  "senha": "Senha@123"
}
```

### Fluxo Interno

```
AuthResource.login()
       │
       ▼
AuthService.generateToken()
       │
       ├──► 1. Busca usuário por email no banco
       │         SELECT * FROM usuario WHERE email = 'joao@email.com'
       │         → Se não existe: throw CustomException("Falha na autenticação...")
       │
       ├──► 2. Verifica senha com BCrypt
       │         PasswordService.verify("Senha@123", "$2a$12$xK8f9...")
       │         │
       │         └──► BCrypt extrai o salt do hash armazenado
       │              BCrypt aplica mesmo algoritmo na senha recebida
       │              Compara os dois hashes
       │              → Se diferente: throw CustomException("Falha na autenticação...")
       │
       ├──► 3. Gera JWT Token
       │         GenerateToken.generateToken(email, uuid)
       │         │
       │         ├──► Cria payload (claims):
       │         │      {
       │         │        iss: "https://gerenciadorpj.com",
       │         │        upn: "joao@email.com",
       │         │        groups: ["user"],
       │         │        uuid: "0d5904e7-bc71-437e-895e-b55e86e683a2",
       │         │        exp: <agora + 720 segundos>
       │         │      }
       │         │
       │         └──► Assina com privateKey.pem (RS256)
       │              → Gera: "eyJhbGciOiJSUzI1NiJ9.eyJpc3M..."
       │
       └──► 4. Retorna LoginResponse(token, 720)
```

### Resposta
- **Status:** `200 OK`
- **Body:**
```json
{
  "token": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2dlcmVuY2lhZG9ycGouY29tIi...",
  "expiresIn": 720
}
```

---

## 3. ACESSO A ROTA PROTEGIDA

### Requisição
```
GET /api/transacoes
Authorization: Bearer eyJhbGciOiJSUzI1NiJ9.eyJpc3M...
```

### Fluxo Interno

```
Requisição chega com Header:
Authorization: Bearer eyJhbGciOiJSUzI1NiJ9.eyJpc3M...
       │
       ▼
SmallRye JWT Filter (automático do Quarkus)
       │
       ├──► 1. Extrai token do header
       │         "Bearer eyJ..." → "eyJ..."
       │
       ├──► 2. Decodifica JWT (3 partes separadas por .)
       │         Header.Payload.Signature
       │
       ├──► 3. Verifica assinatura com publicKey.pem
       │         → Se inválida: 401 Unauthorized
       │
       ├──► 4. Verifica claims
       │         → issuer == "https://gerenciadorpj.com"?
       │         → exp > agora? (não expirou?)
       │         → Se falhar: 401 Unauthorized
       │
       ├──► 5. Extrai informações do token
       │         → uuid, email, groups
       │         → Injeta no contexto da requisição
       │
       └──► 6. Verifica @RolesAllowed
                → groups contém "user"?
                → Se não: 403 Forbidden
       │
       ▼
Controller processa a requisição
       │
       ├──► Pode acessar dados do token:
       │      @Claim("uuid") String idUsuario
       │
       └──► Retorna dados filtrados pelo usuário
```

### Possíveis Respostas
- **200 OK:** Token válido, acesso permitido
- **401 Unauthorized:** Token ausente, inválido ou expirado
- **403 Forbidden:** Token válido, mas sem permissão (role)

---

## 4. LOGINS SUBSEQUENTES

O processo é **idêntico ao primeiro login**:
- Sempre gera um **novo token** (com novo `exp` e novo `jti`)
- O token anterior continua válido até expirar
- Não há "sessão" no servidor - cada token é independente (stateless)

---

## Arquitetura de Segurança

### Chaves JWT

```
backend/src/main/resources/
├── privateKey.pem   ← Servidor usa para ASSINAR tokens
└── publicKey.pem    ← Servidor usa para VERIFICAR tokens
```

**Importante:** O cliente (Postman/Frontend) NUNCA precisa das chaves!

### Fluxo das Chaves

```
┌─────────────────────────────────────────────────────────────────┐
│                         SERVIDOR (Quarkus)                      │
│                                                                 │
│  privateKey.pem ──► Assina o JWT no LOGIN                      │
│  publicKey.pem  ──► Verifica o JWT em ROTAS PROTEGIDAS         │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ Token JWT
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      CLIENTE (Postman/Frontend)                 │
│                                                                 │
│  - Recebe o token no login                                     │
│  - Armazena o token (localStorage/memória)                     │
│  - Envia o token no header Authorization                       │
│  - NÃO precisa das chaves .pem                                 │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## Estrutura do Token JWT

### Header
```json
{
  "typ": "JWT",
  "alg": "RS256"
}
```

### Payload (Claims)
```json
{
  "iss": "https://gerenciadorpj.com",
  "upn": "joao@email.com",
  "groups": ["user"],
  "uuid": "0d5904e7-bc71-437e-895e-b55e86e683a2",
  "iat": 1765635730,
  "exp": 1765639330,
  "jti": "1b716020-fe6f-4e07-a157-55b8018b9aae"
}
```

| Claim | Descrição |
|-------|-----------|
| `iss` | Issuer - quem emitiu o token |
| `upn` | User Principal Name - email do usuário |
| `groups` | Roles/permissões do usuário |
| `uuid` | ID único do usuário no banco |
| `iat` | Issued At - quando foi criado |
| `exp` | Expiration - quando expira |
| `jti` | JWT ID - identificador único do token |

---

## Diagrama Completo

```
┌──────────────────────────────────────────────────────────────────────────┐
│                              REGISTRO                                    │
│  senha "Senha@123" ──► BCrypt ──► "$2a$12$..." ──► Salva no banco       │
└──────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌──────────────────────────────────────────────────────────────────────────┐
│                               LOGIN                                      │
│  1. Busca user por email                                                │
│  2. BCrypt.verify(senha, hash) ──► true/false                           │
│  3. Se válido: Gera JWT assinado com privateKey.pem                     │
│  4. Retorna token ao cliente                                            │
└──────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌──────────────────────────────────────────────────────────────────────────┐
│                          ROTA PROTEGIDA                                  │
│  1. Recebe token no header Authorization                                │
│  2. Verifica assinatura com publicKey.pem                               │
│  3. Verifica expiração e issuer                                         │
│  4. Extrai uuid/email/groups do token                                   │
│  5. Verifica @RolesAllowed                                              │
│  6. Processa requisição                                                 │
└──────────────────────────────────────────────────────────────────────────┘
```

---

## Conceitos Importantes

| Conceito | Explicação |
|----------|------------|
| **Stateless** | Servidor não guarda sessão. Toda informação está no token. |
| **Senha nunca salva** | Apenas o hash BCrypt é armazenado no banco. |
| **Token auto-contido** | Contém uuid, email, roles - não precisa consultar banco para validar. |
| **Expiração** | Token expira em 720 segundos (12 minutos). Precisa fazer novo login. |
| **Assinatura RS256** | Algoritmo RSA com SHA-256. Garante que token não foi adulterado. |
| **Salt no BCrypt** | Cada hash tem salt único, mesmo senhas iguais geram hashes diferentes. |

---

## Arquivos Envolvidos

| Arquivo | Responsabilidade |
|---------|------------------|
| `AuthResource.java` | Endpoints `/auth/register` e `/auth/login` |
| `AuthService.java` | Lógica de autenticação e geração de token |
| `UsuarioService.java` | CRUD de usuário, hash de senha no registro |
| `PasswordService.java` | Hash e verificação BCrypt |
| `GenerateToken.java` | Criação e assinatura do JWT |
| `privateKey.pem` | Chave privada RSA para assinar tokens |
| `publicKey.pem` | Chave pública RSA para verificar tokens |
| `application.properties` | Configurações JWT (issuer, paths das chaves) |
