# 🚗 Automanager - Ecossistema de Micro-serviços

## 🛠️ Stack Tecnológica

| Componente   | Tecnologia                              |
|--------------|-----------------------------------------|
| Linguagem    | Java 17                                 |
| Framework    | Spring Boot 3+                          |
| Persistência | Spring Data JPA / Hibernate             |
| Banco de Dados | MySQL (Dockerizado)                   |
| Segurança    | Spring Security + Autenticação JWT Stateless |
| Comunicação  | OpenFeign (Comunicação Síncrona)        |
| Infraestrutura | Docker & Docker Compose               |

---

## 🚀 Como Executar o Projeto (Ambiente Docker)

1. Certifique-se de que o **Docker** está instalado e em execução na sua máquina.
2. Navegue até a pasta raiz do projeto (onde está o arquivo `docker-compose.yml`).
3. Crie a variavel de ambiente (.env) seguindo o exemplo a seguir:
```.env
# Senha de acesso (root) ao banco de dados MySQL
DB_PASS=teste
# Chave criptográfica para assinar e validar os tokens JWT
JWT_SECRET_KEY=ChaveSuperSecretaAutobotsMicroservicos2026VW
```


Depois de criar .env, rode o comando docker

```bash
docker-compose up -d --build
```

4. Aguarde até que os 6 containers (`ms-empresas`, `ms-clientes`, `ms-funcionarios`, `ms-produtos`, `ms-vendas`, `ms-veiculos`) e o banco de dados estejam com o status **Running**.

> **Nota:** Graças ao padrão *Data Seeding* implementado, o sistema já nasce com a **Empresa Matriz (ID 1)** e o **Administrador Mestre** pré-configurados no banco de dados.

---

## 🧪 Guia de Testes da API (End-to-End)

### 🔑 Fase 1: Autenticação (O Passaporte)

Como o sistema utiliza o padrão **Zero-Trust**, todas as requisições exigem um token. Vamos extrair o token do usuário Mestre criado automaticamente.

- **Rota:** `POST http://localhost:8083/api/v1/auth/login`
- **Header:** No Auth
- **Status Esperado:** `200 OK`

```json
{
  "nomeUsuario": "admin",
  "senha": "admin123"
}
```

> ⚠️ **Copie a string gigantesca do token gerado.** Você deverá colá-la na aba `Authorization > Bearer Token` de todas as requisições a seguir.

---

### 🛒 Fase 2: Povoando o Ecossistema (Cadastros)

Nesta fase, testaremos a persistência em cascata de relacionamentos complexos, garantindo que nenhum campo retorne como `null`.

#### 2.1. Cadastrando um Cliente

- **Header:** Bearer Token (Administrador)
- **Rota:** `POST http://localhost:8082/api/v1/clientes/empresa/1`
- **Status Esperado:** `201 Created`

```json
{
  "nome": "Mariana Silva",
  "nomeSocial": "Mari",
  "endereco": {
    "estado": "SP",
    "cidade": "Cruzeiro",
    "bairro": "Vila Suíça",
    "logradouro": "Rua das Primaveras",
    "numero": "42",
    "cep": "12700-000",
    "complemento": "Casa 1"
  },
  "telefones": [
    { "ddd": "12", "numero": "97777-6666" }
  ],
  "documentos": [
    { "tipo": "CPF", "numero": "999.888.777-66" }
  ],
  "emails": [
    { "email": "mariana.silva@email.com" }
  ]
}
```

#### 2.2. Cadastrando um Funcionário (Mecânico)

- **Header:** Bearer Token (Administrador)
- **Rota:** `POST http://localhost:8083/api/v1/funcionarios/empresa/1`
- **Status Esperado:** `201 Created`

```json
{
  "nome": "Carlos Roberto",
  "nomeSocial": "Carlão Mecânico",
  "perfil": "VENDEDOR",
  "credencial": {
    "nomeUsuario": "carlos.mecanico",
    "senha": "123"
  },
  "endereco": {
    "estado": "SP",
    "cidade": "Taubaté",
    "bairro": "Centro",
    "logradouro": "Rua Visconde do Rio Branco",
    "numero": "250",
    "cep": "12020-040",
    "complemento": "Oficina"
  },
  "telefones": [
    { "ddd": "12", "numero": "98888-7777" }
  ],
  "documentos": [
    { "tipo": "CPF", "numero": "111.222.333-44" }
  ],
  "emails": [
    { "email": "carlos.mecanico@vw.com.br" }
  ]
}
```

#### 2.3. Cadastrando o Catálogo (Mercadoria e Serviço)

**Mercadoria:**

- **Rota:** `POST http://localhost:8086/api/v1/mercadorias/empresa/1`

```json
{
  "nome": "Óleo de Motor 5W40 Sintético",
  "descricao": "Óleo original VW para motores MPI e TSI.",
  "valor": 65.90
}
```

**Serviço:**

- **Rota:** `POST http://localhost:8086/api/v1/servicos/empresa/1`

```json
{
  "nome": "Troca de Óleo e Filtros",
  "descricao": "Mão de obra padrão para revisão de rotina.",
  "valor": 150.00
}
```

#### 2.4. Cadastrando a Frota (Veículo)

- **Rota:** `POST http://localhost:8085/api/v1/veiculos/empresa/1`

```json
{
  "placa": "ABC-1234",
  "modelo": "Polo Track 1.0",
  "marca": "Volkswagen",
  "ano": "2025",
  "cor": "Branco",
  "tipoVeiculo": "CARRO"
}
```

#### 2.5. A Transação (Orquestração via OpenFeign)

- **Rota:** `POST http://localhost:8084/api/v1/vendas/empresa/1`

```json
{
  "idCliente": 1,
  "idFuncionario": 1,
  "idsMercadorias": [1, 1, 1, 1],
  "idsServicos": [1]
}
```

---

### 🏭 Fase 2B: Populate Multi-Tenant — Empresa Toyota (ID 2)

Para provar que os relatórios financeiros de uma empresa **não se cruzam** com os de outra, vamos cadastrar um ecossistema completo para a Toyota. Use o **Token do Administrador Mestre** em todos os cadastros abaixo.

#### 2B.1. Criando a Empresa (Toyota)

- **Rota:** `POST http://localhost:8081/api/v1/empresas`

```json
{
  "razaoSocial": "Toyota do Brasil Ltda",
  "nomeFantasia": "Toyota Sorocaba",
  "cnpj": "45.987.001/0001-99",
  "endereco": {
    "estado": "SP",
    "cidade": "Sorocaba",
    "bairro": "Éden",
    "logradouro": "Rodovia Castelo Branco",
    "numero": "Km 92",
    "cep": "18087-101",
    "complemento": "Planta Industrial"
  },
  "telefones": [
    { "ddd": "15", "numero": "3239-4444" }
  ]
}
```

> O sistema atribuirá o **ID 2** para esta empresa.

#### 2B.2. Cadastrando o Gerente (Toyota)

- **Rota:** `POST http://localhost:8083/api/v1/funcionarios/empresa/2`

```json
{
  "nome": "Akio San",
  "nomeSocial": "Sr. Akio",
  "perfil": "GERENTE",
  "credencial": {
    "nomeUsuario": "akio.toyota",
    "senha": "123"
  },
  "endereco": {
    "estado": "SP",
    "cidade": "Sorocaba",
    "bairro": "Campolim",
    "logradouro": "Av. Antônio Carlos Comitre",
    "numero": "500",
    "cep": "18047-620",
    "complemento": "Apto 101"
  },
  "telefones": [
    { "ddd": "15", "numero": "99999-1111" }
  ],
  "documentos": [
    { "tipo": "Passaporte", "numero": "TY123456" }
  ],
  "emails": [
    { "email": "akio@toyota.com.br" }
  ]
}
```

#### 2B.3. Cadastrando o Cliente (Toyota)

- **Rota:** `POST http://localhost:8082/api/v1/clientes/empresa/2`

```json
{
  "nome": "Roberto Silva",
  "nomeSocial": "Beto",
  "endereco": {
    "estado": "SP",
    "cidade": "Itu",
    "bairro": "Centro",
    "logradouro": "Rua Floriano Peixoto",
    "numero": "100",
    "cep": "13300-000",
    "complemento": "Casa"
  },
  "telefones": [
    { "ddd": "11", "numero": "94444-2222" }
  ],
  "documentos": [
    { "tipo": "CPF", "numero": "444.555.666-77" }
  ],
  "emails": [
    { "email": "roberto.itu@email.com" }
  ]
}
```

#### 2B.4. Cadastrando a Frota (Veículo Toyota)

- **Rota:** `POST http://localhost:8085/api/v1/veiculos/empresa/2`

```json
{
  "placa": "TYT-0001",
  "modelo": "Corolla Cross XRE",
  "marca": "Toyota",
  "ano": "2024",
  "cor": "Prata",
  "tipoVeiculo": "CARRO"
}
```

#### 2B.5. Cadastrando o Catálogo (Toyota)

**Mercadoria (Peça):**

- **Rota:** `POST http://localhost:8086/api/v1/mercadorias/empresa/2`

```json
{
  "nome": "Filtro de Ar Condicionado Corolla",
  "descricao": "Filtro de cabine original Toyota.",
  "valor": 110.00
}
```

**Serviço (Mão de obra):**

- **Rota:** `POST http://localhost:8086/api/v1/servicos/empresa/2`

```json
{
  "nome": "Revisão de 10.000 km",
  "descricao": "Inspeção de dezenas de itens e troca de fluidos essenciais.",
  "valor": 450.00
}
```

#### 2B.6. Transação de Venda (A Prova do Relatório)

> Dentro do escopo da Empresa 2, o Cliente (Roberto) é o ID 1 e o Funcionário (Akio) também é o ID 1.

- **Rota:** `POST http://localhost:8084/api/v1/vendas/empresa/2`

```json
{
  "idCliente": 1,
  "idFuncionario": 1,
  "idsMercadorias": [1],
  "idsServicos": [1]
}
```

---

### 📊 Fase 3: Consultas Estratégicas (Entregáveis do Projeto)

Abaixo estão as 5 APIs solicitadas pelos executivos para visualizar dados em tempo real. Utilize o token do **Administrador** para acessá-las.

#### 3.1. Relatório de Clientes

Lista todos os clientes cadastrados por empresa, com informações completas (documentos, telefones, endereço).

- **Método:** `GET`
- **Rota:** `http://localhost:8082/api/v1/clientes/empresa/1`
- **Status Esperado:** `200 OK`

#### 3.2. Relatório de Funcionários

Lista todos os funcionários cadastrados por empresa, com documentos, perfil, telefones, endereço etc.

- **Método:** `GET`
- **Rota:** `http://localhost:8083/api/v1/funcionarios/empresa/1`
- **Status Esperado:** `200 OK`

#### 3.3. Catálogo Corporativo

Lista serviços e mercadorias disponíveis para venda por empresa (informações completas com data de cadastro, nome, descrição, valor).

- **Método:** `GET` (Mercadorias) → `http://localhost:8086/api/v1/mercadorias/empresa/1`
- **Método:** `GET` (Serviços) → `http://localhost:8086/api/v1/servicos/empresa/1`
- **Status Esperado:** `200 OK`

#### 3.4. Relatório de Veículos Atendidos

Lista todos os veículos atendidos por empresa.

- **Método:** `GET`
- **Rota:** `http://localhost:8085/api/v1/veiculos/empresa/1`
- **Status Esperado:** `200 OK`

#### 3.5. Relatório Financeiro por Período

Lista todos os serviços ou peças vendidas por empresa para um determinado período.

- **Método:** `GET`
- **Rota:** `http://localhost:8084/api/v1/vendas/empresa/1?inicio=2026-06-01T00:00:00&fim=2026-06-30T23:59:59`
- **Status Esperado:** `200 OK`

---

### 🔒 Fase 4: Testes de segurança (RBAC)

Para provar que a segurança da arquitetura está baseada em papéis (RBAC):

1. Faça o **Login com o mecânico** recém-criado (`carlos.mecanico` / `123`) na porta `8083`.
2. Pegue o token recebido (perfil `VENDEDOR`).
3. Tente excluir um catálogo batendo com um `DELETE` em `http://localhost:8086/api/v1/mercadorias/1`.

- **Status Esperado:** `403 Forbidden` *(Acesso negado pela arquitetura de segurança)*

---

### 🛡️ Fase 5: Teste de isolamento dos dados 

Como o sistema será utilizado simultaneamente pela Volkswagen e pela Toyota, o maior risco arquitetural seria o vazamento de dados entre as unidades. A nossa arquitetura garante isolamento total através do `idEmpresa`.

Para validar a governança de dados:
1. Faça um `GET` no Catálogo da **Volkswagen**: `http://localhost:8086/api/v1/mercadorias/empresa/1`. Você verá os itens da VW (ex: Óleo Sintético).
2. Faça um `GET` no Catálogo da **Toyota**: `http://localhost:8086/api/v1/mercadorias/empresa/2`. Você verá **apenas** os itens da Toyota (ex: Filtro do Corolla). 

**Status Esperado:** Sucesso total. O relatório financeiro, os clientes, os funcionários e o catálogo da Volkswagen não se cruzam com o ecossistema da Toyota, validando a arquitetura Multi-Tenant.
