# Gerenciador Financeiro PJ - Organização de Pastas

Este projeto é um MVP de estudo (Java 21 + Quarkus no backend, Angular no frontend). Abaixo está a organização das pastas e o propósito de cada uma, a título de orientações.

```
gerenciador/
├── docs/                       # Documentação do projeto (MVP, SRS, planos)
│   ├── MVP.md
│   ├── SRS.md
│   └── Areas.md                # Planejamento por áreas / tarefas
├── backend/                    # Código do backend (Quarkus)
│   ├── src/main/java/com/gerenciador/
│   │   ├── entity/             # Entidades JPA (Usuario, Transacao, Empresa, Categoria)
│   │   ├── repository/         # Repositórios Panache
│   │   ├── resource/           # Recursos REST (Auth, Transacoes, Dashboard)
│   │   ├── service/            # Serviços (JWT, regras de negócio)
│   │   └── security/           # Filtros/interceptors de segurança (JWT)
│   ├── src/main/resources/
│   │   └── application.properties  # Configuração (H2 dev / PostgreSQL prod, JWT, etc.)
│   └── target/                 # Artefatos de build (gerado pelo Maven)
├── frontend/                   # Código do frontend (Angular)
│   ├── src/app/
│   │   ├── components/         # Componentes (login, register, dashboard, transacoes, transacao-form)
│   │   ├── services/           # Serviços (auth.service, transacao.service)
│   │   ├── guards/             # AuthGuard e afins
│   │   ├── models/             # Modelos TS (transacao, usuario, etc.)
│   │   └── app-routing.module.ts
│   ├── src/assets/             # Assets estáticos
│   └── dist/                   # Build de produção (gerado pelo Angular)
├── scripts/                    # Scripts auxiliares (setup, seeds, tooling)
└── README.md                   # Este documento
```

## Observações
- Os diretórios `backend/` e `frontend/` serão gerados pelos respectivos scaffolds (Quarkus e Angular). Ajuste nomes se usar artefatos diferentes.
- Pastas `target/` (Maven) e `dist/` (Angular) são geradas em build e podem ser ignoradas no versionamento.
- Configurações sensíveis (secrets, JWT key, credenciais de banco) devem ficar em variáveis de ambiente, não em repositório.

## Padrões e Convenções
- Clean Code: nomes claros (classes, métodos, variáveis), funções pequenas e coesas, evitar duplicação, comentários apenas quando o código não for autoexplicativo.
- Versionamento: manter `main` estável; commits pequenos e descritivos.
- Fluxo de branches (gitflow simplificado): `main` (estável) → `dev` (integração) → `feature/<nome-curto>` (trabalho). Abrir PR de feature para dev; merge em main apenas após validação.
- Formatação:
  - Backend: usar formatter padrão do Quarkus/IDE; seguir convenções Java 21; evitar lógica em recursos REST (delegar a services).
  - Frontend: usar `eslint`/`prettier` (quando configurados) e padrões Angular de módulos/serviços/componentes.
- Tratamento de erros:
  - Backend: retornar HTTP status adequados; mensagens genéricas para auth; logs sem dados sensíveis.
  - Frontend: exibir feedback de sucesso/erro; manter estados de loading; não expor stack traces.
- Segurança básica: validar entrada (backend e frontend), senhas com bcrypt, JWT com expiração, CORS restrito em produção, nunca commitar secrets.

