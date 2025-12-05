# Software Requirements Specification (SRS)
## Gerenciador Financeiro PJ - Versão de Estudo/Treinamento

**Versão:** 1.0 (Simplificada)  
**Data:** 2025  
**Objetivo:** Projeto de estudo para treinar Java 21, Quarkus e Angular

---

## 1. Introdução

### 1.1 Propósito do Documento

Este documento define os requisitos de um sistema simples de gerenciamento financeiro para estudo e treinamento das tecnologias: Java 21, Quarkus e Angular.

### 1.2 Escopo do Produto

Sistema web minimalista para controle básico de receitas e despesas de uma empresa. Foco em aprender os frameworks, não em funcionalidades complexas.

**Funcionalidades básicas:**
- Cadastro e login de usuário
- Cadastro de receitas e despesas
- Listagem de transações
- Dashboard simples com totais

**Fora do escopo:**
- Multi-tenancy (múltiplas empresas)
- Categorias de transações
- Relatórios complexos
- Integrações externas
- Recuperação de senha

### 1.3 Stack Tecnológico

- **Backend**: Java 21 + Quarkus
- **Frontend**: Angular
- **Banco de Dados**: PostgreSQL (ou H2 para desenvolvimento)
- **Deploy**: AWS (free tier)

---

## 2. Requisitos Funcionais

### RF-001: Cadastro de Usuário

**Descrição:** Sistema deve permitir cadastro de um usuário com:
- Nome
- Email (único)
- Senha

**Prioridade:** Alta

### RF-002: Login

**Descrição:** Sistema deve permitir login com email e senha, retornando token JWT.

**Prioridade:** Alta

### RF-003: Cadastro de Receita

**Descrição:** Sistema deve permitir cadastrar receita com:
- Data
- Valor (positivo)
- Descrição

**Prioridade:** Alta

### RF-004: Cadastro de Despesa

**Descrição:** Sistema deve permitir cadastrar despesa com:
- Data
- Valor (positivo)
- Descrição

**Prioridade:** Alta

### RF-005: Listagem de Transações

**Descrição:** Sistema deve listar todas as transações (receitas e despesas) do usuário logado.

**Prioridade:** Alta

### RF-006: Edição de Transação

**Descrição:** Sistema deve permitir editar transações cadastradas.

**Prioridade:** Média

### RF-007: Exclusão de Transação

**Descrição:** Sistema deve permitir excluir transações.

**Prioridade:** Média

### RF-008: Dashboard

**Descrição:** Sistema deve exibir dashboard com:
- Total de receitas
- Total de despesas
- Saldo (receitas - despesas)

**Prioridade:** Alta

---

## 3. Requisitos Não-Funcionais

### RNF-001: Autenticação JWT

**Descrição:** Usar JWT para autenticação de APIs.

**Prioridade:** Alta

### RNF-002: Senhas com Hash

**Descrição:** Senhas devem ser armazenadas com hash (bcrypt).

**Prioridade:** Alta

### RNF-003: Validação de Dados

**Descrição:** Validar dados de entrada (valores positivos, campos obrigatórios).

**Prioridade:** Alta

### RNF-004: Interface Responsiva

**Descrição:** Interface deve funcionar em mobile e desktop.

**Prioridade:** Média

---

## 4. Arquitetura Simplificada

### 4.1 Backend (Quarkus)

**Estrutura básica:**
```
src/main/java/
  ├── entity/
  │   ├── Usuario.java
  │   └── Transacao.java
  ├── repository/
  │   ├── UsuarioRepository.java
  │   └── TransacaoRepository.java
  ├── service/
  │   ├── AuthService.java
  │   └── TransacaoService.java
  ├── resource/
  │   ├── AuthResource.java
  │   └── TransacaoResource.java
  └── security/
      └── JwtUtil.java
```

**Tecnologias:**
- Quarkus REST (JAX-RS)
- Hibernate Panache (ORM)
- SmallRye JWT
- Bean Validation

### 4.2 Frontend (Angular)

**Estrutura básica:**
```
src/app/
  ├── components/
  │   ├── login/
  │   ├── dashboard/
  │   └── transacoes/
  ├── services/
  │   ├── auth.service.ts
  │   └── transacao.service.ts
  ├── guards/
  │   └── auth.guard.ts
  └── models/
      ├── usuario.ts
      └── transacao.ts
```

### 4.3 Banco de Dados

**Tabelas:**

**Usuario**
- id (PK)
- nome
- email (único)
- senha_hash
- data_cadastro

**Transacao**
- id (PK)
- tipo (RECEITA ou DESPESA)
- data
- valor
- descricao
- usuario_id (FK)

---

## 5. Modelo de Dados Simplificado

```
┌──────────────┐
│   Usuario    │
├──────────────┤
│ id (PK)      │
│ nome         │
│ email (UK)   │
│ senha_hash   │
└──────┬───────┘
       │ 1
       │
       │ N
┌──────▼────────┐
│  Transacao   │
├──────────────┤
│ id (PK)      │
│ tipo         │
│ data         │
│ valor        │
│ descricao    │
│ usuario_id   │
└──────────────┘
```

---

## 6. Casos de Uso Principais

### UC-001: Fazer Login

1. Usuário acessa tela de login
2. Informa email e senha
3. Sistema valida credenciais
4. Sistema retorna token JWT
5. Usuário é redirecionado para dashboard

### UC-002: Cadastrar Receita

1. Usuário acessa formulário de receita
2. Preenche data, valor e descrição
3. Sistema valida dados
4. Sistema salva receita vinculada ao usuário
5. Sistema exibe mensagem de sucesso

### UC-003: Visualizar Dashboard

1. Usuário acessa dashboard
2. Sistema calcula total de receitas
3. Sistema calcula total de despesas
4. Sistema calcula saldo
5. Sistema exibe os valores

---

## 7. Regras de Negócio

### RN-001: Valor Positivo
Transações devem ter valor maior que zero.

### RN-002: Email Único
Email deve ser único no sistema.

### RN-003: Dados do Usuário
Usuário só pode ver/editar suas próprias transações.

---

## 8. Deploy AWS (Opcional - Para Estudo)

### Opção Simplificada: EC2 + RDS

**Passos básicos:**
1. Criar instância EC2 t2.micro (free tier)
2. Criar RDS PostgreSQL db.t2.micro (free tier)
3. Deploy do backend Quarkus no EC2
4. Build do Angular e servir via Nginx ou pelo próprio backend
5. Configurar Security Groups

**Serviços AWS (free tier):**
- EC2 t2.micro: 750h/mês
- RDS PostgreSQL db.t2.micro: 750h/mês
- S3: 5GB storage (para assets, se necessário)

**Alternativa para desenvolvimento:**
- Usar H2 database (em memória)
- Rodar tudo localmente
- Deploy AWS apenas quando necessário

---

## 9. Checklist de Implementação

### Backend (Quarkus)
- [ ] Configurar projeto Quarkus
- [ ] Criar entidades (Usuario, Transacao)
- [ ] Criar repositories
- [ ] Implementar autenticação JWT
- [ ] Criar endpoints REST
- [ ] Adicionar validações
- [ ] Testar APIs com Postman/Insomnia

### Frontend (Angular)
- [ ] Criar projeto Angular
- [ ] Configurar roteamento
- [ ] Criar serviço de autenticação
- [ ] Criar guard de autenticação
- [ ] Criar componentes (login, dashboard, transações)
- [ ] Integrar com API backend
- [ ] Adicionar validações de formulário

### Banco de Dados
- [ ] Configurar PostgreSQL (ou H2)
- [ ] Criar migrations (Flyway ou manual)
- [ ] Popular dados de teste

### Deploy (Opcional)
- [ ] Configurar EC2
- [ ] Configurar RDS
- [ ] Deploy do backend
- [ ] Deploy do frontend
- [ ] Configurar HTTPS (Let's Encrypt)

---

## 10. Objetivos de Aprendizado

Este projeto visa treinar:

1. **Quarkus:**
   - Criação de APIs REST
   - Integração com banco de dados (Panache)
   - Autenticação JWT
   - Validação de dados

2. **Angular:**
   - Componentes e serviços
   - Roteamento
   - HTTP Client
   - Guards e interceptors
   - Formulários reativos

3. **Java 21:**
   - Recursos modernos do Java
   - Records (se aplicável)
   - Pattern matching

4. **AWS (Opcional):**
   - EC2 básico
   - RDS básico
   - Security Groups
   - Deploy simples

---

**Fim do Documento SRS (Versão Simplificada)**

- Gestão de múltiplas empresas (multi-tenancy)
- Controle de receitas e despesas
- Categorização de transações financeiras
- Visualização de dashboard com indicadores financeiros
- Relatórios básicos de fluxo de caixa

**Fora do escopo desta versão minimalista:**
- Integração com sistemas bancários
- Integração com APIs de Nota Fiscal (Sefaz)
- Controle de estoque
- Gestão de contas a pagar/receber com parcelamento
- Anexos de documentos
- Múltiplas moedas
- Relatórios fiscais complexos

### 1.3 Definições, Acrônimos e Abreviações

- **SRS**: Software Requirements Specification
- **PJ**: Pessoa Jurídica
- **CNPJ**: Cadastro Nacional da Pessoa Jurídica
- **JWT**: JSON Web Token
- **API**: Application Programming Interface
- **CRUD**: Create, Read, Update, Delete
- **HTTPS**: Hypertext Transfer Protocol Secure
- **AWS**: Amazon Web Services
- **RDS**: Relational Database Service
- **VPC**: Virtual Private Cloud
- **IAM**: Identity and Access Management
- **CORS**: Cross-Origin Resource Sharing
- **XSS**: Cross-Site Scripting
- **SQL Injection**: Técnica de ataque que explora vulnerabilidades em consultas SQL
- **Multi-tenancy**: Arquitetura onde uma única instância do software serve múltiplos clientes (empresas)

### 1.4 Referências

- IEEE Std 830-1998 - IEEE Recommended Practice for Software Requirements Specifications
- Java 21 Documentation - https://docs.oracle.com/en/java/javase/21/
- Quarkus Framework Documentation - https://quarkus.io/guides/
- Angular Documentation - https://angular.io/docs
- AWS Free Tier - https://aws.amazon.com/free/
- PostgreSQL Documentation - https://www.postgresql.org/docs/

### 1.5 Visão Geral do Documento

Este documento está organizado nas seguintes seções:

- **Seção 2**: Descrição Geral - apresenta a perspectiva do produto, funções principais, características dos usuários e restrições
- **Seção 3**: Requisitos Funcionais - detalha todas as funcionalidades do sistema
- **Seção 4**: Requisitos Não-Funcionais - especifica requisitos de performance, segurança, usabilidade, compatibilidade e infraestrutura
- **Seção 5**: Arquitetura do Sistema - descreve a stack tecnológica e arquitetura proposta
- **Seção 6**: Casos de Uso - apresenta os principais fluxos de uso do sistema
- **Seção 7**: Regras de Negócio - define as regras que o sistema deve seguir
- **Seção 8**: Estratégia de Deploy AWS - detalha a estratégia de deploy na AWS usando free tier
- **Seção 9**: Glossário - termos técnicos e de negócio
- **Seção 10**: Apêndices - diagramas e modelos adicionais

---

## 2. Descrição Geral

### 2.1 Perspectiva do Produto

O Gerenciador Financeiro PJ é um sistema web standalone que opera de forma independente, sem necessidade de integração com outros sistemas na versão minimalista. O sistema será acessado via navegador web e hospedado na infraestrutura AWS.

**Componentes principais:**
- Frontend: Aplicação Angular responsiva
- Backend: API REST desenvolvida com Quarkus (Java 21)
- Banco de Dados: PostgreSQL (RDS AWS)
- Autenticação: Sistema próprio baseado em JWT

### 2.2 Funções do Produto

O sistema oferece as seguintes funções principais:

1. **Gestão de Empresas**: Permite cadastrar, editar e gerenciar informações de empresas
2. **Gestão de Usuários**: Sistema de autenticação e autorização com isolamento por empresa
3. **Gestão de Transações**: Cadastro, edição e exclusão de receitas e despesas
4. **Gestão de Categorias**: Criação e manutenção de categorias para classificação de transações
5. **Dashboard Financeiro**: Visualização de indicadores e resumos financeiros
6. **Relatórios**: Geração de relatórios básicos de fluxo de caixa

### 2.3 Características do Usuário

O sistema é destinado a:

- **Usuários Administradores de Empresa**: Responsáveis por cadastrar a empresa e gerenciar usuários
- **Usuários Financeiros**: Responsáveis por cadastrar e gerenciar transações financeiras
- **Usuários Consultores**: Apenas visualização de dados e relatórios

**Requisitos técnicos dos usuários:**
- Conhecimento básico de navegação web
- Acesso a navegador moderno (Chrome, Firefox, Edge ou Safari)
- Conexão com internet

### 2.4 Restrições Gerais

**Restrições Técnicas:**
- Deve utilizar Java 21 como linguagem de programação do backend
- Deve utilizar Quarkus como framework
- Deve utilizar Angular para o frontend
- Deve ser deployado na AWS usando serviços do free tier
- Banco de dados deve ser PostgreSQL ou MySQL

**Restrições de Negócio:**
- Versão minimalista - funcionalidades básicas apenas
- Foco em pequenas e médias empresas
- Sem integrações externas nesta versão

**Restrições de Segurança:**
- HTTPS obrigatório em produção
- Isolamento completo de dados entre empresas
- Autenticação obrigatória para todas as operações

**Restrições de Infraestrutura:**
- Uso exclusivo de serviços AWS free tier
- Limitações de recursos conforme free tier (ex: RDS db.t2.micro)

### 2.5 Suposições e Dependências

**Suposições:**
- Usuários possuem conhecimento básico de gestão financeira
- Empresas possuem CNPJ válido
- Usuários têm acesso estável à internet
- AWS free tier estará disponível durante o desenvolvimento e operação inicial

**Dependências:**
- Disponibilidade de serviços AWS (RDS, EC2/ECS, S3)
- JDK 21 instalado no ambiente de desenvolvimento
- Node.js e npm para desenvolvimento do frontend Angular
- PostgreSQL como banco de dados (via RDS ou instalação local para desenvolvimento)

---

## 3. Requisitos Funcionais

### 3.1 Gestão de Empresas

#### RF-001: Cadastro de Empresas

**Descrição:** O sistema deve permitir o cadastro de empresas com as seguintes informações:
- CNPJ (obrigatório, único, válido)
- Razão Social (obrigatório)
- Nome Fantasia (opcional)
- Data de cadastro (automático)

**Prioridade:** Alta  
**Origem:** Requisito do negócio

**Critérios de Aceitação:**
- CNPJ deve ser validado antes do cadastro
- CNPJ duplicado não pode ser cadastrado
- Todos os campos obrigatórios devem ser preenchidos
- Mensagem de sucesso deve ser exibida após cadastro

#### RF-002: Edição de Dados da Empresa

**Descrição:** O sistema deve permitir a edição dos dados cadastrais da empresa, exceto o CNPJ.

**Prioridade:** Alta  
**Origem:** Requisito do negócio

**Critérios de Aceitação:**
- Apenas usuários autenticados da empresa podem editar
- CNPJ não pode ser alterado
- Validações devem ser aplicadas nos campos editados
- Alterações devem ser salvas e persistidas

#### RF-003: Exclusão Lógica de Empresa

**Descrição:** O sistema deve permitir a exclusão lógica de empresas, mantendo os dados históricos mas impedindo acesso futuro.

**Prioridade:** Média  
**Origem:** Requisito de negócio (compliance e auditoria)

**Critérios de Aceitação:**
- Exclusão deve ser lógica (flag de ativo/inativo)
- Dados históricos devem ser preservados
- Usuários da empresa não devem mais conseguir fazer login após exclusão
- Operação deve ser registrada em log de auditoria

### 3.2 Gestão de Usuários e Autenticação

#### RF-004: Cadastro de Usuários Vinculados a Empresa

**Descrição:** O sistema deve permitir o cadastro de usuários vinculados a uma empresa específica. Cada usuário deve ter:
- Nome completo (obrigatório)
- Email (obrigatório, único, válido)
- Senha (obrigatório, mínimo 8 caracteres)
- Empresa vinculada (obrigatório)

**Prioridade:** Alta  
**Origem:** Requisito de segurança e negócio

**Critérios de Aceitação:**
- Email deve ser único no sistema
- Senha deve atender critérios de segurança (mínimo 8 caracteres)
- Usuário deve ser vinculado a uma empresa existente
- Senha deve ser armazenada com hash (bcrypt)

#### RF-005: Autenticação (Login/Logout)

**Descrição:** O sistema deve fornecer funcionalidade de autenticação permitindo que usuários façam login e logout.

**Prioridade:** Alta  
**Origem:** Requisito de segurança

**Critérios de Aceitação:**
- Login deve validar email e senha
- Após login bem-sucedido, deve retornar token JWT
- Token JWT deve conter informações da empresa do usuário
- Logout deve invalidar o token
- Tentativas de login inválidas devem ser registradas
- Após múltiplas tentativas falhas, conta deve ser temporariamente bloqueada

#### RF-006: Autorização Baseada em Empresa

**Descrição:** O sistema deve garantir que usuários só possam acessar dados da empresa à qual estão vinculados (multi-tenancy).

**Prioridade:** Crítica  
**Origem:** Requisito de segurança

**Critérios de Aceitação:**
- Todas as requisições devem validar o token JWT
- Token deve conter identificador da empresa
- Queries ao banco de dados devem sempre filtrar por empresa
- Tentativas de acesso a dados de outra empresa devem ser bloqueadas e registradas
- Isolamento deve ser aplicado em todas as operações CRUD

#### RF-007: Recuperação de Senha

**Descrição:** O sistema deve permitir que usuários recuperem suas senhas através de email.

**Prioridade:** Média  
**Origem:** Requisito de usabilidade

**Critérios de Aceitação:**
- Usuário deve informar email cadastrado
- Sistema deve gerar token de recuperação único e temporário
- Email com link de recuperação deve ser enviado
- Link deve expirar após período determinado (ex: 1 hora)
- Nova senha deve atender critérios de segurança

### 3.3 Gestão de Transações Financeiras

#### RF-008: Cadastro de Receitas

**Descrição:** O sistema deve permitir o cadastro de receitas com as seguintes informações:
- Data (obrigatório, não pode ser futura)
- Valor (obrigatório, positivo)
- Descrição (obrigatório)
- Categoria (obrigatório, deve existir)

**Prioridade:** Alta  
**Origem:** Requisito funcional principal

**Critérios de Aceitação:**
- Todos os campos obrigatórios devem ser preenchidos
- Valor deve ser positivo
- Data não pode ser futura
- Categoria deve existir e estar vinculada à empresa
- Transação deve ser automaticamente vinculada à empresa do usuário logado

#### RF-009: Cadastro de Despesas

**Descrição:** O sistema deve permitir o cadastro de despesas com as mesmas informações das receitas (data, valor, descrição, categoria).

**Prioridade:** Alta  
**Origem:** Requisito funcional principal

**Critérios de Aceitação:**
- Mesmos critérios de RF-008
- Valor deve ser positivo
- Transação deve ser marcada como tipo "despesa"

#### RF-010: Edição de Transações

**Descrição:** O sistema deve permitir a edição de transações financeiras já cadastradas.

**Prioridade:** Alta  
**Origem:** Requisito funcional

**Critérios de Aceitação:**
- Apenas transações da empresa do usuário podem ser editadas
- Todas as validações de RF-008 e RF-009 devem ser aplicadas
- Histórico de alterações deve ser mantido (opcional na versão minimalista)
- Data de última alteração deve ser atualizada

#### RF-011: Exclusão Lógica de Transações

**Descrição:** O sistema deve permitir a exclusão lógica de transações, mantendo dados históricos.

**Prioridade:** Média  
**Origem:** Requisito de negócio (auditoria)

**Critérios de Aceitação:**
- Exclusão deve ser lógica (flag de ativo/inativo)
- Transação excluída não deve aparecer em listagens normais
- Dados devem ser preservados para auditoria
- Operação deve ser registrada em log

#### RF-012: Listagem de Transações com Filtros

**Descrição:** O sistema deve permitir listar transações com filtros por:
- Período (data inicial e final)
- Tipo (receita/despesa)
- Categoria

**Prioridade:** Alta  
**Origem:** Requisito funcional

**Critérios de Aceitação:**
- Listagem deve mostrar apenas transações da empresa do usuário
- Filtros devem ser aplicáveis em combinação
- Resultados devem ser paginados (ex: 20 itens por página)
- Ordenação padrão por data (mais recente primeiro)

#### RF-013: Gestão de Categorias (CRUD)

**Descrição:** O sistema deve permitir criar, ler, atualizar e excluir categorias de transações. Cada categoria pertence a uma empresa.

**Prioridade:** Alta  
**Origem:** Requisito funcional

**Critérios de Aceitação:**
- Categoria deve ter nome (obrigatório, único por empresa)
- Categoria deve estar vinculada à empresa
- Categorias não podem ser excluídas se houver transações vinculadas (ou exclusão lógica)
- Listagem deve mostrar apenas categorias da empresa do usuário

### 3.4 Relatórios e Visualizações

#### RF-014: Dashboard com Resumo Financeiro

**Descrição:** O sistema deve exibir um dashboard com:
- Saldo atual (receitas - despesas)
- Total de receitas do mês atual
- Total de despesas do mês atual
- Gráfico de receitas vs despesas (opcional na versão minimalista)

**Prioridade:** Alta  
**Origem:** Requisito funcional principal

**Critérios de Aceitação:**
- Dados devem ser calculados apenas para a empresa do usuário
- Valores devem ser atualizados em tempo real
- Dashboard deve carregar em tempo razoável (< 2 segundos)

#### RF-015: Relatório de Fluxo de Caixa por Período

**Descrição:** O sistema deve gerar relatório de fluxo de caixa mostrando receitas e despesas agrupadas por período (diário, semanal ou mensal).

**Prioridade:** Média  
**Origem:** Requisito funcional

**Critérios de Aceitação:**
- Relatório deve permitir seleção de período (data inicial e final)
- Deve mostrar receitas e despesas agrupadas
- Deve calcular saldo por período
- Dados devem ser apenas da empresa do usuário

#### RF-016: Visualização de Transações em Lista/Tabela

**Descrição:** O sistema deve exibir transações em formato de lista/tabela com informações essenciais.

**Prioridade:** Alta  
**Origem:** Requisito funcional

**Critérios de Aceitação:**
- Tabela deve mostrar: data, tipo, valor, descrição, categoria
- Interface deve ser responsiva (mobile-friendly)
- Deve permitir ordenação por colunas
- Deve suportar paginação

---

## 4. Requisitos Não-Funcionais

### 4.1 Performance

#### RNF-001: Tempo de Resposta para Operações CRUD

**Descrição:** O sistema deve responder a operações CRUD (Create, Read, Update, Delete) em menos de 2 segundos em condições normais de uso.

**Prioridade:** Alta  
**Medição:** Tempo entre requisição HTTP e resposta HTTP completa

**Critérios de Aceitação:**
- 95% das operações CRUD devem completar em < 2s
- Operações de leitura (GET) devem ser otimizadas com índices no banco
- Queries complexas devem ser otimizadas

#### RNF-002: Suporte a Volume de Dados

**Descrição:** O sistema deve suportar até 1000 transações por empresa sem degradação significativa de performance.

**Prioridade:** Média  
**Medição:** Testes de carga com até 1000 transações por empresa

**Critérios de Aceitação:**
- Sistema deve manter performance aceitável com 1000 transações
- Queries devem utilizar índices apropriados
- Paginação deve ser implementada para listagens grandes

### 4.2 Segurança

#### RNF-003: Autenticação JWT com Tokens Seguros

**Descrição:** O sistema deve utilizar JWT (JSON Web Token) para autenticação, com tokens seguros e com tempo de expiração adequado.

**Prioridade:** Crítica  
**Implementação:**
- Tokens devem expirar após período determinado (ex: 24 horas)
- Tokens devem ser assinados com algoritmo seguro (HS256 ou RS256)
- Secret key deve ser armazenada de forma segura (AWS Secrets Manager)
- Refresh tokens devem ser implementados (opcional na versão minimalista)

#### RNF-004: Senhas Armazenadas com Hash Bcrypt

**Descrição:** Todas as senhas devem ser armazenadas usando algoritmo de hash bcrypt com salt adequado.

**Prioridade:** Crítica  
**Implementação:**
- Nunca armazenar senhas em texto plano
- Usar bcrypt com cost factor mínimo de 10
- Salt deve ser único para cada senha

#### RNF-005: HTTPS Obrigatório em Produção

**Descrição:** Todas as comunicações em produção devem utilizar HTTPS (TLS 1.2 ou superior).

**Prioridade:** Crítica  
**Implementação:**
- Certificado SSL válido deve ser configurado
- Redirecionamento automático de HTTP para HTTPS
- Headers de segurança devem ser configurados (HSTS)

#### RNF-006: Isolamento de Dados por Empresa (Multi-tenancy)

**Descrição:** O sistema deve garantir isolamento completo de dados entre empresas, impedindo que usuários de uma empresa acessem dados de outra.

**Prioridade:** Crítica  
**Implementação:**
- Todas as queries devem filtrar por empresa_id
- Validação de empresa deve ocorrer em todas as camadas (API, Service, Repository)
- Testes devem validar que isolamento funciona corretamente

#### RNF-007: Validação de Entrada em Todas as APIs

**Descrição:** Todas as APIs devem validar e sanitizar dados de entrada antes do processamento.

**Prioridade:** Alta  
**Implementação:**
- Validação usando Bean Validation (JSR 303) no backend
- Validação no frontend antes do envio
- Rejeição de dados inválidos com mensagens claras

#### RNF-008: Proteção contra SQL Injection e XSS

**Descrição:** O sistema deve estar protegido contra ataques de SQL Injection e Cross-Site Scripting (XSS).

**Prioridade:** Crítica  
**Implementação:**
- Usar prepared statements/parameterized queries (JPA/Hibernate)
- Sanitizar inputs do usuário
- Configurar Content Security Policy (CSP) no frontend
- Escapar dados antes de exibir no frontend

#### RNF-009: Rate Limiting nas APIs

**Descrição:** O sistema deve implementar rate limiting para prevenir abuso e ataques de força bruta.

**Prioridade:** Média  
**Implementação:**
- Limitar número de requisições por IP/usuário por período
- Bloquear temporariamente após múltiplas tentativas de login falhas
- Configurar limites apropriados (ex: 100 requisições/minuto por IP)

#### RNF-010: Logs de Auditoria para Operações Críticas

**Descrição:** O sistema deve registrar logs de auditoria para operações críticas (criação/edição/exclusão de dados sensíveis).

**Prioridade:** Alta  
**Implementação:**
- Registrar: usuário, ação, timestamp, dados alterados
- Logs devem ser armazenados de forma segura
- Logs devem ser imutáveis (append-only)

### 4.3 Usabilidade

#### RNF-011: Interface Responsiva (Mobile-First)

**Descrição:** A interface do sistema deve ser responsiva e funcionar adequadamente em dispositivos móveis, tablets e desktops.

**Prioridade:** Alta  
**Critérios de Aceitação:**
- Interface deve ser utilizável em telas a partir de 320px de largura
- Elementos devem ser tocáveis em dispositivos móveis (mínimo 44x44px)
- Layout deve se adaptar a diferentes tamanhos de tela

#### RNF-012: Feedback Visual para Todas as Ações

**Descrição:** O sistema deve fornecer feedback visual claro para todas as ações do usuário (sucesso, erro, carregamento).

**Prioridade:** Média  
**Critérios de Aceitação:**
- Mensagens de sucesso devem ser exibidas após operações bem-sucedidas
- Mensagens de erro devem ser claras e acionáveis
- Indicadores de carregamento devem ser exibidos durante operações assíncronas

### 4.4 Compatibilidade

#### RNF-013: Compatibilidade com Navegadores Modernos

**Descrição:** O sistema deve ser compatível com versões recentes dos principais navegadores.

**Prioridade:** Alta  
**Navegadores Suportados:**
- Google Chrome (últimas 2 versões)
- Mozilla Firefox (últimas 2 versões)
- Microsoft Edge (últimas 2 versões)
- Safari (últimas 2 versões)

#### RNF-014: Java 21 como Requisito Mínimo

**Descrição:** O backend deve utilizar Java 21 como versão mínima.

**Prioridade:** Alta  
**Implementação:**
- Código deve compilar e executar com JDK 21
- Recursos específicos do Java 21 podem ser utilizados
- Documentação deve especificar requisito de Java 21

### 4.5 Infraestrutura

#### RNF-015: Deploy na AWS usando Free Tier

**Descrição:** O sistema deve ser deployado na AWS utilizando exclusivamente serviços do free tier.

**Prioridade:** Alta  
**Implementação:**
- Utilizar EC2 t2.micro ou ECS Fargate (conforme limites free tier)
- RDS PostgreSQL db.t2.micro (750h/mês free)
- S3 para assets estáticos (5GB free)
- Respeitar limites do free tier em todos os serviços

#### RNF-016: Banco de Dados Relacional

**Descrição:** O sistema deve utilizar banco de dados relacional (PostgreSQL ou MySQL).

**Prioridade:** Alta  
**Implementação:**
- PostgreSQL recomendado (melhor suporte no Quarkus)
- RDS PostgreSQL para produção
- Migrations devem ser versionadas (Flyway ou Liquibase)

#### RNF-017: Backup Automático Diário

**Descrição:** O sistema deve realizar backup automático do banco de dados diariamente.

**Prioridade:** Alta  
**Implementação:**
- RDS oferece backups automáticos (configurar retention)
- Backups devem ser testados periodicamente
- Plano de recuperação de desastre deve ser documentado

---

## 5. Arquitetura do Sistema

### 5.1 Stack Tecnológico

#### Backend
- **Linguagem**: Java 21
- **Framework**: Quarkus (versão LTS mais recente)
- **ORM**: Hibernate/Panache
- **Validação**: Bean Validation (JSR 303)
- **Autenticação**: SmallRye JWT
- **API**: REST (JAX-RS)
- **Migrations**: Flyway ou Liquibase

#### Frontend
- **Framework**: Angular (versão LTS mais recente)
- **HTTP Client**: HttpClient (nativo Angular)
- **Autenticação**: Interceptors para JWT
- **UI Framework**: Angular Material ou PrimeNG (opcional)
- **Build**: Angular CLI

#### Banco de Dados
- **SGBD**: PostgreSQL 15+
- **Hosting**: AWS RDS (db.t2.micro no free tier)
- **Conexão**: JDBC via Hibernate

#### Infraestrutura AWS
- **Compute**: EC2 t2.micro ou ECS Fargate
- **Database**: RDS PostgreSQL db.t2.micro
- **Storage**: S3 para assets estáticos
- **Secrets**: AWS Secrets Manager
- **Monitoring**: CloudWatch
- **Networking**: VPC com subnets públicas/privadas

### 5.2 Arquitetura de Segurança

#### Camadas de Segurança

1. **Camada de Rede (AWS)**
   - VPC com subnets isoladas
   - Security Groups configurados (portas mínimas necessárias)
   - RDS em subnet privada (sem acesso público direto)

2. **Camada de Aplicação (Backend)**
   - Autenticação JWT obrigatória para todas as APIs (exceto login/registro)
   - Validação de entrada em todas as requisições
   - Filtro de empresa em todas as queries
   - Rate limiting
   - CORS configurado adequadamente

3. **Camada de Dados**
   - Conexões criptografadas (SSL/TLS)
   - Senhas com hash bcrypt
   - Prepared statements (proteção SQL Injection)

4. **Camada de Frontend**
   - Tokens JWT armazenados de forma segura (httpOnly cookies ou localStorage com proteção XSS)
   - Validação de formulários
   - Sanitização de dados antes de exibição
   - Content Security Policy (CSP)

#### Configurações de Segurança

- **CORS**: Permitir apenas domínio da aplicação em produção
- **Secrets**: Armazenar em AWS Secrets Manager (não em código)
- **Variáveis de Ambiente**: Usar para configurações sensíveis
- **Logs**: Não registrar informações sensíveis (senhas, tokens completos)

### 5.3 Modelo de Dados

#### Entidades Principais

**Empresa**
- `id` (PK, UUID ou Long)
- `cnpj` (String, único, indexado)
- `razao_social` (String)
- `nome_fantasia` (String, opcional)
- `ativo` (Boolean, para exclusão lógica)
- `data_cadastro` (Timestamp)
- `data_atualizacao` (Timestamp)

**Usuario**
- `id` (PK, UUID ou Long)
- `nome` (String)
- `email` (String, único, indexado)
- `senha_hash` (String, bcrypt)
- `empresa_id` (FK para Empresa)
- `ativo` (Boolean)
- `data_cadastro` (Timestamp)
- `ultimo_login` (Timestamp, opcional)

**Categoria**
- `id` (PK, UUID ou Long)
- `nome` (String, único por empresa)
- `empresa_id` (FK para Empresa)
- `ativo` (Boolean)
- `data_cadastro` (Timestamp)

**Transacao**
- `id` (PK, UUID ou Long)
- `tipo` (Enum: RECEITA, DESPESA)
- `data` (Date)
- `valor` (BigDecimal)
- `descricao` (String)
- `categoria_id` (FK para Categoria)
- `empresa_id` (FK para Empresa)
- `ativo` (Boolean)
- `data_cadastro` (Timestamp)
- `data_atualizacao` (Timestamp)

#### Relacionamentos

- Usuario → Empresa (N:1) - Muitos usuários pertencem a uma empresa
- Transacao → Empresa (N:1) - Muitas transações pertencem a uma empresa
- Transacao → Categoria (N:1) - Muitas transações pertencem a uma categoria
- Categoria → Empresa (N:1) - Muitas categorias pertencem a uma empresa

#### Índices Recomendados

- `empresa.cnpj` (único)
- `usuario.email` (único)
- `transacao.empresa_id` (para filtros)
- `transacao.data` (para filtros por período)
- `transacao.empresa_id + data` (índice composto)
- `categoria.empresa_id + nome` (índice composto para unicidade)

---

## 6. Casos de Uso

### 6.1 UC-001: Cadastrar Empresa

**Ator Principal:** Usuário Administrador

**Pré-condições:** Nenhuma

**Fluxo Principal:**
1. Usuário acessa a tela de cadastro de empresa
2. Sistema exibe formulário com campos: CNPJ, Razão Social, Nome Fantasia
3. Usuário preenche os dados
4. Usuário submete o formulário
5. Sistema valida CNPJ (formato e dígitos verificadores)
6. Sistema verifica se CNPJ já existe
7. Sistema cria empresa no banco de dados
8. Sistema exibe mensagem de sucesso
9. Sistema redireciona para tela de login

**Fluxos Alternativos:**
- 5a. CNPJ inválido: Sistema exibe mensagem de erro e retorna ao passo 3
- 6a. CNPJ já existe: Sistema exibe mensagem de erro e retorna ao passo 3

**Pós-condições:** Empresa cadastrada no sistema

### 6.2 UC-002: Realizar Login

**Ator Principal:** Usuário

**Pré-condições:** Usuário deve estar cadastrado no sistema

**Fluxo Principal:**
1. Usuário acessa a tela de login
2. Sistema exibe formulário com campos: Email e Senha
3. Usuário informa email e senha
4. Usuário submete o formulário
5. Sistema valida email e senha
6. Sistema verifica se usuário está ativo
7. Sistema verifica se empresa do usuário está ativa
8. Sistema gera token JWT contendo informações do usuário e empresa
9. Sistema retorna token para o frontend
10. Frontend armazena token
11. Sistema redireciona para dashboard

**Fluxos Alternativos:**
- 5a. Email ou senha inválidos: Sistema exibe mensagem de erro genérica e retorna ao passo 3
- 6a. Usuário inativo: Sistema exibe mensagem de erro e retorna ao passo 3
- 7a. Empresa inativa: Sistema exibe mensagem de erro e retorna ao passo 3

**Pós-condições:** Usuário autenticado no sistema

### 6.3 UC-003: Cadastrar Receita

**Ator Principal:** Usuário Autenticado

**Pré-condições:** Usuário deve estar autenticado e empresa deve ter categorias cadastradas

**Fluxo Principal:**
1. Usuário acessa a tela de cadastro de receita
2. Sistema exibe formulário com campos: Data, Valor, Descrição, Categoria
3. Sistema carrega lista de categorias da empresa do usuário
4. Usuário preenche os dados
5. Usuário submete o formulário
6. Sistema valida dados (valor positivo, data não futura, categoria válida)
7. Sistema vincula transação à empresa do usuário (do token JWT)
8. Sistema salva receita no banco de dados
9. Sistema exibe mensagem de sucesso
10. Sistema redireciona para lista de transações

**Fluxos Alternativos:**
- 6a. Dados inválidos: Sistema exibe mensagens de erro específicas e retorna ao passo 4
- 3a. Nenhuma categoria cadastrada: Sistema exibe mensagem informando necessidade de cadastrar categoria primeiro

**Pós-condições:** Receita cadastrada no sistema

### 6.4 UC-004: Cadastrar Despesa

**Ator Principal:** Usuário Autenticado

**Pré-condições:** Usuário deve estar autenticado e empresa deve ter categorias cadastradas

**Fluxo Principal:**
1. Usuário acessa a tela de cadastro de despesa
2. Sistema exibe formulário com campos: Data, Valor, Descrição, Categoria
3. Sistema carrega lista de categorias da empresa do usuário
4. Usuário preenche os dados
5. Usuário submete o formulário
6. Sistema valida dados (valor positivo, data não futura, categoria válida)
7. Sistema vincula transação à empresa do usuário (do token JWT)
8. Sistema marca transação como tipo DESPESA
9. Sistema salva despesa no banco de dados
10. Sistema exibe mensagem de sucesso
11. Sistema redireciona para lista de transações

**Fluxos Alternativos:**
- 6a. Dados inválidos: Sistema exibe mensagens de erro específicas e retorna ao passo 4

**Pós-condições:** Despesa cadastrada no sistema

### 6.5 UC-005: Visualizar Dashboard

**Ator Principal:** Usuário Autenticado

**Pré-condições:** Usuário deve estar autenticado

**Fluxo Principal:**
1. Usuário acessa o dashboard (tela inicial após login)
2. Sistema identifica empresa do usuário (do token JWT)
3. Sistema calcula saldo atual (soma receitas - soma despesas)
4. Sistema calcula total de receitas do mês atual
5. Sistema calcula total de despesas do mês atual
6. Sistema exibe os valores no dashboard
7. Sistema exibe lista de transações recentes (últimas 10)

**Fluxos Alternativos:**
- 3a. Nenhuma transação cadastrada: Sistema exibe valores zerados e mensagem informativa

**Pós-condições:** Dashboard exibido com dados atualizados

### 6.6 UC-006: Filtrar Transações

**Ator Principal:** Usuário Autenticado

**Pré-condições:** Usuário deve estar autenticado

**Fluxo Principal:**
1. Usuário acessa a tela de listagem de transações
2. Sistema exibe lista de transações da empresa do usuário (paginada)
3. Sistema exibe filtros: Período (data inicial/final), Tipo (receita/despesa), Categoria
4. Usuário seleciona filtros desejados
5. Usuário aplica filtros
6. Sistema valida período (data inicial <= data final)
7. Sistema busca transações aplicando filtros
8. Sistema exibe resultados filtrados (paginados)

**Fluxos Alternativos:**
- 6a. Período inválido: Sistema exibe mensagem de erro e retorna ao passo 4
- 7a. Nenhum resultado: Sistema exibe mensagem informativa

**Pós-condições:** Lista de transações filtrada exibida

---

## 7. Regras de Negócio

### RN-001: Valor de Transação Deve Ser Positivo

**Descrição:** Todas as transações (receitas e despesas) devem ter valor maior que zero.

**Aplicação:** Validação obrigatória no cadastro e edição de transações.

**Mensagem de Erro:** "O valor da transação deve ser maior que zero."

### RN-002: Data Não Pode Ser Futura

**Descrição:** A data de uma transação não pode ser uma data futura. Transações devem ser registradas apenas para datas passadas ou a data atual.

**Aplicação:** Validação obrigatória no cadastro e edição de transações.

**Mensagem de Erro:** "A data da transação não pode ser futura."

**Exceção:** Em casos especiais (opcional na versão minimalista), pode-se permitir transações futuras para planejamento, mas isso deve ser configurável.

### RN-003: Usuário Só Pode Acessar Dados da Empresa Vinculada

**Descrição:** Um usuário só pode visualizar, criar, editar ou excluir dados da empresa à qual está vinculado. Esta é uma regra crítica de segurança e isolamento.

**Aplicação:** 
- Validação em todas as requisições de API
- Filtro automático por empresa_id em todas as queries
- Validação no frontend (token JWT contém empresa_id)

**Violação:** Tentativas de acesso a dados de outra empresa devem ser bloqueadas e registradas em log de segurança.

### RN-004: CNPJ Deve Ser Válido e Único por Empresa

**Descrição:** O CNPJ cadastrado deve ser válido (formato e dígitos verificadores) e único no sistema (não pode haver duas empresas com o mesmo CNPJ).

**Aplicação:** 
- Validação de formato (XX.XXX.XXX/XXXX-XX)
- Validação de dígitos verificadores
- Verificação de unicidade no banco de dados

**Mensagem de Erro:** "CNPJ inválido" ou "CNPJ já cadastrado no sistema."

### RN-005: Email Deve Ser Único por Usuário

**Descrição:** Cada usuário deve ter um email único no sistema. Não é permitido cadastrar dois usuários com o mesmo email.

**Aplicação:** 
- Validação de formato de email
- Verificação de unicidade no banco de dados
- Validação no cadastro e edição de usuários

**Mensagem de Erro:** "Email já cadastrado no sistema."

---

## 8. Estratégia de Deploy AWS (Free Tier)

### 8.1 Opções de Deploy

#### Opção 1: EC2 t2.micro + RDS PostgreSQL (Recomendada)

**Vantagens:**
- Controle total sobre o ambiente
- Fácil configuração inicial
- 750 horas/mês gratuitas (suficiente para 24/7 em um mês)
- RDS db.t2.micro também com 750h/mês free

**Desvantagens:**
- Requer gerenciamento do servidor
- Necessita configuração manual de segurança

**Configuração:**
- EC2 t2.micro (1 vCPU, 1GB RAM)
- RDS PostgreSQL db.t2.micro (1 vCPU, 1GB RAM, 20GB storage)
- Security Groups configurados
- Elastic IP (opcional, pode ter custo)

**Custos Estimados (Free Tier):**
- EC2: $0 (750h/mês)
- RDS: $0 (750h/mês, 20GB storage)
- S3: $0 (5GB storage, 20.000 GET requests)
- Total: $0/mês (dentro dos limites)

#### Opção 2: ECS Fargate + RDS

**Vantagens:**
- Gerenciamento de containers simplificado
- Escalabilidade automática (futuro)
- Sem gerenciamento de servidor

**Desvantagens:**
- Free tier limitado (apenas 20GB-horas/mês)
- Pode não ser suficiente para 24/7
- Configuração mais complexa

**Configuração:**
- ECS Fargate (0.25 vCPU, 0.5GB RAM) - dentro do free tier
- RDS PostgreSQL db.t2.micro

**Custos Estimados:**
- ECS Fargate: Limitado no free tier (pode ter custo adicional)
- RDS: $0 (750h/mês)
- Total: Pode ter custos adicionais

#### Opção 3: Lambda + API Gateway + DynamoDB

**Vantagens:**
- Free tier generoso (1M requisições/mês)
- Serverless (sem gerenciamento de servidor)
- Escalabilidade automática

**Desvantagens:**
- Mudança de stack (DynamoDB ao invés de PostgreSQL)
- Cold starts podem afetar performance
- Configuração mais complexa

**Não Recomendado:** Requer mudança significativa de arquitetura (banco de dados).

### 8.2 Serviços AWS a Utilizar

#### RDS PostgreSQL
- **Instância**: db.t2.micro
- **Free Tier**: 750 horas/mês por 12 meses
- **Storage**: 20GB incluído
- **Backup**: Automático (7 dias retention no free tier)
- **Configuração**: 
  - Subnet privada (sem acesso público direto)
  - Security Group permitindo apenas conexões da aplicação
  - Encryption at rest habilitado

#### EC2 (Opção 1)
- **Instância**: t2.micro
- **Free Tier**: 750 horas/mês por 12 meses
- **Sistema Operacional**: Amazon Linux 2023 ou Ubuntu Server
- **Configuração**:
  - Security Group: HTTP (80), HTTPS (443), SSH (22) apenas do seu IP
  - Elastic IP (opcional)
  - User Data script para instalação automática

#### S3
- **Uso**: Armazenar assets estáticos do frontend (build do Angular)
- **Free Tier**: 5GB storage, 20.000 GET requests/mês
- **Configuração**:
  - Bucket com versionamento (opcional)
  - Política de acesso pública para assets estáticos
  - Lifecycle policy para otimização de custos

#### CloudFront (Opcional)
- **Uso**: CDN para assets estáticos
- **Free Tier**: 50GB transfer out, 2M requests
- **Configuração**: Origin apontando para S3

#### AWS Secrets Manager
- **Uso**: Armazenar secrets (JWT secret, DB password)
- **Free Tier**: Limitado (pode ter custo)
- **Alternativa**: Usar variáveis de ambiente no EC2 (menos seguro, mas gratuito)

#### CloudWatch
- **Uso**: Logs e monitoramento
- **Free Tier**: 5GB logs, métricas básicas
- **Configuração**: Logs da aplicação, métricas de RDS e EC2

#### Route 53 (Opcional)
- **Uso**: DNS personalizado
- **Free Tier**: $0.50/hosted zone/mês (pode ter custo)
- **Alternativa**: Usar DNS gratuito de terceiros (Cloudflare, etc.)

### 8.3 Considerações de Segurança AWS

#### Security Groups

**EC2 Security Group:**
- Inbound:
  - HTTP (80) de 0.0.0.0/0 (ou apenas CloudFront)
  - HTTPS (443) de 0.0.0.0/0 (ou apenas CloudFront)
  - SSH (22) apenas do seu IP pessoal
- Outbound:
  - PostgreSQL (5432) apenas para RDS Security Group
  - HTTPS (443) para internet (para atualizações, etc.)

**RDS Security Group:**
- Inbound:
  - PostgreSQL (5432) apenas do EC2 Security Group
- Outbound: Nenhum (não necessário)

#### IAM Roles

**EC2 Instance Role:**
- Permissões mínimas necessárias:
  - Leitura do Secrets Manager (se utilizado)
  - Escrita de logs no CloudWatch
  - Leitura de S3 (se necessário)

**Princípio de Menor Privilégio:**
- Aplicar apenas permissões estritamente necessárias
- Revisar permissões regularmente

#### Encryption

**Encryption at Rest:**
- RDS: Habilitar encryption (pode ter custo adicional, verificar free tier)
- EBS (volumes EC2): Encryption por padrão em algumas regiões
- S3: Server-side encryption (SSE-S3 é gratuito)

**Encryption in Transit:**
- HTTPS obrigatório (certificado SSL via Let's Encrypt ou AWS Certificate Manager)
- Conexões RDS via SSL/TLS
- TLS 1.2 ou superior

#### VPC e Networking

**Arquitetura de Rede:**
- VPC com CIDR apropriado (ex: 10.0.0.0/16)
- Subnet pública para EC2 (com Internet Gateway)
- Subnet privada para RDS (sem acesso à internet)
- NAT Gateway (opcional, pode ter custo - não necessário se RDS não precisa de internet)

**Configuração Recomendada:**
```
VPC: 10.0.0.0/16
  - Subnet Pública: 10.0.1.0/24 (EC2)
  - Subnet Privada: 10.0.2.0/24 (RDS)
```

#### Backup e Disaster Recovery

**RDS Backups:**
- Backup automático habilitado (7 dias retention no free tier)
- Snapshots manuais antes de mudanças importantes
- Testar restauração periodicamente

**Estratégia de Backup:**
- Backup diário automático do RDS
- Backup manual do código (Git)
- Documentar procedimento de recuperação

### 8.4 Passos de Deploy (Resumo)

1. **Criar VPC e Subnets**
2. **Criar Security Groups**
3. **Criar RDS PostgreSQL** (subnet privada)
4. **Criar EC2 Instance** (subnet pública)
5. **Configurar EC2** (instalar Java 21, Quarkus app)
6. **Configurar Nginx** (reverse proxy, SSL)
7. **Deploy do Frontend** (S3 + CloudFront ou servido pelo backend)
8. **Configurar Secrets** (Secrets Manager ou variáveis de ambiente)
9. **Configurar CloudWatch Logs**
10. **Testar e Validar**

### 8.5 Monitoramento e Manutenção

**CloudWatch Metrics:**
- CPU utilization (EC2, RDS)
- Memory utilization
- Network in/out
- Database connections
- Free storage space (RDS)

**Alertas Recomendados:**
- CPU > 80% por 5 minutos
- Free storage < 20% (RDS)
- Database connections > 80% do máximo

**Manutenção:**
- Atualizações de segurança do sistema operacional
- Atualizações da aplicação
- Revisão de logs regularmente
- Revisão de custos mensalmente

---

## 9. Glossário

- **API**: Application Programming Interface - Interface de programação que permite comunicação entre sistemas
- **AWS**: Amazon Web Services - Plataforma de serviços de computação em nuvem
- **Backend**: Camada de servidor da aplicação, responsável pela lógica de negócio e acesso a dados
- **Bcrypt**: Algoritmo de hash de senha que utiliza salt e é resistente a ataques de força bruta
- **CNPJ**: Cadastro Nacional da Pessoa Jurídica - Número de identificação de empresas no Brasil
- **CORS**: Cross-Origin Resource Sharing - Mecanismo que permite requisições entre diferentes origens
- **CRUD**: Create, Read, Update, Delete - Operações básicas de manipulação de dados
- **Dashboard**: Painel de controle que exibe informações resumidas e indicadores
- **Despesa**: Saída de recursos financeiros da empresa
- **EC2**: Elastic Compute Cloud - Serviço de computação em nuvem da AWS
- **ECS**: Elastic Container Service - Serviço de orquestração de containers da AWS
- **Frontend**: Camada de interface do usuário, executada no navegador
- **HTTPS**: Hypertext Transfer Protocol Secure - Protocolo HTTP com criptografia TLS/SSL
- **IAM**: Identity and Access Management - Serviço de gerenciamento de identidades e acessos da AWS
- **JWT**: JSON Web Token - Token de autenticação baseado em JSON
- **Multi-tenancy**: Arquitetura onde uma única instância do software serve múltiplos clientes (empresas)
- **ORM**: Object-Relational Mapping - Técnica que mapeia objetos para tabelas de banco de dados
- **PJ**: Pessoa Jurídica - Entidade legal (empresa) distinta de pessoa física
- **Quarkus**: Framework Java otimizado para cloud e containers
- **RDS**: Relational Database Service - Serviço gerenciado de banco de dados relacional da AWS
- **Receita**: Entrada de recursos financeiros na empresa
- **S3**: Simple Storage Service - Serviço de armazenamento de objetos da AWS
- **SQL Injection**: Técnica de ataque que explora vulnerabilidades em consultas SQL
- **Transação Financeira**: Movimentação financeira (receita ou despesa) registrada no sistema
- **VPC**: Virtual Private Cloud - Rede virtual privada na AWS
- **XSS**: Cross-Site Scripting - Técnica de ataque que injeta scripts maliciosos em páginas web

---

## 10. Apêndices

### Apêndice A: Diagrama de Arquitetura Simplificado

```
┌─────────────┐
│   Usuário   │
│  (Browser)  │
└──────┬──────┘
       │ HTTPS
       │
┌──────▼──────────────────┐
│   CloudFront (CDN)      │  (Opcional)
│   ou EC2 (Nginx)        │
└──────┬──────────────────┘
       │
┌──────▼──────────────────┐
│   Frontend (Angular)    │
│   (S3 ou EC2)           │
└──────┬──────────────────┘
       │ REST API (HTTPS)
       │
┌──────▼──────────────────┐
│   Backend (Quarkus)     │
│   (EC2 ou ECS)         │
└──────┬──────────────────┘
       │ JDBC (SSL)
       │
┌──────▼──────────────────┐
│   RDS PostgreSQL        │
│   (Subnet Privada)     │
└─────────────────────────┘
```

### Apêndice B: Modelo ER Simplificado

```
┌──────────────┐
│   Empresa    │
├──────────────┤
│ id (PK)      │
│ cnpj (UK)    │
│ razao_social │
│ nome_fantasia│
│ ativo        │
└──────┬───────┘
       │ 1
       │
       │ N
┌──────▼───────┐      ┌──────────────┐
│   Usuario    │      │  Categoria   │
├──────────────┤      ├──────────────┤
│ id (PK)      │      │ id (PK)      │
│ nome         │      │ nome         │
│ email (UK)   │      │ empresa_id   │
│ senha_hash   │      │ ativo        │
│ empresa_id   │      └──────┬───────┘
│ ativo        │             │ 1
└──────────────┘             │
                             │ N
                    ┌────────▼────────┐
                    │   Transacao    │
                    ├────────────────┤
                    │ id (PK)        │
                    │ tipo           │
                    │ data           │
                    │ valor          │
                    │ descricao      │
                    │ categoria_id   │
                    │ empresa_id     │
                    │ ativo          │
                    └────────────────┘
```

### Apêndice C: Fluxo de Autenticação

```
1. Usuário → Frontend: Informa email/senha
2. Frontend → Backend: POST /api/auth/login {email, senha}
3. Backend: Valida credenciais
4. Backend: Gera JWT token {userId, empresaId, email, exp}
5. Backend → Frontend: Retorna {token, usuario}
6. Frontend: Armazena token (localStorage ou httpOnly cookie)
7. Frontend: Adiciona token em header Authorization: Bearer <token>
8. Frontend → Backend: Requisições subsequentes com token
9. Backend: Valida token em cada requisição
10. Backend: Extrai empresaId do token
11. Backend: Filtra dados por empresaId
12. Backend → Frontend: Retorna dados filtrados
```

### Apêndice D: Exemplo de Estrutura de Token JWT

**Header:**
```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

**Payload:**
```json
{
  "sub": "user-uuid",
  "email": "usuario@empresa.com",
  "empresaId": "empresa-uuid",
  "iat": 1234567890,
  "exp": 1234654290
}
```

### Apêndice E: Checklist de Segurança

- [ ] HTTPS configurado e funcionando
- [ ] Senhas armazenadas com bcrypt
- [ ] JWT tokens com expiração adequada
- [ ] Validação de entrada em todas as APIs
- [ ] Filtro de empresa em todas as queries
- [ ] Security Groups configurados corretamente
- [ ] RDS em subnet privada
- [ ] Secrets não commitados no código
- [ ] CORS configurado adequadamente
- [ ] Rate limiting implementado
- [ ] Logs de auditoria funcionando
- [ ] Backup automático configurado
- [ ] Testes de isolamento de dados realizados

---

**Fim do Documento SRS**

