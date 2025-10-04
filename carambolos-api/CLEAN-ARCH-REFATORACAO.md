## Guia de Refatoração para Clean Architecture (Fornadas e Pedido de Fornada)

Objetivo: refatorar os fluxos de Fornadas e de Pedido de Fornada para Clean Architecture (Ports & Adapters) sem mudar comportamento, rotas, DTOs ou persistência.

### Como ler este guia (nomenclatura e pastas IGUAIS às do projeto)
- Usaremos APENAS os nomes de pastas que já existem no seu repo:
  - `domain/entity`: entidades de domínio (limpas, sem JPA).
  - `application/usecases`: casos de uso (interfaces + classes que implementam a regra).
  - `application/gateways`: interfaces para integrações (banco/serviços externos).
  - `infrastructure/web`: controllers e DTOs HTTP.
  - `infrastructure/persistence/jpa`: entities JPA e repositórios Spring Data JPA.
  - `infrastructure/gateways`: implementações dos gateways usando JPA/HTTP/etc.
  - `main`: classes de configuração para criar os beans que ligam tudo.
- Termos:
  - “Use Case” = classe em `application/usecases` que contém a regra de aplicação (equivale ao seu `FornadaService`/`PedidoFornadaService`, mas desacoplado de repositórios concretos).
  - “Gateway” = interface em `application/gateways` que o Use Case usa para acessar banco/externos. A implementação real fica em `infrastructure/gateways` usando JPA.
  - NÃO usaremos “ports in/out” no texto para evitar confusão.
  - Dependências: `infrastructure → application → domain`.

### Estrutura de pacotes (módulos lógicos dentro da sua estrutura)
```
com.carambolos.carambolosapi.fornadas
  ├── domain
  ├── application
  │   ├── port
  │   │   ├── in
  │   │   └── out
  │   └── usecase
  ├── infrastructure
  │   ├── controllers
  │   ├── gateways
  │   └── persistence
  └── main

com.carambolos.carambolosapi.pedidosfornada
  (mesma estrutura)
```

### Estrutura de pastas EXATA (mantendo padrão atual)
Use estes diretórios sob `src/main/java/com/carambolos/carambolosapi` para alinhar com sua estrutura atual (igual ao repo de referência):

```
domain/
  entity/
    fornadas/Fornada.java                  (DOMÍNIO limpo)
    pedidosfornada/PedidoFornada.java      (DOMÍNIO limpo)

application/
  gateways/
    fornadas/FornadaGateway.java
    pedidosfornada/PedidoFornadaGateway.java
    pedidosfornada/FornadaDaVezGateway.java
    pedidosfornada/EnderecoGateway.java
    pedidosfornada/UsuarioGateway.java
  usecases/
    fornadas/ (ports IN + interactors)
      CreateFornadaUseCase.java
      CreateFornadaInteractor.java
      AtualizarFornadaUseCase.java
      AtualizarFornadaInteractor.java
      EncerrarFornadaUseCase.java
      EncerrarFornadaInteractor.java
      ListarFornadasAtivasUseCase.java
      ListarFornadasAtivasInteractor.java
      ListarTodasFornadasUseCase.java
      ListarTodasFornadasInteractor.java
      ListarFornadasPorMesAnoUseCase.java
      ListarFornadasPorMesAnoInteractor.java
      BuscarFornadaPorIdUseCase.java
      BuscarFornadaPorIdInteractor.java
      BuscarFornadaMaisRecenteUseCase.java
      BuscarFornadaMaisRecenteInteractor.java
      BuscarProximaFornadaUseCase.java
      BuscarProximaFornadaInteractor.java
    pedidosfornada/ (ports IN + interactors)
      CreatePedidoFornadaUseCase.java
      CreatePedidoFornadaInteractor.java
      AtualizarPedidoFornadaUseCase.java
      AtualizarPedidoFornadaInteractor.java
      ExcluirPedidoFornadaUseCase.java
      ExcluirPedidoFornadaInteractor.java
      ListarPedidosFornadaUseCase.java
      ListarPedidosFornadaInteractor.java
      BuscarPedidoFornadaPorIdUseCase.java
      BuscarPedidoFornadaPorIdInteractor.java

infrastructure/
  web/
    controllers/
      fornadas/FornadaController.java                (refatorado para use cases)
      pedidosfornada/PedidoFornadaController.java    (opcional agora; ou manter dentro do de Fornadas)
    request/ (reaproveitar seus DTOs atuais)
    response/ (reaproveitar seus DTOs atuais)
  persistence/
    fornadas/
      FornadaEntity.java
      FornadaRepository.java
    pedidosfornada/
      PedidoFornadaEntity.java
      PedidoFornadaRepository.java
  gateways/
    fornadas/
      FornadaEntityMapper.java
      FornadaRepositoryGateway.java
    pedidosfornada/
      PedidoFornadaEntityMapper.java
      PedidoFornadaRepositoryGateway.java

main/
  FornadaConfig.java
  PedidoFornadaConfig.java
```

Pacotes (package) recomendados em cada arquivo seguem o caminho do diretório. Exemplos:
- `domain/entity/fornadas/Fornada.java` → `package com.carambolos.carambolosapi.domain.entity.fornadas;`
- `application/gateways/fornadas/FornadaGateway.java` → `package com.carambolos.carambolosapi.application.gateways.fornadas;`
- `application/usecases/fornadas/CreateFornadaUseCase.java` → `package com.carambolos.carambolosapi.application.usecases.fornadas;`
- `infrastructure/persistence/fornadas/FornadaEntity.java` → `package com.carambolos.carambolosapi.infrastructure.persistence.fornadas;`
- `infrastructure/gateways/fornadas/FornadaRepositoryGateway.java` → `package com.carambolos.carambolosapi.infrastructure.gateways.fornadas;`
- `infrastructure/web/controllers/fornadas/FornadaController.java` → `package com.carambolos.carambolosapi.infrastructure.web.controllers.fornadas;`
- `main/FornadaConfig.java` → `package com.carambolos.carambolosapi.system;`

Observação importante: hoje suas `@Entity` vivem em `domain/entity`. Na refatoração, movemos as classes JPA para `infrastructure/persistence/jpa` e criamos as ENTIDADES DE DOMÍNIO com o mesmo nome em `domain/entity` (sem anotações JPA). Isso separa regra de tecnologia.

---

## Fornadas — passo a passo (apoiado no seu código atual)

Antes de começar, abra estas classes para acompanhar cada transformação:
- `infrastructure/web/controllers/FornadaController.java` (centraliza endpoints de fornadas e pedidos de fornada)
- `application/usecases/FornadaService.java` (hoje contém a regra de aplicação de fornadas)
- `application/usecases/FornadaDaVezService.java` (consulta itens da fornada da vez)
- `infrastructure/persistence/jpa/FornadaRepository.java` (JPA atual)

### 1) Domain: `Fornada`
Arquivo: `com/carambolos/carambolosapi/fornadas/domain/Fornada.java`

```java
package com.carambolos.carambolosapi.fornadas.domain;

import java.time.LocalDate;

public class Fornada {
  private Integer id;
  private LocalDate dataInicio;
  private LocalDate dataFim;
  private Boolean ativo;

  public Fornada(LocalDate dataInicio, LocalDate dataFim, Boolean ativo) {
    if (dataInicio == null || dataFim == null) throw new IllegalArgumentException("Datas obrigatórias");
    if (dataFim.isBefore(dataInicio)) throw new IllegalArgumentException("Data fim antes do início");
    this.dataInicio = dataInicio;
    this.dataFim = dataFim;
    this.ativo = ativo != null ? ativo : Boolean.TRUE;
  }

  public void desativar() { this.ativo = false; }
  public void ativar() { this.ativo = true; }

  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }
  public LocalDate getDataInicio() { return dataInicio; }
  public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
  public LocalDate getDataFim() { return dataFim; }
  public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
  public Boolean getAtivo() { return ativo; }
  public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
```

Por quê (olhando para `domain/entity/Fornada.java` atual): hoje essa classe tem JPA e validações. Ao criar esta versão limpa em `domain/entity`, movemos as anotações JPA para a camada `infrastructure/persistence/jpa` e mantemos as regras (datas válidas, estado ativo) no domínio. Isso permite testar sem Spring e evita acoplamento em `FornadaService`.

### 2) Gateway: `FornadaGateway`
Arquivo: `application/gateways/fornadas/FornadaGateway.java`

```java
package com.carambolos.carambolosapi.fornadas.application.port.out;

import com.carambolos.carambolosapi.fornadas.domain.Fornada;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FornadaGateway {
  Fornada save(Fornada fornada);
  boolean existsAtivaById(Integer id);
  List<Fornada> findAll();
  List<Fornada> findAllAtivas();
  List<Fornada> findByDataInicioBetweenOrderByDataInicioAsc(LocalDate inicio, LocalDate fim);
  Optional<Fornada> findTop1ByAtivaTrueOrderByDataInicioDesc();
  List<Fornada> findAllByAtivaTrueOrderByDataInicioAsc();
  Optional<Fornada> findById(Integer id);
}
```

Por quê (olhando para `FornadaService`): hoje o service chama diretamente `FornadaRepository`. Aqui criamos uma interface que representa “o que precisamos do banco”. Os Use Cases dependem do `FornadaGateway`, não do `FornadaRepository`. A implementação concreta (JPA) ficará em `infrastructure/gateways` e chamará `infrastructure/persistence/jpa/FornadaRepository`.

### 3) Use cases (interfaces + implementações)
Arquivos: `application/usecases/fornadas/*.java`

Create (equivale a `FornadaService.criarFornada`)
```java
package com.carambolos.carambolosapi.fornadas.application.port.in;

import com.carambolos.carambolosapi.fornadas.domain.Fornada;
import java.time.LocalDate;

public interface CreateFornadaUseCase {
  Fornada execute(Integer id, LocalDate dataInicio, LocalDate dataFim);
}
```

```java
package com.carambolos.carambolosapi.fornadas.application.usecase;

import com.carambolos.carambolosapi.fornadas.application.port.in.CreateFornadaUseCase;
import com.carambolos.carambolosapi.fornadas.application.port.out.FornadaGateway;
import com.carambolos.carambolosapi.fornadas.domain.Fornada;

public class CreateFornadaInteractor implements CreateFornadaUseCase {
  private final FornadaGateway gateway;

  public CreateFornadaInteractor(FornadaGateway gateway) { this.gateway = gateway; }

  @Override
  public Fornada execute(Integer id, java.time.LocalDate inicio, java.time.LocalDate fim) {
    if (id != null && gateway.existsAtivaById(id)) {
      throw new RuntimeException("Fornada com cadastro " + id + " já existe.");
    }
    Fornada f = new Fornada(inicio, fim, true);
    if (id != null) f.setId(id);
    return gateway.save(f);
  }
}
```

Atualizar (equivale a `FornadaService.atualizarFornada`)
```java
public interface AtualizarFornadaUseCase {
  com.carambolos.carambolosapi.fornadas.domain.Fornada execute(Integer id, java.time.LocalDate inicio, java.time.LocalDate fim);
}
```

```java
public class AtualizarFornadaInteractor implements AtualizarFornadaUseCase {
  private final com.carambolos.carambolosapi.fornadas.application.port.out.FornadaGateway gateway;
  public AtualizarFornadaInteractor(com.carambolos.carambolosapi.fornadas.application.port.out.FornadaGateway gateway) { this.gateway = gateway; }
  @Override
  public com.carambolos.carambolosapi.fornadas.domain.Fornada execute(Integer id, java.time.LocalDate inicio, java.time.LocalDate fim) {
    var f = gateway.findById(id).filter(x -> Boolean.TRUE.equals(x.getAtivo()))
      .orElseThrow(() -> new RuntimeException("Fornada com cadastro " + id + " não encontrada."));
    f.setDataInicio(inicio);
    f.setDataFim(fim);
    return gateway.save(f);
  }
}
```

Encerrar (equivale a `FornadaService.excluirFornada`: marcar inativo)
```java
public interface EncerrarFornadaUseCase { void execute(Integer id); }
```

```java
public class EncerrarFornadaInteractor implements EncerrarFornadaUseCase {
  private final com.carambolos.carambolosapi.fornadas.application.port.out.FornadaGateway gateway;
  public EncerrarFornadaInteractor(com.carambolos.carambolosapi.fornadas.application.port.out.FornadaGateway gateway) { this.gateway = gateway; }
  @Override public void execute(Integer id) {
    var f = gateway.findById(id).orElseThrow(() -> new RuntimeException("Fornada com cadastro " + id + " não encontrada."));
    f.desativar();
    gateway.save(f);
  }
}
```

Listar por mês/ano (equivale a `FornadaService.listarFornadasPorMesAno`)
```java
public interface ListarFornadasPorMesAnoUseCase { java.util.List<com.carambolos.carambolosapi.fornadas.domain.Fornada> execute(int ano, int mes); }
```

```java
public class ListarFornadasPorMesAnoInteractor implements ListarFornadasPorMesAnoUseCase {
  private final com.carambolos.carambolosapi.fornadas.application.port.out.FornadaGateway gateway;
  public ListarFornadasPorMesAnoInteractor(com.carambolos.carambolosapi.fornadas.application.port.out.FornadaGateway gateway) { this.gateway = gateway; }
  @Override public java.util.List<com.carambolos.carambolosapi.fornadas.domain.Fornada> execute(int ano, int mes) {
    var ym = java.time.YearMonth.of(ano, mes);
    return gateway.findByDataInicioBetweenOrderByDataInicioAsc(ym.atDay(1), ym.atEndOfMonth());
  }
}
```

Outros use cases: `ListarFornadasAtivas` (=`listarFornada` filtrando ativo), `ListarTodasFornadas`, `BuscarFornadaPorId`, `BuscarFornadaMaisRecente`, `BuscarProximaFornada`. Copie a lógica de `FornadaService` para cada interactor correspondente.

Por quê (olhando seus métodos em `FornadaService`): cada método tem validação e regra (datas, ativo, ordenação). Centralizando isso em Use Cases, o controller apenas delega, e a persistência real fica atrás do gateway. Ganhamos testabilidade e clareza.

### 4) Infrastructure: persistência JPA + gateway impl
Arquivos:
- `infrastructure/persistence/jpa/fornadas/FornadaEntity.java` (mover a JPA Entity atual para cá)
- `infrastructure/persistence/jpa/fornadas/FornadaRepository.java`
- `infrastructure/gateways/fornadas/FornadaEntityMapper.java`
- `infrastructure/gateways/fornadas/FornadaRepositoryGateway.java` (implementa `application/gateways/fornadas/FornadaGateway`)

```java
@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "fornada")
public class FornadaEntity {
  @jakarta.persistence.Id @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Integer id;
  @jakarta.persistence.Column(name = "data_inicio") private java.time.LocalDate dataInicio;
  @jakarta.persistence.Column(name = "data_fim") private java.time.LocalDate dataFim;
  @jakarta.persistence.Column(name = "is_ativo") private Boolean isAtivo = true;
  // getters/setters
}
```

```java
public interface FornadaRepository extends org.springframework.data.jpa.repository.JpaRepository<FornadaEntity, Integer> {
  boolean existsByIdAndIsAtivoTrue(Integer id);
  java.util.List<FornadaEntity> findAllByIsAtivoTrueOrderByDataInicioAsc();
  java.util.Optional<FornadaEntity> findTop1ByIsAtivoTrueOrderByDataInicioDesc();
  java.util.List<FornadaEntity> findByDataInicioBetweenOrderByDataInicioAsc(java.time.LocalDate inicio, java.time.LocalDate fim);
}
```

```java
public class FornadaEntityMapper {
  public static FornadaEntity toEntity(com.carambolos.carambolosapi.fornadas.domain.Fornada d) {
    var e = new FornadaEntity();
    e.setId(d.getId()); e.setDataInicio(d.getDataInicio()); e.setDataFim(d.getDataFim()); e.setIsAtivo(Boolean.TRUE.equals(d.getAtivo()));
    return e;
  }
  public static com.carambolos.carambolosapi.fornadas.domain.Fornada toDomain(FornadaEntity e) {
    var d = new com.carambolos.carambolosapi.fornadas.domain.Fornada(e.getDataInicio(), e.getDataFim(), e.getIsAtivo());
    d.setId(e.getId());
    return d;
  }
}
```

```java
public class FornadaRepositoryGateway implements com.carambolos.carambolosapi.fornadas.application.port.out.FornadaGateway {
  private final FornadaRepository repo;
  public FornadaRepositoryGateway(FornadaRepository repo) { this.repo = repo; }
  public com.carambolos.carambolosapi.fornadas.domain.Fornada save(com.carambolos.carambolosapi.fornadas.domain.Fornada f) { return FornadaEntityMapper.toDomain(repo.save(FornadaEntityMapper.toEntity(f))); }
  public boolean existsAtivaById(Integer id) { return repo.existsByIdAndIsAtivoTrue(id); }
  public java.util.List<com.carambolos.carambolosapi.fornadas.domain.Fornada> findAll() { return repo.findAll().stream().map(FornadaEntityMapper::toDomain).toList(); }
  public java.util.List<com.carambolos.carambolosapi.fornadas.domain.Fornada> findAllAtivas() { return repo.findAllByIsAtivoTrueOrderByDataInicioAsc().stream().map(FornadaEntityMapper::toDomain).toList(); }
  public java.util.List<com.carambolos.carambolosapi.fornadas.domain.Fornada> findByDataInicioBetweenOrderByDataInicioAsc(java.time.LocalDate i, java.time.LocalDate f) { return repo.findByDataInicioBetweenOrderByDataInicioAsc(i, f).stream().map(FornadaEntityMapper::toDomain).toList(); }
  public java.util.Optional<com.carambolos.carambolosapi.fornadas.domain.Fornada> findTop1ByAtivaTrueOrderByDataInicioDesc() { return repo.findTop1ByIsAtivoTrueOrderByDataInicioDesc().map(FornadaEntityMapper::toDomain); }
  public java.util.List<com.carambolos.carambolosapi.fornadas.domain.Fornada> findAllByAtivaTrueOrderByDataInicioAsc() { return repo.findAllByIsAtivoTrueOrderByDataInicioAsc().stream().map(FornadaEntityMapper::toDomain).toList(); }
  public java.util.Optional<com.carambolos.carambolosapi.fornadas.domain.Fornada> findById(Integer id) { return repo.findById(id).map(FornadaEntityMapper::toDomain); }
}
```

Por quê (olhando para `domain/entity/Fornada.java` atual): ao mover as anotações JPA e o `Repository` para `infrastructure/persistence/jpa`, isolamos a tecnologia. O mapper converte `Entity JPA ↔ Entidade de domínio`.

### 5) Wiring (beans)
Arquivo: `main/FornadaConfig.java`

```java
@org.springframework.context.annotation.Configuration
public class FornadaConfig {
  @org.springframework.context.annotation.Bean
  com.carambolos.carambolosapi.fornadas.application.port.out.FornadaGateway fornadaGateway(com.carambolos.carambolosapi.fornadas.infrastructure.persistence.FornadaRepository repo) {
    return new com.carambolos.carambolosapi.fornadas.infrastructure.gateways.FornadaRepositoryGateway(repo);
  }
  @org.springframework.context.annotation.Bean
  com.carambolos.carambolosapi.fornadas.application.port.in.CreateFornadaUseCase createFornadaUseCase(com.carambolos.carambolosapi.fornadas.application.port.out.FornadaGateway g) {
    return new com.carambolos.carambolosapi.fornadas.application.usecase.CreateFornadaInteractor(g);
  }
  // Bean para cada use case listado acima…
}
```

Por quê: cria os beans dos Use Cases injetando o `FornadaGateway` (implementação JPA), sem acoplar o domínio ao Spring.

### 6) Controller (trocas método a método)
Arquivo a editar: `infrastructure/web/controllers/FornadaController.java`

- POST `/fornadas` → `CreateFornadaUseCase.execute(req.id(), req.dataInicio(), req.dataFim())`
- GET `/fornadas` → `ListarFornadasAtivasUseCase.execute()`
- GET `/fornadas/todas` → `ListarTodasFornadasUseCase.execute()`
- GET `/fornadas/meses-anos` → derive da lista de todas (map/distinct/sorted)
- GET `/fornadas/com-itens?ano&mes` → `ListarFornadasPorMesAnoUseCase.execute(ano, mes)` e manter `fornadaDaVezService.buscarProdutosPorFornadaId(...)`
- GET `/fornadas/mais-recente/produtos` → `BuscarFornadaMaisRecenteUseCase.execute()` e manter chamada a `fornadaDaVezService`
- GET `/fornadas/proxima` → `BuscarProximaFornadaUseCase.execute()`
- GET `/fornadas/{id}` → `BuscarFornadaPorIdUseCase.execute(id)`
- PUT `/fornadas/{id}` → `AtualizarFornadaUseCase.execute(id, req.dataInicio(), req.dataFim())`
- DELETE `/fornadas/{id}` → `EncerrarFornadaUseCase.execute(id)`

Por quê (olhando para `FornadaController`): hoje ele chama `FornadaService`. Após a mudança, ele chamará os Use Cases equivalentes. Os DTOs e rotas permanecem iguais; o corpo apenas delega ao Use Case.

---

## Pedido de Fornada — passo a passo (apoiado no seu código atual)

Antes de começar, abra estas classes para acompanhar cada transformação:
- `application/usecases/PedidoFornadaService.java` (regras de criação/atualização/exclusão e ajuste de estoque)
- `infrastructure/persistence/jpa/FornadaDaVezRepository.java`, `EnderecoRepository.java`, `UsuarioRepository.java` (dependências atuais do service)

### 1) Domain: `PedidoFornada`
Arquivo: `pedidosfornada/domain/PedidoFornada.java`

```java
package com.carambolos.carambolosapi.pedidosfornada.domain;

import com.carambolos.carambolosapi.domain.enums.TipoEntregaEnum;
import java.time.LocalDate;

public class PedidoFornada {
  private Integer id;
  private Integer fornadaDaVez; // id
  private Integer endereco; // id
  private Integer usuario; // id
  private Integer quantidade;
  private LocalDate dataPrevisaoEntrega;
  private TipoEntregaEnum tipoEntrega;
  private String nomeCliente;
  private String telefoneCliente;
  private String horarioRetirada;
  private String observacoes;
  private Boolean ativo;

  // getters/setters (mesmos nomes/campos da Entity atual)
}
```

Por quê (olhando para `domain/entity/PedidoFornada.java` atual): essa entity JPA usa conversores cripto. Criamos a versão de domínio sem JPA/cripto em `domain/entity/pedidosfornada`. A cripto permanece só na `Entity` JPA em `infrastructure/persistence/jpa`.

### 2) Gateways e dependências externas
Arquivos: `application/gateways/pedidosfornada/*.java`

```java
import com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez;

public interface PedidoFornadaGateway {
    com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada save(com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada p);

    java.util.Optional<com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada> findById(Integer id);

    java.util.List<com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada> findAll();
}

public interface FornadaDaVezGateway {
    java.util.Optional<com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez> findById(Integer id);

    FornadaDaVez save(FornadaDaVez f);
}

public interface EnderecoGateway {
    java.util.Optional<com.carambolos.carambolosapi.domain.entity.Endereco> findById(Integer id);
}

public interface UsuarioGateway {
    java.util.Optional<com.carambolos.carambolosapi.domain.entity.Usuario> findById(Integer id);
}
```

Por quê (olhando para `PedidoFornadaService`): hoje o service usa diretamente 4 repositórios (pedido, fornada-da-vez, endereço, usuário). Transformamos isso em 4 gateways; os Use Cases passam a depender dessas interfaces.

### 3) Use cases (regras atuais)
Arquivos: `application/usecases/pedidosfornada/*.java`

Create (equivale a `PedidoFornadaService.criarPedidoFornada`)
```java
public interface CreatePedidoFornadaUseCase {
  com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada execute(
    Integer fornadaDaVezId, Integer quantidade, com.carambolos.carambolosapi.domain.enums.TipoEntregaEnum tipoEntrega,
    Integer enderecoId, Integer usuarioId, String nomeCliente, String telefoneCliente,
    String horarioRetirada, String observacoes, java.time.LocalDate dataPrevisaoEntrega
  );
}
```

```java
import com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez;

public class CreatePedidoFornadaInteractor implements CreatePedidoFornadaUseCase {
    private final PedidoFornadaGateway pedidos;
    private final FornadaDaVezGateway fdv;
    private final EnderecoGateway enderecos;
    private final UsuarioGateway usuarios;

    public CreatePedidoFornadaInteractor(PedidoFornadaGateway p, FornadaDaVezGateway f, EnderecoGateway e, UsuarioGateway u) {
        this.pedidos = p;
        this.fdv = f;
        this.enderecos = e;
        this.usuarios = u;
    }

    @Override
    public com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada execute(Integer fdvId, Integer qtd, com.carambolos.carambolosapi.domain.enums.TipoEntregaEnum tipoEntrega, Integer enderecoId, Integer usuarioId, String nome, String tel, String horarioRetirada, String observacoes, java.time.LocalDate previsao) {
        var fornadaDaVez = fdv.findById(fdvId).filter(com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez::isAtivo).orElseThrow(() -> new RuntimeException("FornadaDaVez com ID " + fdvId + " não encontrada."));
        if (fornadaDaVez.getQuantidade() < qtd)
            throw new RuntimeException(String.format("Estoque insuficiente. Disponível: %d, Solicitado: %d", fornadaDaVez.getQuantidade(), qtd));
        if (tipoEntrega == com.carambolos.carambolosapi.domain.enums.TipoEntregaEnum.ENTREGA && enderecoId == null)
            throw new RuntimeException("Tipo de entrega 'ENTREGA' requer um endereço válido.");
        if (enderecoId != null)
            enderecos.findById(enderecoId).filter(com.carambolos.carambolosapi.domain.entity.Endereco::isAtivo).orElseThrow(() -> new RuntimeException("Endereço com ID " + enderecoId + " não encontrado."));
        if (usuarioId != null)
            usuarios.findById(usuarioId).filter(com.carambolos.carambolosapi.domain.entity.Usuario::isAtivo).orElseThrow(() -> new RuntimeException("Usuário com ID " + usuarioId + " não encontrado."));
        fornadaDaVez.setQuantidade(fornadaDaVez.getQuantidade() - qtd);
        fdv.save(fornadaDaVez);
        var p = new com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada();
        p.setFornadaDaVez(fdvId);
        p.setQuantidade(qtd);
        p.setTipoEntrega(tipoEntrega);
        p.setEndereco(enderecoId);
        p.setUsuario(usuarioId);
        p.setNomeCliente(nome);
        p.setTelefoneCliente(tel);
        p.setHorarioRetirada(horarioRetirada);
        p.setObservacoes(observacoes);
        p.setDataPrevisaoEntrega(previsao);
        p.setAtivo(true);
        return pedidos.save(p);
    }
}
```

Atualizar (equivale a `PedidoFornadaService.atualizarPedidoFornada` — cálculo de delta e ajuste no estoque da FornadaDaVez)
```java
public interface AtualizarPedidoFornadaUseCase { com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada execute(Integer id, Integer novaQuantidade, java.time.LocalDate novaPrevisao); }
```

```java
public class AtualizarPedidoFornadaInteractor implements AtualizarPedidoFornadaUseCase {
  private final PedidoFornadaGateway pedidos; private final FornadaDaVezGateway fdv;
  public AtualizarPedidoFornadaInteractor(PedidoFornadaGateway p, FornadaDaVezGateway f) { this.pedidos = p; this.fdv = f; }
  @Override public com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada execute(Integer id, Integer novaQtd, java.time.LocalDate novaPrev) {
    if (novaQtd == null || novaQtd <= 0) throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
    var pedido = pedidos.findById(id).filter(com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada::getAtivo).orElseThrow(() -> new RuntimeException("PedidoFornada com ID " + id + " não encontrado."));
    int antiga = pedido.getQuantidade() != null ? pedido.getQuantidade() : 0; int delta = novaQtd - antiga;
    if (delta != 0) {
      var fornadaDaVez = fdv.findById(pedido.getFornadaDaVez()).orElse(null);
      if (fornadaDaVez != null) {
        int atual = fornadaDaVez.getQuantidade() != null ? fornadaDaVez.getQuantidade() : 0;
        int ajustado = delta > 0 ? (atual - delta) : (atual + (-delta));
        if (ajustado < 0) throw new RuntimeException("Estoque insuficiente para aumentar a quantidade do pedido.");
        fornadaDaVez.setQuantidade(ajustado); fdv.save(fornadaDaVez);
      }
    }
    pedido.setQuantidade(novaQtd); pedido.setDataPrevisaoEntrega(novaPrev);
    return pedidos.save(pedido);
  }
}
```

Excluir (equivale a `PedidoFornadaService.excluirPedidoFornada` — repõe estoque e marca `ativo=false`)
```java
public interface ExcluirPedidoFornadaUseCase { void execute(Integer id); }
```

```java
public class ExcluirPedidoFornadaInteractor implements ExcluirPedidoFornadaUseCase {
  private final PedidoFornadaGateway pedidos; private final FornadaDaVezGateway fdv;
  public ExcluirPedidoFornadaInteractor(PedidoFornadaGateway p, FornadaDaVezGateway f) { this.pedidos = p; this.fdv = f; }
  @Override public void execute(Integer id) {
    var pedido = pedidos.findById(id).orElseThrow(() -> new RuntimeException("PedidoFornada com ID " + id + " não encontrado."));
    try {
      var fornadaDaVez = fdv.findById(pedido.getFornadaDaVez()).orElse(null);
      if (fornadaDaVez != null) {
        int atual = fornadaDaVez.getQuantidade() != null ? fornadaDaVez.getQuantidade() : 0;
        fornadaDaVez.setQuantidade(atual + (pedido.getQuantidade() != null ? pedido.getQuantidade() : 0));
        fdv.save(fornadaDaVez);
      }
    } catch (Exception ignored) {}
    pedido.setAtivo(false); pedidos.save(pedido);
  }
}
```

Listar e Buscar (equivalem a `listarPedidosFornada` e `buscarPedidoFornada` — implementações delegam ao gateway, filtrando ativo quando aplicável).

### 4) Infrastructure: persistência JPA + gateways impl
Arquivos:
- `infrastructure/persistence/jpa/pedidosfornada/PedidoFornadaEntity.java`
- `infrastructure/persistence/jpa/pedidosfornada/PedidoFornadaRepository.java`
- `infrastructure/gateways/pedidosfornada/PedidoFornadaEntityMapper.java`
- `infrastructure/gateways/pedidosfornada/PedidoFornadaRepositoryGateway.java`
- Gateways auxiliares (`FornadaDaVezGateway`, `EnderecoGateway`, `UsuarioGateway`) implementados sobre `infrastructure/persistence/jpa/*Repository` atuais.

```java

@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "pedido_fornada")
public class PedidoFornadaEntity {
    @jakarta.persistence.Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer id;
    @jakarta.persistence.Column(name = "fornada_da_vez_id")
    private Integer fornadaDaVez;
    @jakarta.persistence.Column(name = "endereco_id")
    private Integer endereco;
    @jakarta.persistence.Column(name = "usuario_id")
    private Integer usuario;
    private Integer quantidade;
    @jakarta.persistence.Column(name = "data_previsao_entrega")
    private java.time.LocalDate dataPrevisaoEntrega;
    @jakarta.persistence.Enumerated(jakarta.persistence.EnumType.STRING)
    @jakarta.persistence.Column(name = "tipo_entrega")
    private com.carambolos.carambolosapi.domain.enums.TipoEntregaEnum tipoEntrega;
    @jakarta.persistence.Column(name = "nome_cliente")
    @jakarta.persistence.Convert(converter = com.carambolos.carambolosapi.system.security.CryptoAttributeConverter.class)
    private String nomeCliente;
    @jakarta.persistence.Column(name = "telefone_cliente")
    @jakarta.persistence.Convert(converter = com.carambolos.carambolosapi.system.security.CryptoAttributeConverter.class)
    private String telefoneCliente;
    @jakarta.persistence.Column(name = "horario_retirada")
    private String horarioRetirada;
    @jakarta.persistence.Column(name = "observacoes")
    private String observacoes;
    @jakarta.persistence.Column(name = "is_ativo")
    private Boolean isAtivo = true;
    // getters/setters
}
```

```java
public interface PedidoFornadaRepository extends org.springframework.data.jpa.repository.JpaRepository<PedidoFornadaEntity, Integer> {}
```

```java
public class PedidoFornadaEntityMapper {
  public static PedidoFornadaEntity toEntity(com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada d) {
    var e = new PedidoFornadaEntity();
    e.setId(d.getId()); e.setFornadaDaVez(d.getFornadaDaVez()); e.setEndereco(d.getEndereco()); e.setUsuario(d.getUsuario());
    e.setQuantidade(d.getQuantidade()); e.setDataPrevisaoEntrega(d.getDataPrevisaoEntrega()); e.setTipoEntrega(d.getTipoEntrega());
    e.setNomeCliente(d.getNomeCliente()); e.setTelefoneCliente(d.getTelefoneCliente()); e.setHorarioRetirada(d.getHorarioRetirada()); e.setObservacoes(d.getObservacoes());
    e.setIsAtivo(Boolean.TRUE.equals(d.getAtivo()));
    return e;
  }
  public static com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada toDomain(PedidoFornadaEntity e) {
    var d = new com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada();
    d.setId(e.getId()); d.setFornadaDaVez(e.getFornadaDaVez()); d.setEndereco(e.getEndereco()); d.setUsuario(e.getUsuario());
    d.setQuantidade(e.getQuantidade()); d.setDataPrevisaoEntrega(e.getDataPrevisaoEntrega()); d.setTipoEntrega(e.getTipoEntrega());
    d.setNomeCliente(e.getNomeCliente()); d.setTelefoneCliente(e.getTelefoneCliente()); d.setHorarioRetirada(e.getHorarioRetirada()); d.setObservacoes(e.getObservacoes());
    d.setAtivo(e.getIsAtivo());
    return d;
  }
}
```

```java
public class PedidoFornadaRepositoryGateway implements com.carambolos.carambolosapi.pedidosfornada.application.port.out.PedidoFornadaGateway {
  private final PedidoFornadaRepository repo;
  public PedidoFornadaRepositoryGateway(PedidoFornadaRepository repo) { this.repo = repo; }
  public com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada save(com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada p) { return PedidoFornadaEntityMapper.toDomain(repo.save(PedidoFornadaEntityMapper.toEntity(p))); }
  public java.util.Optional<com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada> findById(Integer id) { return repo.findById(id).map(PedidoFornadaEntityMapper::toDomain); }
  public java.util.List<com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada> findAll() { return repo.findAll().stream().map(PedidoFornadaEntityMapper::toDomain).toList(); }
}
```

Gateways auxiliares: implementar lambdas/adapters simples sobre os seus repositórios `FornadaDaVezRepository`, `EnderecoRepository`, `UsuarioRepository`.

### 5) Wiring (beans)
Arquivo: `main/PedidoFornadaConfig.java`

```java
import com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez;

@org.springframework.context.annotation.Configuration
public class PedidoFornadaConfig {
    @org.springframework.context.annotation.Bean
    com.carambolos.carambolosapi.pedidosfornada.application.port.out.PedidoFornadaGateway pedidoFornadaGateway(
            com.carambolos.carambolosapi.pedidosfornada.infrastructure.persistence.PedidoFornadaRepository r) {
        return new com.carambolos.carambolosapi.pedidosfornada.infrastructure.gateways.PedidoFornadaRepositoryGateway(r);
    }

    @org.springframework.context.annotation.Bean
    com.carambolos.carambolosapi.pedidosfornada.application.port.out.FornadaDaVezGateway fornadaDaVezGateway(
            com.carambolos.carambolosapi.infrastructure.persistence.jpa.FornadaDaVezRepository r) {
        return new com.carambolos.carambolosapi.pedidosfornada.application.port.out.FornadaDaVezGateway() {
            public java.util.Optional<FornadaDaVez> findById(Integer id) {
                return r.findById(id);
            }

            public FornadaDaVez save(FornadaDaVez e) {
                return r.save(e);
            }
        };
    }

    @org.springframework.context.annotation.Bean
    com.carambolos.carambolosapi.pedidosfornada.application.port.out.EnderecoGateway enderecoGateway(
            com.carambolos.carambolosapi.infrastructure.persistence.jpa.EnderecoRepository r) {
        return id -> r.findById(id);
    }

    @org.springframework.context.annotation.Bean
    com.carambolos.carambolosapi.pedidosfornada.application.port.out.UsuarioGateway usuarioGateway(
            com.carambolos.carambolosapi.infrastructure.persistence.jpa.UsuarioRepository r) {
        return id -> r.findById(id);
    }

    // Beans dos use cases: Create, Atualizar, Excluir, Listar, Buscar…
}
```

### 6) Controller (trocas)
Arquivo a editar: `infrastructure/web/controllers/FornadaController.java`

- POST `/fornadas/pedidos` → `CreatePedidoFornadaUseCase.execute(...)` com `PedidoFornadaRequestDTO` (mesmo mapeamento de campos do serviço atual).
- GET `/fornadas/pedidos` → `ListarPedidosFornadaUseCase.execute()`.
- GET `/fornadas/pedidos/{id}` → `BuscarPedidoFornadaPorIdUseCase.execute(id)`.
- PUT `/fornadas/pedidos/{id}` → `AtualizarPedidoFornadaUseCase.execute(id, req.quantidade(), req.dataPrevisaoEntrega())`.
- DELETE `/fornadas/pedidos/{id}` → `ExcluirPedidoFornadaUseCase.execute(id)`.

Por quê: controller delega para casos de uso; rotas/DTOs permanecem idênticos.

---

## Transações, exceções e testes
- Use `@Transactional` nos interactors que alteram estado (criar/atualizar/excluir Fornada e PedidoFornada) para englobar ajustes de estoque.
- Reuse as exceções existentes (`EntidadeNaoEncontradaException`, `EntidadeImprocessavelException`) dentro dos use cases para manter o tratamento do `@ControllerAdvice`.
- Testes: unidade para domínio/use cases; integração para adapters (controller + JPA).

## Checklist (execução)
- Fornadas: Domain → Ports → Use cases → Adapters JPA → Config → Controller.
- Pedidos de Fornada: Domain → Ports (incluindo gateways auxiliares) → Use cases → Adapters JPA → Config → Controller.
- Temporário: serviços atuais podem delegar aos use cases até a troca completa.

Mapa rápido de “de → para” (para não se perder):
- `FornadaService.criarFornada` → `application/usecases/fornadas/CreateFornadaInteractor`
- `FornadaService.atualizarFornada` → `application/usecases/fornadas/AtualizarFornadaInteractor`
- `FornadaService.excluirFornada` → `application/usecases/fornadas/EncerrarFornadaInteractor`
- `FornadaService.listarFornada` → `ListarFornadasAtivasInteractor`
- `FornadaService.listarTodasFornadas` → `ListarTodasFornadasInteractor`
- `FornadaService.listarFornadasPorMesAno` → `ListarFornadasPorMesAnoInteractor`
- `FornadaService.buscarFornadaMaisRecente` → `BuscarFornadaMaisRecenteInteractor`
- `FornadaService.buscarProximaFornada` → `BuscarProximaFornadaInteractor`
- `FornadaService.buscarFornada` → `BuscarFornadaPorIdInteractor`
- `PedidoFornadaService.criarPedidoFornada` → `application/usecases/pedidosfornada/CreatePedidoFornadaInteractor`
- `PedidoFornadaService.atualizarPedidoFornada` → `AtualizarPedidoFornadaInteractor`
- `PedidoFornadaService.excluirPedidoFornada` → `ExcluirPedidoFornadaInteractor`
- `PedidoFornadaService.listarPedidosFornada` → `ListarPedidosFornadaInteractor`
- `PedidoFornadaService.buscarPedidoFornada` → `BuscarPedidoFornadaPorIdInteractor`

## Observações finais
- Sem adicionar atributos: os modelos refletem exatamente seus campos atuais.
- `CryptoAttributeConverter` permanece apenas na Entity JPA (infra), não no domínio.
- Itens agregados (ex.: listar produtos da fornada) seguem usando serviços existentes até migrarmos os respectivos módulos.

---

## Apêndice — Snippets completos para colar

Este apêndice inclui os use cases restantes, beans de configuração e exemplos de métodos de controller prontos para copiar e colar, seguindo os contratos atuais.

### Fornadas — use cases restantes (interfaces + interactors)

Listar ativas
```java
// fornadas/application/port/in/ListarFornadasAtivasUseCase.java
package com.carambolos.carambolosapi.fornadas.application.port.in;

import com.carambolos.carambolosapi.fornadas.domain.Fornada;
import java.util.List;

public interface ListarFornadasAtivasUseCase {
  List<Fornada> execute();
}
```

```java
// fornadas/application/usecase/ListarFornadasAtivasInteractor.java
package com.carambolos.carambolosapi.fornadas.application.usecase;

import com.carambolos.carambolosapi.fornadas.application.port.in.ListarFornadasAtivasUseCase;
import com.carambolos.carambolosapi.fornadas.application.port.out.FornadaGateway;
import com.carambolos.carambolosapi.fornadas.domain.Fornada;
import java.util.List;

public class ListarFornadasAtivasInteractor implements ListarFornadasAtivasUseCase {
  private final FornadaGateway gateway;
  public ListarFornadasAtivasInteractor(FornadaGateway gateway) { this.gateway = gateway; }
  @Override public List<Fornada> execute() { return gateway.findAllAtivas(); }
}
```

Listar todas
```java
// fornadas/application/port/in/ListarTodasFornadasUseCase.java
package com.carambolos.carambolosapi.fornadas.application.port.in;

import com.carambolos.carambolosapi.fornadas.domain.Fornada;
import java.util.List;

public interface ListarTodasFornadasUseCase {
  List<Fornada> execute();
}
```

```java
// fornadas/application/usecase/ListarTodasFornadasInteractor.java
package com.carambolos.carambolosapi.fornadas.application.usecase;

import com.carambolos.carambolosapi.fornadas.application.port.in.ListarTodasFornadasUseCase;
import com.carambolos.carambolosapi.fornadas.application.port.out.FornadaGateway;
import com.carambolos.carambolosapi.fornadas.domain.Fornada;
import java.util.List;

public class ListarTodasFornadasInteractor implements ListarTodasFornadasUseCase {
  private final FornadaGateway gateway;
  public ListarTodasFornadasInteractor(FornadaGateway gateway) { this.gateway = gateway; }
  @Override public List<Fornada> execute() { return gateway.findAll(); }
}
```

Buscar por id
```java
// fornadas/application/port/in/BuscarFornadaPorIdUseCase.java
package com.carambolos.carambolosapi.fornadas.application.port.in;

import com.carambolos.carambolosapi.fornadas.domain.Fornada;

public interface BuscarFornadaPorIdUseCase {
  Fornada execute(Integer id);
}
```

```java
// fornadas/application/usecase/BuscarFornadaPorIdInteractor.java
package com.carambolos.carambolosapi.fornadas.application.usecase;

import com.carambolos.carambolosapi.fornadas.application.port.in.BuscarFornadaPorIdUseCase;
import com.carambolos.carambolosapi.fornadas.application.port.out.FornadaGateway;

public class BuscarFornadaPorIdInteractor implements BuscarFornadaPorIdUseCase {
  private final FornadaGateway gateway;
  public BuscarFornadaPorIdInteractor(FornadaGateway gateway) { this.gateway = gateway; }
  @Override public com.carambolos.carambolosapi.fornadas.domain.Fornada execute(Integer id) {
    return gateway.findById(id)
      .filter(f -> Boolean.TRUE.equals(f.getAtivo()))
      .orElseThrow(() -> new RuntimeException("Fornada com cadastro " + id + " não encontrada."));
  }
}
```

Buscar mais recente
```java
// fornadas/application/port/in/BuscarFornadaMaisRecenteUseCase.java
package com.carambolos.carambolosapi.fornadas.application.port.in;

import com.carambolos.carambolosapi.fornadas.domain.Fornada;
import java.util.Optional;

public interface BuscarFornadaMaisRecenteUseCase {
  Optional<Fornada> execute();
}
```

```java
// fornadas/application/usecase/BuscarFornadaMaisRecenteInteractor.java
package com.carambolos.carambolosapi.fornadas.application.usecase;

import com.carambolos.carambolosapi.fornadas.application.port.in.BuscarFornadaMaisRecenteUseCase;
import com.carambolos.carambolosapi.fornadas.application.port.out.FornadaGateway;
import com.carambolos.carambolosapi.fornadas.domain.Fornada;
import java.util.Optional;

public class BuscarFornadaMaisRecenteInteractor implements BuscarFornadaMaisRecenteUseCase {
  private final FornadaGateway gateway;
  public BuscarFornadaMaisRecenteInteractor(FornadaGateway gateway) { this.gateway = gateway; }
  @Override public Optional<Fornada> execute() { return gateway.findTop1ByAtivaTrueOrderByDataInicioDesc(); }
}
```

Buscar próxima
```java
// fornadas/application/port/in/BuscarProximaFornadaUseCase.java
package com.carambolos.carambolosapi.fornadas.application.port.in;

import com.carambolos.carambolosapi.fornadas.domain.Fornada;
import java.util.Optional;

public interface BuscarProximaFornadaUseCase {
  Optional<Fornada> execute();
}
```

```java
// fornadas/application/usecase/BuscarProximaFornadaInteractor.java
package com.carambolos.carambolosapi.fornadas.application.usecase;

import com.carambolos.carambolosapi.fornadas.application.port.in.BuscarProximaFornadaUseCase;
import com.carambolos.carambolosapi.fornadas.application.port.out.FornadaGateway;
import com.carambolos.carambolosapi.fornadas.domain.Fornada;
import java.time.LocalDate;
import java.util.Optional;

public class BuscarProximaFornadaInteractor implements BuscarProximaFornadaUseCase {
  private final FornadaGateway gateway;
  public BuscarProximaFornadaInteractor(FornadaGateway gateway) { this.gateway = gateway; }
  @Override public Optional<Fornada> execute() {
    LocalDate hoje = LocalDate.now();
    return gateway.findAllByAtivaTrueOrderByDataInicioAsc().stream()
      .filter(f -> f.getDataInicio() != null && f.getDataInicio().isAfter(hoje))
      .filter(f -> f.getDataFim() == null || !f.getDataFim().isBefore(hoje))
      .findFirst();
  }
}
```

### Fornadas — beans adicionais (FornadaConfig)

```java
// fornadas/main/FornadaConfig.java (completar com)
@org.springframework.context.annotation.Bean
com.carambolos.carambolosapi.fornadas.application.port.in.ListarFornadasAtivasUseCase listarFornadasAtivasUseCase(
    com.carambolos.carambolosapi.fornadas.application.port.out.FornadaGateway g) {
  return new com.carambolos.carambolosapi.fornadas.application.usecase.ListarFornadasAtivasInteractor(g);
}

@org.springframework.context.annotation.Bean
com.carambolos.carambolosapi.fornadas.application.port.in.ListarTodasFornadasUseCase listarTodasFornadasUseCase(
    com.carambolos.carambolosapi.fornadas.application.port.out.FornadaGateway g) {
  return new com.carambolos.carambolosapi.fornadas.application.usecase.ListarTodasFornadasInteractor(g);
}

@org.springframework.context.annotation.Bean
com.carambolos.carambolosapi.fornadas.application.port.in.BuscarFornadaPorIdUseCase buscarFornadaPorIdUseCase(
    com.carambolos.carambolosapi.fornadas.application.port.out.FornadaGateway g) {
  return new com.carambolos.carambolosapi.fornadas.application.usecase.BuscarFornadaPorIdInteractor(g);
}

@org.springframework.context.annotation.Bean
com.carambolos.carambolosapi.fornadas.application.port.in.BuscarFornadaMaisRecenteUseCase buscarFornadaMaisRecenteUseCase(
    com.carambolos.carambolosapi.fornadas.application.port.out.FornadaGateway g) {
  return new com.carambolos.carambolosapi.fornadas.application.usecase.BuscarFornadaMaisRecenteInteractor(g);
}

@org.springframework.context.annotation.Bean
com.carambolos.carambolosapi.fornadas.application.port.in.BuscarProximaFornadaUseCase buscarProximaFornadaUseCase(
    com.carambolos.carambolosapi.fornadas.application.port.out.FornadaGateway g) {
  return new com.carambolos.carambolosapi.fornadas.application.usecase.BuscarProximaFornadaInteractor(g);
}
```

### Fornadas — métodos de controller (exemplos prontos)

```java
// Dentro do FornadaController existente, troque o corpo para usar os use cases injetados

@org.springframework.web.bind.annotation.PostMapping
public org.springframework.http.ResponseEntity<com.carambolos.carambolosapi.fornadas.domain.Fornada> criarFornada(
    @org.springframework.web.bind.annotation.RequestBody @jakarta.validation.Valid com.carambolos.carambolosapi.infrastructure.web.request.FornadaRequestDTO request) {
  var f = createFornadaUseCase.execute(request.id(), request.dataInicio(), request.dataFim());
  return org.springframework.http.ResponseEntity.status(201).body(f);
}

@org.springframework.web.bind.annotation.GetMapping
public org.springframework.http.ResponseEntity<java.util.List<com.carambolos.carambolosapi.fornadas.domain.Fornada>> listarFornadas() {
  return org.springframework.http.ResponseEntity.ok(listarFornadasAtivasUseCase.execute());
}

@org.springframework.web.bind.annotation.GetMapping("/todas")
public org.springframework.http.ResponseEntity<java.util.List<com.carambolos.carambolosapi.fornadas.domain.Fornada>> listarTodasFornadas() {
  return org.springframework.http.ResponseEntity.ok(listarTodasFornadasUseCase.execute());
}

@org.springframework.web.bind.annotation.GetMapping("/{id}")
public org.springframework.http.ResponseEntity<com.carambolos.carambolosapi.fornadas.domain.Fornada> buscarFornada(@org.springframework.web.bind.annotation.PathVariable Integer id) {
  return org.springframework.http.ResponseEntity.ok(buscarFornadaPorIdUseCase.execute(id));
}

@org.springframework.web.bind.annotation.PutMapping("/{id}")
public org.springframework.http.ResponseEntity<com.carambolos.carambolosapi.fornadas.domain.Fornada> atualizarFornada(
    @org.springframework.web.bind.annotation.PathVariable Integer id,
    @org.springframework.web.bind.annotation.RequestBody @jakarta.validation.Valid com.carambolos.carambolosapi.infrastructure.web.request.FornadaRequestDTO request) {
  var f = atualizarFornadaUseCase.execute(id, request.dataInicio(), request.dataFim());
  return org.springframework.http.ResponseEntity.ok(f);
}

@org.springframework.web.bind.annotation.DeleteMapping("/{id}")
public org.springframework.http.ResponseEntity<Void> excluirFornada(@org.springframework.web.bind.annotation.PathVariable Integer id) {
  encerrarFornadaUseCase.execute(id);
  return org.springframework.http.ResponseEntity.noContent().build();
}

// Endpoints que combinam com itens/mais-recente/proxima mantêm o uso de fornadaDaVezService, mas obtêm a Fornada via use cases
```

### PedidoFornada — use cases restantes (interfaces + interactors)

Listar
```java
// pedidosfornada/application/port/in/ListarPedidosFornadaUseCase.java
package com.carambolos.carambolosapi.pedidosfornada.application.port.in;

import com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada;
import java.util.List;

public interface ListarPedidosFornadaUseCase {
  List<PedidoFornada> execute();
}
```

```java
// pedidosfornada/application/usecase/ListarPedidosFornadaInteractor.java
package com.carambolos.carambolosapi.pedidosfornada.application.usecase;

import com.carambolos.carambolosapi.pedidosfornada.application.port.in.ListarPedidosFornadaUseCase;
import com.carambolos.carambolosapi.pedidosfornada.application.port.out.PedidoFornadaGateway;
import com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada;
import java.util.List;

public class ListarPedidosFornadaInteractor implements ListarPedidosFornadaUseCase {
  private final PedidoFornadaGateway gateway;
  public ListarPedidosFornadaInteractor(PedidoFornadaGateway gateway) { this.gateway = gateway; }
  @Override public List<PedidoFornada> execute() { return gateway.findAll().stream().filter(PedidoFornada::getAtivo).toList(); }
}
```

Buscar por id
```java
// pedidosfornada/application/port/in/BuscarPedidoFornadaPorIdUseCase.java
package com.carambolos.carambolosapi.pedidosfornada.application.port.in;

import com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada;

public interface BuscarPedidoFornadaPorIdUseCase {
  PedidoFornada execute(Integer id);
}
```

```java
// pedidosfornada/application/usecase/BuscarPedidoFornadaPorIdInteractor.java
package com.carambolos.carambolosapi.pedidosfornada.application.usecase;

import com.carambolos.carambolosapi.pedidosfornada.application.port.in.BuscarPedidoFornadaPorIdUseCase;
import com.carambolos.carambolosapi.pedidosfornada.application.port.out.PedidoFornadaGateway;

public class BuscarPedidoFornadaPorIdInteractor implements BuscarPedidoFornadaPorIdUseCase {
  private final PedidoFornadaGateway gateway;
  public BuscarPedidoFornadaPorIdInteractor(PedidoFornadaGateway gateway) { this.gateway = gateway; }
  @Override public com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada execute(Integer id) {
    return gateway.findById(id)
      .filter(com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada::getAtivo)
      .orElseThrow(() -> new RuntimeException("PedidoFornada com ID " + id + " não encontrado."));
  }
}
```

### PedidoFornada — beans adicionais (PedidoFornadaConfig)

```java
// pedidosfornada/main/PedidoFornadaConfig.java (completar com)
@org.springframework.context.annotation.Bean
com.carambolos.carambolosapi.pedidosfornada.application.port.in.ListarPedidosFornadaUseCase listarPedidosFornadaUseCase(
    com.carambolos.carambolosapi.pedidosfornada.application.port.out.PedidoFornadaGateway g) {
  return new com.carambolos.carambolosapi.pedidosfornada.application.usecase.ListarPedidosFornadaInteractor(g);
}

@org.springframework.context.annotation.Bean
com.carambolos.carambolosapi.pedidosfornada.application.port.in.BuscarPedidoFornadaPorIdUseCase buscarPedidoFornadaPorIdUseCase(
    com.carambolos.carambolosapi.pedidosfornada.application.port.out.PedidoFornadaGateway g) {
  return new com.carambolos.carambolosapi.pedidosfornada.application.usecase.BuscarPedidoFornadaPorIdInteractor(g);
}
```

### PedidoFornada — métodos de controller (exemplos prontos)

```java
// Dentro do FornadaController (se mantendo as rotas), substitua os métodos:

@org.springframework.web.bind.annotation.PostMapping("/pedidos")
public org.springframework.http.ResponseEntity<com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada> criarPedidoFornada(
    @org.springframework.web.bind.annotation.RequestBody @jakarta.validation.Valid com.carambolos.carambolosapi.infrastructure.web.request.PedidoFornadaRequestDTO request) {
  var p = createPedidoFornadaUseCase.execute(
    request.fornadaDaVezId(), request.quantidade(), request.tipoEntrega(),
    request.enderecoId(), request.usuarioId(), request.nomeCliente(), request.telefoneCliente(),
    request.horario(), request.observacoes(), request.dataPrevisaoEntrega()
  );
  return org.springframework.http.ResponseEntity.status(201).body(p);
}

@org.springframework.web.bind.annotation.GetMapping("/pedidos")
public org.springframework.http.ResponseEntity<java.util.List<com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada>> listarPedidos() {
  return org.springframework.http.ResponseEntity.ok(listarPedidosFornadaUseCase.execute());
}

@org.springframework.web.bind.annotation.GetMapping("/pedidos/{id}")
public org.springframework.http.ResponseEntity<com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada> buscarPedido(@org.springframework.web.bind.annotation.PathVariable Integer id) {
  return org.springframework.http.ResponseEntity.ok(buscarPedidoFornadaPorIdUseCase.execute(id));
}

@org.springframework.web.bind.annotation.PutMapping("/pedidos/{id}")
public org.springframework.http.ResponseEntity<com.carambolos.carambolosapi.pedidosfornada.domain.PedidoFornada> atualizarPedidoFornada(
    @org.springframework.web.bind.annotation.PathVariable Integer id,
    @org.springframework.web.bind.annotation.RequestBody @jakarta.validation.Valid com.carambolos.carambolosapi.infrastructure.web.request.PedidoFornadaUpdateRequestDTO request) {
  var p = atualizarPedidoFornadaUseCase.execute(id, request.quantidade(), request.dataPrevisaoEntrega());
  return org.springframework.http.ResponseEntity.ok(p);
}

@org.springframework.web.bind.annotation.DeleteMapping("/pedidos/{id}")
public org.springframework.http.ResponseEntity<Void> excluirPedidoFornada(@org.springframework.web.bind.annotation.PathVariable Integer id) {
  excluirPedidoFornadaUseCase.execute(id);
  return org.springframework.http.ResponseEntity.noContent().build();
}
```

---

Se preferir, substitua `RuntimeException` pelas suas exceções existentes (`EntidadeNaoEncontradaException`, `EntidadeImprocessavelException`) dentro dos interactors para manter mensagens e status do `@ControllerAdvice`.


