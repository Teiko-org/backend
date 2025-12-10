# Backend - API Carambolos

API REST desenvolvida em **Java Spring Boot** seguindo os princípios de **Clean Architecture** para o sistema de gestão da Confeitaria Carambolos. Este backend fornece todos os endpoints necessários para o funcionamento da aplicação frontend, incluindo autenticação JWT, gerenciamento de usuários, pedidos, integração com AWS S3, mensageria com RabbitMQ e cache com Redis.

## 🛠️ Tecnologias Utilizadas

### Core
- **Java 21** - Linguagem de programação
- **Spring Boot 3.4.3** - Framework principal
- **Maven** - Gerenciador de dependências

### Segurança e Autenticação
- **Spring Security** - Autenticação e autorização
- **JWT (JSON Web Token) 0.11.5** - Autenticação stateless
- **Bean Validation** - Validação de dados de entrada

### Persistência de Dados
- **Spring Data JPA** - Abstração de persistência
- **Hibernate** - ORM (Object-Relational Mapping)
- **HikariCP** - Pool de conexões de alta performance
- **MySQL 8** - Banco de dados principal
- **H2 Database** - Banco de dados para testes (opcional)

### Armazenamento e Cache
- **AWS S3 (SDK v2)** - Armazenamento de arquivos na nuvem
- **Redis** - Cache distribuído (usado para cache de endereços)
- **Spring Data Redis** - Integração com Redis

### Mensageria
- **RabbitMQ** - Sistema de mensageria assíncrona
- **Spring AMQP** - Integração com RabbitMQ

### Monitoramento e Observabilidade
- **Spring Boot Actuator** - Endpoints de monitoramento
- **Prometheus** - Coleta de métricas
- **Micrometer** - Facade para métricas

### Documentação e Utilitários
- **Swagger/OpenAPI 3 (SpringDoc 2.4.0)** - Documentação interativa da API
- **OpenPDF 2.0.5** - Geração de PDFs
- **dotenv-java 3.2.0** - Carregamento de variáveis de ambiente

### Containerização
- **Docker** - Containerização da aplicação

## 🚀 Início Rápido

Se você quer rodar o projeto rapidamente, siga estes passos:

```bash
# 1. Clone o repositório
git clone https://github.com/Teiko-org/backend.git
cd backend/carambolos-api

# 2. Configure o arquivo dev.env (veja seção "Configuração das Variáveis de Ambiente" abaixo)
# Copie o exemplo e ajuste as credenciais do banco de dados

# 3. Suba o MySQL via Docker (ou use um MySQL local já instalado)
docker run -d --name mysql-carambolos \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=carambolo_doces \
  -e MYSQL_USER=carambolo_user \
  -e MYSQL_PASSWORD=senha123 \
  -p 3306:3306 mysql:8.0

# 4. Aguarde alguns segundos e execute o script SQL
sleep 10  # Aguardar MySQL inicializar
docker exec -i mysql-carambolos mysql -ucarambolo_user -psenha123 carambolo_doces < src/main/resources/script-bd.sql

# 5. Instale as dependências e rode o projeto
./mvnw clean install
./mvnw spring-boot:run
```

**A aplicação estará disponível em:** http://localhost:8080  
**Swagger UI:** http://localhost:8080/swagger-ui.html

> **Nota:** Se você não tem Docker, veja a seção "Pré-requisitos" abaixo para instalar MySQL localmente.

---

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado os seguintes softwares:

### Obrigatórios

- **Java JDK 21** ou superior
  - Download: [Adoptium](https://adoptium.net/) ou [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
  - Verificar instalação: `java --version`
  
- **Maven 3.6+** (ou use o Maven Wrapper incluído no projeto)
  - Download: [Maven](https://maven.apache.org/download.cgi)
  - Verificar instalação: `mvn --version`
  - **Nota:** O projeto inclui `mvnw` (Maven Wrapper), então o Maven não é obrigatório

- **MySQL 8.0+**
  - Download: [MySQL](https://dev.mysql.com/downloads/mysql/)
  - Verificar instalação: `mysql --version`
  - Alternativa: Use Docker para rodar MySQL sem instalação local

- **Git**
  - Download: [Git](https://git-scm.com/)
  - Verificar instalação: `git --version`

### Opcionais (mas recomendados)

- **Docker** - Para executar dependências (MySQL, Redis, RabbitMQ) via containers
  - Download: [Docker Desktop](https://www.docker.com/products/docker-desktop/)
  - Verificar instalação: `docker --version`

- **RabbitMQ** - Para mensageria assíncrona (pode usar Docker)
  - Download: [RabbitMQ](https://www.rabbitmq.com/download.html)
  - Ou via Docker: `docker run -d -p 5672:5672 -p 15672:15672 rabbitmq:management`

- **Redis** - Para cache de endereços (pode usar Docker)
  - Download: [Redis](https://redis.io/download)
  - Ou via Docker: `docker run -d -p 6379:6379 redis:alpine`

- **Conta AWS** - Para armazenamento de arquivos no S3
  - Criar conta: [AWS](https://aws.amazon.com/)
  - Criar bucket S3 e obter credenciais (Access Key ID e Secret Access Key)

### Verificando as Instalações

Execute os seguintes comandos para verificar se tudo está instalado corretamente:

```bash
# Verificar Java
java --version
# Deve mostrar: openjdk version "21" ou superior

# Verificar Maven (se instalado)
mvn --version
# Deve mostrar: Apache Maven 3.6.x ou superior

# Verificar MySQL
mysql --version
# Deve mostrar: mysql Ver 8.0.x ou superior

# Verificar Git
git --version
# Deve mostrar: git version 2.x.x ou superior

# Verificar Docker (se instalado)
docker --version
# Deve mostrar: Docker version 20.x.x ou superior
```

### Instalação Rápida com Docker (Recomendado para Desenvolvimento)

Se você tem Docker instalado, pode subir todas as dependências de uma vez:

```bash
# MySQL
docker run -d \
  --name mysql-carambolos \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=carambolo_doces \
  -e MYSQL_USER=carambolo_user \
  -e MYSQL_PASSWORD=senha123 \
  -p 3306:3306 \
  mysql:8.0

# Redis
docker run -d --name redis-carambolos -p 6379:6379 redis:alpine

# RabbitMQ
docker run -d \
  --name rabbitmq-carambolos \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:management
```

## 🚀 Instalação e Configuração

Siga estes passos na ordem para configurar o projeto:

### 1. Clone o Repositório

```bash
# Clone o repositório
git clone https://github.com/Teiko-org/backend.git

# Entre no diretório do projeto
cd backend/carambolos-api
```

### 2. Instale as Dependências do Projeto

```bash
# Usando Maven Wrapper (recomendado - não precisa ter Maven instalado)
./mvnw clean install

# Ou usando Maven instalado globalmente
mvn clean install
```

**Nota:** Na primeira execução, o Maven Wrapper baixará o Maven automaticamente e instalará todas as dependências. Isso pode levar alguns minutos.

### 3. Configure o Banco de Dados

#### MySQL (Recomendado para produção)

**Opção A: MySQL Local**

1. **Inicie o MySQL:**
```bash
# Linux/Mac
sudo systemctl start mysql
# ou
sudo service mysql start

# Windows (via serviços)
# Procure por "MySQL" nos Serviços do Windows
```

2. **Acesse o MySQL e crie o banco de dados:**

```bash
mysql -u root -p
```

No prompt do MySQL, execute:

```sql
CREATE DATABASE carambolo_doces;
CREATE USER 'carambolo_user'@'localhost' IDENTIFIED BY 'sua_senha_segura';
GRANT ALL PRIVILEGES ON carambolo_doces.* TO 'carambolo_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

3. **Execute o script de criação das tabelas:**

```bash
# Execute o arquivo script-bd.sql no seu MySQL
mysql -u carambolo_user -p carambolo_doces < src/main/resources/script-bd.sql
```

**Opção B: MySQL via Docker (Mais fácil para desenvolvimento)**

```bash
# Subir MySQL em container
docker run -d \
  --name mysql-carambolos \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=carambolo_doces \
  -e MYSQL_USER=carambolo_user \
  -e MYSQL_PASSWORD=senha123 \
  -p 3306:3306 \
  mysql:8.0

# Aguardar alguns segundos para o MySQL inicializar
sleep 10

# Executar o script SQL
docker exec -i mysql-carambolos mysql -ucarambolo_user -psenha123 carambolo_doces < src/main/resources/script-bd.sql
```

**Verificar se o banco está funcionando:**

```bash
mysql -u carambolo_user -p -e "USE carambolo_doces; SHOW TABLES;"
```

#### H2 Database (Para desenvolvimento/testes)

Para usar H2 (mais rápido para desenvolvimento), descomente as linhas no `application.properties`:

```properties
spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:mem:banco
```

### 4. Configure as Variáveis de Ambiente

Crie um arquivo `dev.env` na raiz do projeto `carambolos-api` (mesmo nível do `pom.xml`) com as seguintes variáveis:

```env
# Configurações do Banco de Dados MySQL
DB_USERNAME=carambolo_user
DB_PASSWORD=sua_senha_segura
DB_URL=jdbc:mysql://localhost:3306/carambolo_doces

# Configurações JWT
JWT_VALIDITY=3600000
JWT_SECRET=minha_chave_secreta_super_segura_com_32_caracteres_ou_mais

# Configurações AWS S3 (opcional)
AWS_S3_BUCKET_NAME=teiko-bucket-pj
AWS_REGION=us-east-1
AWS_ACCESS_KEY_ID=sua_access_key_id
AWS_SECRET_ACCESS_KEY=sua_secret_access_key
AWS_SESSION_TOKEN=seu_session_token  # Opcional, apenas para credenciais temporárias

# Configurações Redis (opcional - para cache de endereços)
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=  # Opcional
CACHE_TYPE=redis  # Use 'none' para desabilitar cache

# Configurações RabbitMQ (opcional - para mensageria)
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest
RABBITMQ_CONCURRENCY=2
RABBITMQ_MAX_CONCURRENCY=4
RABBITMQ_PREFETCH=10
```

#### 3.1 Criptografia de dados (PII) — CRYPTO_SECRET_B64

Usamos criptografia AES‑256‑GCM para campos sensíveis (ex.: nome, telefone, endereço). Defina a chave secreta via variável de ambiente `CRYPTO_SECRET_B64` (Base64 de 32 bytes):

- Windows (PowerShell)
  1. Gerar chave:
     ```powershell
     $rng = [System.Security.Cryptography.RandomNumberGenerator]::Create(); $b = New-Object byte[] 32; $rng.GetBytes($b); [Convert]::ToBase64String($b)
     ```
  2. Usar na sessão atual:
     ```powershell
     $env:CRYPTO_SECRET_B64 = "<COLE AQUI O BASE64>"
     ```
  3. Validar (deve imprimir 32):
     ```powershell
     [Convert]::FromBase64String($env:CRYPTO_SECRET_B64).Length
     ```

- macOS/Linux (bash/zsh)
  1. Gerar chave:
     ```bash
     openssl rand -base64 32
     ```
  2. Usar na sessão atual:
     ```bash
     export CRYPTO_SECRET_B64="<COLE AQUI O BASE64>"
     ```
  3. Validar (deve imprimir 32):
     ```bash
     echo -n "$CRYPTO_SECRET_B64" | base64 -d | wc -c
     ```

- Persistência (opcional):
  - Windows: `setx CRYPTO_SECRET_B64 "<BASE64>"` e reabra o terminal/IDE
  - Linux: adicione `export CRYPTO_SECRET_B64="<BASE64>"` no `~/.bashrc`/`~/.zshrc`

Alternativa sem variável de ambiente (apenas para desenvolvimento):
```bash
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-DCRYPTO_SECRET_B64=<BASE64>"
```

### 5. Configure Serviços Opcionais (Redis e RabbitMQ)

Se você quiser usar cache (Redis) ou mensageria (RabbitMQ), configure-os:

**Redis (Cache):**
```bash
# Via Docker
docker run -d --name redis-carambolos -p 6379:6379 redis:alpine

# Ou instale localmente e inicie
redis-server
```

**RabbitMQ (Mensageria):**
```bash
# Via Docker
docker run -d \
  --name rabbitmq-carambolos \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:management

# Interface web do RabbitMQ: http://localhost:15672
# Usuário padrão: guest / Senha: guest
```

**Nota:** Se não configurar Redis ou RabbitMQ, a aplicação funcionará normalmente, apenas sem essas funcionalidades.

## 🎯 Como Rodar o Projeto

Agora que tudo está configurado, você pode rodar o projeto:

### Modo Desenvolvimento (Recomendado)

```bash
# Certifique-se de estar no diretório carambolos-api
cd backend/carambolos-api

# Usando Maven Wrapper (recomendado)
./mvnw spring-boot:run

# Ou usando Maven instalado globalmente
mvn spring-boot:run
```

**O que acontece:**
- A aplicação compila automaticamente
- Inicia o servidor na porta 8080
- Conecta ao banco de dados MySQL
- Carrega as variáveis do arquivo `dev.env`
- Expõe os endpoints da API

**Primeira execução pode demorar mais** devido ao download de dependências e compilação.

### Executar a partir do JAR

Se você já compilou o projeto e quer executar apenas o JAR:

```bash
# 1. Gerar o JAR (compilar o projeto)
./mvnw clean package

# 2. Executar o JAR
java -jar target/carambolos-api-0.0.1-SNAPSHOT.jar
```

**Vantagens do JAR:**
- Execução mais rápida (já está compilado)
- Pode ser executado em qualquer máquina com Java instalado
- Útil para testes de produção local

### 🌐 Acesso à Aplicação

- **API Base:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **API Docs:** http://localhost:8080/v3/api-docs
- **H2 Console:** http://localhost:8080/h2-console (se configurado)
- **Actuator Health:** http://localhost:8080/actuator/health
- **Actuator Metrics:** http://localhost:8080/actuator/metrics
- **Prometheus Metrics:** http://localhost:8080/actuator/prometheus

## 📊 Documentação da API

A API possui documentação completa via **Swagger/OpenAPI**. Após iniciar a aplicação, acesse:

👉 **http://localhost:8080/swagger-ui.html**

### Principais Endpoints

```
GET    /api/usuarios          # Listar usuários
POST   /api/usuarios          # Criar usuário
POST   /api/auth/login        # Autenticação
GET    /api/pedidos           # Listar pedidos
POST   /api/pedidos           # Criar pedido
PUT    /api/pedidos/{id}      # Atualizar pedido
DELETE /api/pedidos/{id}      # Deletar pedido
```

## 🔗 Integração com Frontend

Esta API se conecta com o frontend React localizado no repositório:

📌 **Frontend:** [https://github.com/Teiko-org/frontend](https://github.com/Teiko-org/frontend)

### Configuração CORS

O CORS já está configurado para aceitar requisições do frontend em `http://localhost:5173` (Vite dev server).

### Headers Necessários

Para endpoints protegidos, inclua o header:
```
Authorization: Bearer <seu_jwt_token>
```

## 📁 Estrutura do Projeto (Clean Architecture)

O projeto segue os princípios de **Clean Architecture**, organizando o código em camadas bem definidas:

```
carambolos-api/
├── src/
│   ├── main/
│   │   ├── java/com/carambolos/carambolosapi/
│   │   │   ├── domain/                    # Camada de Domínio
│   │   │   │   ├── entity/                # Entidades de domínio (sem dependências externas)
│   │   │   │   └── enums/                 # Enumeradores
│   │   │   │
│   │   │   ├── application/               # Camada de Aplicação
│   │   │   │   ├── usecases/              # Casos de uso (lógica de negócio)
│   │   │   │   ├── gateways/              # Interfaces (contratos) para infraestrutura
│   │   │   │   └── exception/             # Exceções de domínio/aplicação
│   │   │   │
│   │   │   ├── infrastructure/            # Camada de Infraestrutura
│   │   │   │   ├── persistence/           # Persistência de dados
│   │   │   │   │   ├── entity/            # Entidades JPA
│   │   │   │   │   ├── jpa/               # Repositórios Spring Data JPA
│   │   │   │   │   └── projection/        # Projeções de dados
│   │   │   │   ├── gateways/              # Implementações dos gateways
│   │   │   │   │   ├── impl/              # Implementações concretas
│   │   │   │   │   └── mapperEntity/      # Mappers Entity ↔ Domain
│   │   │   │   ├── web/                   # Camada web (controllers, DTOs)
│   │   │   │   └── messaging/             # Mensageria (RabbitMQ)
│   │   │   │       ├── listeners/         # Consumidores de filas
│   │   │   │       ├── publishers/        # Publicadores de mensagens
│   │   │   │       └── dto/               # DTOs de mensagens
│   │   │   │
│   │   │   ├── system/                    # Configurações do Sistema
│   │   │   │   ├── config/                # Configurações Spring
│   │   │   │   ├── security/              # Configurações de segurança
│   │   │   │   ├── swagger/               # Configuração Swagger
│   │   │   │   └── web/                   # Configurações web (CORS, etc.)
│   │   │   │
│   │   │   └── CarambolosApiApplication.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties         # Configurações principais
│   │       ├── application-prod.properties   # Configurações de produção
│   │       └── script-bd.sql                 # Script do banco de dados
│   │
│   └── test/                            # Testes unitários e de integração
│
├── docker/
│   └── Dockerfile                       # Dockerfile para containerização
│
├── dev.env                              # Variáveis de ambiente (criar)
├── pom.xml                              # Dependências Maven
├── mvnw                                 # Maven Wrapper (Unix)
├── mvnw.cmd                             # Maven Wrapper (Windows)
├── CLEAN-ARCH-REFATORACAO-SIMPLES.md    # Documentação da arquitetura
└── README.md                            # Este arquivo
```

### Princípios da Arquitetura

- **Domain**: Contém as entidades e regras de negócio puras, sem dependências de frameworks
- **Application**: Contém os casos de uso e interfaces (gateways) que definem contratos
- **Infrastructure**: Implementa as interfaces definidas na camada de aplicação (JPA, Web, Messaging)
- **System**: Configurações e aspectos transversais (segurança, documentação, etc.)

Para mais detalhes sobre a arquitetura, consulte: `CLEAN-ARCH-REFATORACAO-SIMPLES.md`

## 🧪 Executando Testes

```bash
# Executar todos os testes
./mvnw test

# Executar testes com relatório
./mvnw test jacoco:report
```

## 🔧 Configurações Adicionais

### Profiles Spring

```bash
# Desenvolvimento
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Produção
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

### Configuração do AWS S3

1. Crie um bucket S3 na AWS
2. Configure as credenciais AWS (Access Key ID e Secret Access Key)
3. Configure as variáveis no arquivo `dev.env`:
   - `AWS_S3_BUCKET_NAME`: Nome do bucket
   - `AWS_REGION`: Região do bucket (ex: `us-east-1`)
   - `AWS_ACCESS_KEY_ID`: Sua Access Key ID
   - `AWS_SECRET_ACCESS_KEY`: Sua Secret Access Key
   - `AWS_SESSION_TOKEN`: (Opcional) Para credenciais temporárias

**Nota:** A aplicação também suporta usar `DefaultCredentialsProvider`, que busca credenciais automaticamente em variáveis de ambiente padrão da AWS ou em arquivos de credenciais (`~/.aws/credentials`).

### Configuração do Redis (Cache)

O Redis é usado para cache de endereços. Para ativar:

1. Instale e inicie o Redis:
```bash
# Docker
docker run -d -p 6379:6379 redis:alpine

# Ou instale localmente (Linux)
sudo apt-get install redis-server
sudo systemctl start redis
```

2. Configure as variáveis no `dev.env`:
   - `REDIS_HOST`: Host do Redis (padrão: `localhost`)
   - `REDIS_PORT`: Porta do Redis (padrão: `6379`)
   - `REDIS_PASSWORD`: (Opcional) Senha do Redis
   - `CACHE_TYPE`: Defina como `redis` para ativar, ou `none` para desabilitar

**Nota:** Se o Redis não estiver disponível, a aplicação continuará funcionando normalmente, apenas sem cache.

### Configuração do RabbitMQ (Mensageria)

Para usar mensageria assíncrona:

1. Instale e inicie o RabbitMQ:
```bash
# Docker
docker run -d -p 5672:5672 -p 15672:15672 rabbitmq:management

# Ou instale localmente (Linux)
sudo apt-get install rabbitmq-server
sudo systemctl start rabbitmq-server
```

2. Configure as variáveis no `dev.env`:
   - `RABBITMQ_HOST`: Host do RabbitMQ (padrão: `localhost`)
   - `RABBITMQ_PORT`: Porta do RabbitMQ (padrão: `5672`)
   - `RABBITMQ_USERNAME`: Usuário (padrão: `guest`)
   - `RABBITMQ_PASSWORD`: Senha (padrão: `guest`)
   - `RABBITMQ_CONCURRENCY`: Número de consumidores (padrão: `2`)
   - `RABBITMQ_MAX_CONCURRENCY`: Máximo de consumidores (padrão: `4`)
   - `RABBITMQ_PREFETCH`: Mensagens pré-buscadas (padrão: `10`)

### Logs

Os logs da aplicação são configurados automaticamente. Para personalizar:

```properties
# application.properties
logging.level.com.carambolos=DEBUG
logging.level.org.springframework.security=DEBUG
```

## 🐛 Solução de Problemas

### Erro de conexão com MySQL

```bash
# Verificar se o MySQL está rodando
sudo systemctl status mysql

# Verificar se o usuário e banco existem
mysql -u root -p -e "SHOW DATABASES; SELECT User FROM mysql.user;"
```

### Problemas com JWT

- Verifique se o `JWT_SECRET` tem pelo menos 32 caracteres
- Confirme se o `JWT_VALIDITY` está em milissegundos

### Erro de CORS

```java
// Se precisar ajustar CORS, adicione em WebConfig:
@CrossOrigin(origins = "http://localhost:5173")
```

### Port já em uso

```bash
# Verificar qual processo está usando a porta 8080
lsof -i :8080

# Ou configurar porta diferente
./mvnw spring-boot:run -Dserver.port=8081
```

## 📚 Dependências Principais

| Dependência | Versão | Descrição |
|-------------|--------|-----------|
| Spring Boot | 3.4.3 | Framework principal |
| Spring Security | 6.x | Segurança e autenticação |
| Spring Data JPA | 3.x | Abstração de persistência |
| Spring Data Redis | 3.x | Integração com Redis |
| Spring AMQP | 3.x | Integração com RabbitMQ |
| Spring Boot Actuator | 3.4.3 | Monitoramento e métricas |
| MySQL Connector | 8.x | Driver MySQL |
| HikariCP | 5.x | Pool de conexões |
| Hibernate | 6.x | ORM |
| JWT (jjwt) | 0.11.5 | JSON Web Tokens |
| SpringDoc OpenAPI | 2.4.0 | Documentação API (Swagger) |
| AWS SDK S3 | 2.25.70 | Cliente AWS S3 |
| Micrometer Prometheus | 1.x | Métricas Prometheus |
| Redis (Lettuce) | 6.x | Cliente Redis |
| RabbitMQ | 5.x | Cliente RabbitMQ |
| OpenPDF | 2.0.5 | Geração de PDFs |
| dotenv-java | 3.2.0 | Carregamento de variáveis de ambiente |
| Bean Validation | 3.x | Validação de dados |

## 🔄 Scripts Maven Úteis

```bash
# Compilar sem executar testes
./mvnw clean compile -DskipTests

# Gerar JAR para produção
./mvnw clean package -Pprod

# Limpar target e dependencies cache
./mvnw dependency:purge-local-repository

# Verificar dependências desatualizadas
./mvnw versions:display-dependency-updates
```

## 📊 Monitoramento e Métricas

A aplicação expõe endpoints de monitoramento via **Spring Boot Actuator** e métricas para **Prometheus**.

### Endpoints do Actuator

- **Health Check:** `GET /actuator/health` - Status da aplicação e dependências
- **Info:** `GET /actuator/info` - Informações da aplicação
- **Metrics:** `GET /actuator/metrics` - Lista de métricas disponíveis
- **Prometheus:** `GET /actuator/prometheus` - Métricas no formato Prometheus

### Exemplo de Health Check

```json
{
  "status": "UP",
  "components": {
    "db": { "status": "UP" },
    "redis": { "status": "UP" },
    "rabbit": { "status": "UP" }
  }
}
```

### Integração com Prometheus

As métricas podem ser coletadas pelo Prometheus configurando um job:

```yaml
scrape_configs:
  - job_name: 'carambolos-api'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:8080']
```

## 🚀 Deploy

### Variáveis de Ambiente para Produção

```env
# Banco de Dados
DB_URL=jdbc:mysql://seu-servidor-mysql:3306/carambolo_doces
DB_USERNAME=usuario_producao
DB_PASSWORD=senha_segura_producao

# JWT
JWT_VALIDITY=3600000
JWT_SECRET=chave_muito_segura_para_producao_com_mais_de_32_caracteres

# AWS S3
AWS_S3_BUCKET_NAME=seu-bucket-producao
AWS_REGION=us-east-1
AWS_ACCESS_KEY_ID=sua_access_key_producao
AWS_SECRET_ACCESS_KEY=sua_secret_key_producao
AWS_SESSION_TOKEN=  # Opcional, apenas para credenciais temporárias

# Redis (Cache)
REDIS_HOST=seu-redis-host
REDIS_PORT=6379
REDIS_PASSWORD=senha_redis_segura
CACHE_TYPE=redis

# RabbitMQ (Mensageria)
RABBITMQ_HOST=seu-rabbitmq-host
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=usuario_rabbitmq
RABBITMQ_PASSWORD=senha_rabbitmq_segura

# Criptografia (PII)
CRYPTO_SECRET_B64=base64_de_32_bytes_para_aes_256
```

### Build para Produção

#### Opção 1: JAR Executável

```bash
./mvnw clean package -Pprod -DskipTests
```

#### Opção 2: Docker Image

```bash
# Build da imagem
docker build -f docker/Dockerfile -t carambolos-api:latest .

# Tag para registry (exemplo)
docker tag carambolos-api:latest seu-registry/carambolos-api:v1.0.0
docker push seu-registry/carambolos-api:v1.0.0
```

### Deploy em VM Ubuntu (systemd)

1. Defina as variáveis de ambiente de produção (incluindo `CRYPTO_SECRET_B64`) no systemd ou `/etc/environment`.

2. Exemplo de unit file `carambolos-api.service`:
```ini
[Unit]
Description=Carambolos API
After=network.target

[Service]
WorkingDirectory=/opt/carambolos-api
ExecStart=/usr/bin/java -jar /opt/carambolos-api/carambolos-api-0.0.1-SNAPSHOT.jar
User=www-data
Restart=always
Environment=DB_URL=jdbc:mysql://10.0.0.5:3306/carambolo_doces
Environment=DB_USERNAME=usuario_producao
Environment=DB_PASSWORD=senha_segura_producao
Environment=JWT_SECRET=chave_muito_segura_para_producao
Environment=AWS_S3_BUCKET_NAME=seu-bucket-producao
Environment=AWS_REGION=us-east-1
Environment=AWS_ACCESS_KEY_ID=sua_access_key
Environment=AWS_SECRET_ACCESS_KEY=sua_secret_key
Environment=REDIS_HOST=10.0.0.6
Environment=REDIS_PORT=6379
Environment=CACHE_TYPE=redis
Environment=RABBITMQ_HOST=10.0.0.7
Environment=RABBITMQ_PORT=5672
Environment=RABBITMQ_USERNAME=usuario_rabbitmq
Environment=RABBITMQ_PASSWORD=senha_rabbitmq
Environment=CRYPTO_SECRET_B64=<BASE64_DE_32_BYTES>

[Install]
WantedBy=multi-user.target
```

3. Copie o JAR para `/opt/carambolos-api`, habilite e inicie:
```bash
sudo mkdir -p /opt/carambolos-api
sudo cp target/carambolos-api-0.0.1-SNAPSHOT.jar /opt/carambolos-api/
sudo cp carambolos-api.service /etc/systemd/system/
sudo systemctl daemon-reload
sudo systemctl enable carambolos-api
sudo systemctl start carambolos-api
sudo systemctl status carambolos-api
```

Observação: você também pode definir `CRYPTO_SECRET_B64` em `/etc/environment`:
```bash
echo 'CRYPTO_SECRET_B64="<BASE64>"' | sudo tee -a /etc/environment
```
e depois `sudo systemctl restart carambolos-api`.

### Troubleshooting (CRYPTO_SECRET_B64)
- Erro: "CRYPTO_SECRET_B64 não definido": variável ausente — defina conforme acima.
- Erro ao descriptografar: verifique se o Base64 tem 32 bytes após decodificar e se é a mesma chave usada para cifrar dados existentes.

---

## 🐳 Executando com Docker

### Build da Imagem

```bash
# Na raiz do projeto backend
docker build -f docker/Dockerfile -t carambolos-api:latest .
```

### Executar Container

```bash
# Executar com variáveis de ambiente
docker run -d \
  -p 8080:8080 \
  -e DB_URL=jdbc:mysql://host.docker.internal:3306/carambolo_doces \
  -e DB_USERNAME=usuario \
  -e DB_PASSWORD=senha \
  -e JWT_SECRET=sua_chave_secreta \
  -e AWS_S3_BUCKET_NAME=seu-bucket \
  -e AWS_REGION=us-east-1 \
  --name carambolos-api \
  carambolos-api:latest
```

### Docker Compose

Para executar a aplicação completa (API + Worker + dependências), consulte o docker-compose em `infra/aws-ec2`:

```bash
cd infra/aws-ec2
docker compose -f docker-compose.backend.yml up -d --build
```

## 🧵 Execução: API + Worker (RabbitMQ)

O projeto suporta execução em modo **API** (endpoints REST) e modo **Worker** (consumidores de filas RabbitMQ). O consumidor de filas está sob o profile Spring `worker`, permitindo rodar a API e o Worker como processos separados.

### Arquitetura de Mensageria

- **API**: Expõe endpoints REST e publica mensagens nas filas RabbitMQ
- **Worker**: Consome mensagens das filas e processa tarefas assíncronas
- **RabbitMQ**: Broker de mensageria que gerencia as filas

### Execução Local (Maven)

```bash
# Terminal 1: API (sem profile worker)
./mvnw spring-boot:run

# Terminal 2: Worker (apenas listeners)
./mvnw spring-boot:run -Dspring-boot.run.profiles=worker
```

**Requisitos:**
- RabbitMQ acessível (veja configuração acima ou docker-compose em `infra/aws-ec2`)
- Variáveis de ambiente de RabbitMQ podem ser definidas via `dev.env` ou variáveis de ambiente do sistema

### Execução com Docker Compose

No diretório `infra/aws-ec2`:

```bash
# Sobe MySQL, RabbitMQ, Redis, API e Worker
docker compose -f docker-compose.backend.yml up -d --build

# Ver logs da API
docker compose -f docker-compose.backend.yml logs -f api

# Ver logs do Worker
docker compose -f docker-compose.backend.yml logs -f worker

# Ver logs do RabbitMQ
docker compose -f docker-compose.backend.yml logs -f rabbitmq
```

**Portas:**
- API: `8080` (endpoints REST)
- Worker: Não expõe portas (apenas consome filas)
- RabbitMQ Management: `15672` (interface web)
- Redis: `6379`

## 👥 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/NovaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/NovaFeature`)
5. Abra um Pull Request

## 📝 Licença

Este projeto foi desenvolvido como parte do curso da SPTech - 3º semestre.

---

## 🆘 Suporte

Se encontrar problemas:

1. Verifique se todas as variáveis de ambiente estão configuradas
2. Confirme se o banco de dados está rodando e acessível
3. Consulte os logs da aplicação
4. Verifique a documentação do Swagger
5. Abra uma issue no repositório

Para mais informações sobre o frontend que consome esta API, consulte: [Frontend README](https://github.com/Teiko-org/frontend)
