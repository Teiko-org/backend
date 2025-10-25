# Backend - API Carambolos

API REST desenvolvida em **Java Spring Boot** para o sistema de gestão da Confeitaria Carambolos. Este backend fornece todos os endpoints necessários para o funcionamento da aplicação frontend, incluindo autenticação JWT, gerenciamento de usuários, pedidos e integração com Azure Storage.

## 🛠️ Tecnologias Utilizadas

- **Java 21** - Linguagem de programação
- **Spring Boot 3.4.3** - Framework principal
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **Hibernate** - ORM (Object-Relational Mapping)
- **MySQL 8** - Banco de dados principal
- **H2 Database** - Banco de dados para testes (opcional)
- **JWT (JSON Web Token)** - Autenticação stateless
- **Azure Blob Storage** - Armazenamento de arquivos
- **Swagger/OpenAPI 3** - Documentação da API
- **Maven** - Gerenciador de dependências
- **OpenPDF** - Geração de PDFs

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- [Java JDK 21](https://adoptium.net/) ou superior
- [Maven 3.6+](https://maven.apache.org/download.cgi) (ou use o wrapper incluído)
- [MySQL 8.0+](https://dev.mysql.com/downloads/mysql/)
- [Git](https://git-scm.com/)
- Conta no [Azure Storage](https://azure.microsoft.com/pt-br/services/storage/) (opcional, mas recomendado para produção)

### Verificando as versões instaladas

```bash
java --version
mvn --version
mysql --version
git --version
```

## 🚀 Instalação e Configuração

### 1. Clone o repositório

```bash
git clone https://github.com/Teiko-org/backend.git
cd backend/carambolos-api
```

### 2. Configuração do Banco de Dados

#### MySQL (Recomendado para produção)

1. **Criar o banco de dados:**

```sql
CREATE DATABASE carambolo_doces;
CREATE USER 'carambolo_user'@'localhost' IDENTIFIED BY 'sua_senha_segura';
GRANT ALL PRIVILEGES ON carambolo_doces.* TO 'carambolo_user'@'localhost';
FLUSH PRIVILEGES;
```

2. **Executar o script de criação das tabelas:**

```bash
# Execute o arquivo script-bd.sql no seu MySQL
mysql -u carambolo_user -p carambolo_doces < src/main/resources/script-bd.sql
```

#### H2 Database (Para desenvolvimento/testes)

Para usar H2 (mais rápido para desenvolvimento), descomente as linhas no `application.properties`:

```properties
spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:mem:banco
```

### 3. Configuração das Variáveis de Ambiente

Crie um arquivo `dev.env` na raiz do projeto `carambolos-api` com as seguintes variáveis:

```env
# Configurações do Banco de Dados MySQL
DB_USERNAME=carambolo_user
DB_PASSWORD=sua_senha_segura
DB_URL=jdbc:mysql://localhost:3306/carambolo_doces

# Configurações JWT
JWT_VALIDITY=3600000
JWT_SECRET=minha_chave_secreta_super_segura_com_32_caracteres_ou_mais

# Configurações Azure Storage (opcional)
AZURE_STORAGE_CONNECTION_STRING=DefaultEndpointsProtocol=https;AccountName=...
AZURE_STORAGE_CONTAINER_NAME=carambolo-files
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

### 4. Instalação das Dependências

```bash
# Usando Maven Wrapper (recomendado)
./mvnw clean install

# Ou usando Maven instalado globalmente
mvn clean install
```

## 🎯 Como Rodar o Projeto

### Modo Desenvolvimento

```bash
# Usando Maven Wrapper
./mvnw spring-boot:run

# Ou usando Maven
mvn spring-boot:run
```

### Executar a partir do JAR

```bash
# Gerar o JAR
./mvnw clean package

# Executar o JAR
java -jar target/carambolos-api-0.0.1-SNAPSHOT.jar
```

### 🌐 Acesso à Aplicação

- **API Base:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **API Docs:** http://localhost:8080/v3/api-docs
- **H2 Console:** http://localhost:8080/h2-console (se configurado)

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

## 📁 Estrutura do Projeto

```
carambolos-api/
├── src/
│   ├── main/
│   │   ├── java/com/carambolos/carambolosapi/
│   │   │   ├── controller/          # Controllers REST
│   │   │   ├── service/             # Lógica de negócio
│   │   │   ├── repository/          # Repositórios JPA
│   │   │   ├── model/               # Entidades/Models
│   │   │   ├── exception/           # Tratamento de exceções
│   │   │   ├── utils/               # Utilitários
│   │   │   └── CarambolosApiApplication.java
│   │   └── resources/
│   │       ├── application.properties    # Configurações
│   │       └── script-bd.sql            # Script do banco
│   └── test/                        # Testes unitários
├── dev.env                          # Variáveis de ambiente (criar)
├── pom.xml                          # Dependências Maven
├── mvnw                             # Maven Wrapper (Unix)
├── mvnw.cmd                         # Maven Wrapper (Windows)
└── README.md                        # Este arquivo
```

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

### Configuração do Azure Storage

1. Crie uma conta de Storage no Azure
2. Obtenha a connection string
3. Configure as variáveis no arquivo `dev.env`
4. Crie um container chamado `carambolo-files`

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
| Spring Security | 6.x | Segurança |
| MySQL Connector | 8.x | Driver MySQL |
| JWT | 0.11.5 | JSON Web Tokens |
| Swagger | 2.4.0 | Documentação API |
| Azure Storage | 12.25.0 | Armazenamento de arquivos |

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

## 🚀 Deploy

### Variáveis de Ambiente para Produção

```env
DB_URL=jdbc:mysql://seu-servidor-mysql:3306/carambolo_doces
DB_USERNAME=usuario_producao
DB_PASSWORD=senha_segura_producao
JWT_SECRET=chave_muito_segura_para_producao_com_mais_de_32_caracteres
AZURE_STORAGE_CONNECTION_STRING=connection_string_producao
CRYPTO_SECRET_B64=base64_de_32_bytes_para_aes_256
```

### Build para Produção

```bash
./mvnw clean package -Pprod -DskipTests
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
Environment=AZURE_STORAGE_CONNECTION_STRING=connection_string_producao
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

## 🧵 Execução: API + Worker (RabbitMQ)

O consumidor de filas está sob o profile Spring `worker`. Assim, rodamos a API e o Worker como processos separados.

### Local (Maven)

```bash
# Terminal 1: API (sem profile worker)
./mvnw spring-boot:run

# Terminal 2: Worker (apenas listeners)
./mvnw spring-boot:run -Dspring-boot.run.profiles=worker
```

- Necessário ter RabbitMQ acessível (veja docker-compose em `infra/aws-ec2`).
- Variáveis de ambiente de Rabbit podem ser definidas via `application.properties` ou ambiente (`RABBITMQ_HOST`, etc.).

### Docker Compose (API + Worker)

No diretório `infra/aws-ec2`:

```bash
# Sobe MySQL, RabbitMQ, API e Worker
docker compose -f docker-compose.backend.yml up -d --build

# Ver logs
docker compose -f docker-compose.backend.yml logs -f api
docker compose -f docker-compose.backend.yml logs -f worker
```

A API expõe a porta `8080`. O worker não expõe portas; ele apenas consome filas.

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
