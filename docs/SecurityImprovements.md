# Melhorias de Segurança - Gerenciador Financeiro PJ

**Data da Avaliação:** Dezembro 2025  
**Avaliador:** Análise de Segurança  
**Classificação Geral:** MÉDIO RISCO

---

## Crítico - Problemas Graves

### 1. Credenciais Hardcoded no Código
**Arquivo:** `application.properties`
```properties
quarkus.datasource.username=postgres
quarkus.datasource.password=postgres
```
**Risco:** CWE-798 (Use of Hard-coded Credentials)  
**Impacto:** Se o código for commitado, credenciais expostas no repositório  
**Correção:** Usar variáveis de ambiente (`${DB_USER}`, `${DB_PASSWORD}`)

---

### 2. BCrypt sem Cost Factor Explícito
**Arquivo:** `PasswordService.java`
```java
return BCrypt.hashpw(senha, BCrypt.gensalt());
```
**Risco:** `gensalt()` sem parâmetro usa cost factor 10 (padrão)  
**Problema:** Em 2024, o recomendado é 12-14 para resistir a ataques de força bruta  
**Correção:**
```java
return BCrypt.hashpw(senha, BCrypt.gensalt(12));
```

---

### 3. JWT Expiration e Refresh Token
**Arquivo:** `GenerateToken.java`
```java
private static Integer JWT_EXPIRATION_TIME = 720;
```
**Problema:** 720 segundos = 12 minutos
- Muito curto para UX (usuário precisa relogar frequentemente)
- Sem refresh token para renovar sessão de forma segura

**Recomendação:**
- Access token: 15-30 minutos
- Implementar Refresh Token (longa duração, armazenado de forma segura)

---

### 4. Ausência de Rate Limiting
**Arquivo:** `AuthResource.java`  
**Risco:** CWE-307 (Improper Restriction of Excessive Authentication Attempts)  
**Impacto:** Vulnerável a ataques de brute force e credential stuffing  
**Correção:** Implementar rate limiting por IP/email (ex: 5 tentativas por minuto)

---

## Alto - Problemas Importantes

### 5. Exception Handler Ausente - Information Disclosure
**Arquivo:** `CustomException.java`
```java
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
```
**Problema:** Sem `@Provider` ExceptionMapper, stack traces podem vazar para o cliente  
**Risco:** CWE-209 (Information Exposure Through an Error Message)  
**Correção:** Criar `ExceptionMapper<CustomException>` que retorna apenas mensagem sanitizada

---

### 6. Timing Attack na Verificação de Senha
**Arquivo:** `AuthService.java`
```java
var user = Usuario.find("email", loginRequest.getEmail())
        .firstResultOptional()
        .orElseThrow(() -> new CustomException("..."));  // Retorna IMEDIATAMENTE

if (!passwordService.verify(...)) {  // Só executa BCrypt se email existe
    throw new CustomException("...");
}
```
**Risco:** CWE-208 (Observable Timing Discrepancy)  
**Impacto:** Atacante pode enumerar emails válidos medindo tempo de resposta:
- Email não existe → resposta rápida (~1ms)
- Email existe → resposta lenta (~100ms, BCrypt)

**Correção:**
```java
var user = Usuario.find("email", email).firstResultOptional().orElse(null);
boolean valid = user != null && passwordService.verify(senha, user.getSenhaHash());
if (!valid) {
    // Sempre executa hash dummy para equalizar tempo
    passwordService.verify("dummy", "$2a$12$dummy.hash.here");
    throw new CustomException("Falha na autenticação");
}
```

---

### 7. UUID no JWT como String
**Arquivo:** `GenerateToken.java`
```java
.claim("uuid", userId)
```
**Problema:** UUID sendo passado como objeto, pode serializar de forma inesperada  
**Correção:** Converter explicitamente para String: `.claim("uuid", userId.toString())`

---

### 8. Chaves JWT no Classpath
**Arquivo:** `application.properties`
```properties
mp.jwt.verify.publickey.location=publicKey.pem
smallrye.jwt.sign.key.location=privateKey.pem
```
**Problema:** Chaves no diretório resources podem ser empacotadas no JAR  
**Correção:** Usar caminho absoluto externo ou variável de ambiente

---

## Médio - Melhorias Recomendadas

### 9. CORS Permissivo
**Arquivo:** `application.properties`
```properties
quarkus.http.cors=true
quarkus.http.cors.origins=http://localhost:4200
```
**Status:** OK para desenvolvimento, mas em produção:
- Adicionar `quarkus.http.cors.exposed-headers`
- Considerar `quarkus.http.cors.access-control-allow-credentials=true` se usar cookies

---

### 10. Sem Validação de Força de Senha
**Arquivo:** `RegisterRequest.java`
```java
@Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
private String senha;
```
**Problema:** Apenas valida tamanho mínimo. `12345678` seria aceito.  
**Recomendação:** Adicionar regex para complexidade:
```java
@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", 
         message = "Senha deve conter maiúscula, minúscula e número")
```

---

### 11. Sem Auditoria/Logging de Segurança
**Problema:** Nenhum log de:
- Tentativas de login falhas
- Registros de novos usuários
- Tokens gerados

**Impacto:** Sem capacidade de detectar ataques ou investigar incidentes

---

### 12. Duplicação de Constante JWT_EXPIRATION_TIME
**Arquivos:** `AuthService.java` e `GenerateToken.java`
```java
private static Integer JWT_EXPIRATION_TIME = 720;
```
**Problema:** Constante duplicada pode causar inconsistência  
**Correção:** Centralizar em uma classe de configuração

---

## Pontos Positivos

| Item | Status | Observação |
|------|--------|------------|
| BCrypt para senhas | OK | Algoritmo correto |
| JWT assinado (não encriptado) | OK | Adequado para autorização |
| Mensagem de erro genérica no login | OK | Não revela se email existe |
| Bean Validation | OK | Entrada validada |
| Email único no banco | OK | Constraint + verificação |
| `@PermitAll` explícito | OK | Rotas públicas declaradas |
| UUID como ID | OK | Não expõe sequência |

---

## Resumo de Prioridades

| Prioridade | Item | Ação |
|------------|------|------|
| P0 | Credenciais hardcoded | Usar variáveis de ambiente |
| P0 | Rate limiting | Implementar limite de tentativas |
| P1 | BCrypt cost factor | Aumentar para 12 |
| P1 | Timing attack | Equalizar tempo de resposta |
| P1 | Exception handler | Criar ExceptionMapper |
| P2 | Refresh token | Implementar renovação segura |
| P2 | Audit logging | Adicionar logs de segurança |
| P3 | Validação de senha | Adicionar complexidade |
| P3 | Chaves JWT externas | Mover para fora do classpath |

---

## Referências

- OWASP Top 10: https://owasp.org/www-project-top-ten/
- CWE Database: https://cwe.mitre.org/
- JWT Best Practices: https://datatracker.ietf.org/doc/html/rfc8725
- BCrypt Work Factor: https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html
