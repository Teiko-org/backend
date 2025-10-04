## Refatoração Clean Architecture — Fornadas e Pedido de Fornada (estruturas reais do projeto)

Padrão (sem ports-in/out):
- domain/entity: modelos de domínio limpos (sem JPA)
- application/usecases: UMA classe por contexto (FornadasUseCases, PedidoFornadaUseCases)
- application/gateways: interfaces para banco/externos
- infrastructure/persistence/jpa: Entities JPA e Repositories (Spring Data)
- infrastructure/gateways/impl: implementações dos gateways delegando a repositórios JPA
- infrastructure/gateways/mapperEntity: mapeadores Entity↔Domain (quando necessário)
- infrastructure/web: controllers e DTOs (rotas/contratos mantidos)
- main: configuração de beans

Fontes das regras: `application/usecases/FornadaService.java`, `application/usecases/PedidoFornadaService.java` e `infrastructure/web/controllers/FornadaController.java`.

---

### 1) domain/entity — modelos (sem JPA)

Crie as versões limpas, espelhando os campos atuais (sem anotações do Spring/JPA):

`src/main/java/com/carambolos/carambolosapi/domain/entity/fornadas/Fornada.java`
```java
package com.carambolos.carambolosapi.domain.entity.fornadas;

import java.time.LocalDate;

public class Fornada {
  private Integer id;
  private LocalDate dataInicio;
  private LocalDate dataFim;
  private Boolean ativo;

  public Fornada(LocalDate dataInicio, LocalDate dataFim, Boolean ativo) {
    if (dataInicio == null || dataFim == null) throw new IllegalArgumentException("Datas obrigatórias");
    if (dataFim.isBefore(dataInicio)) throw new IllegalArgumentException("Data fim antes do início");
    this.dataInicio = dataInicio; this.dataFim = dataFim; this.ativo = ativo != null ? ativo : Boolean.TRUE;
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

`src/main/java/com/carambolos/carambolosapi/domain/entity/pedidosfornada/PedidoFornada.java`
```java
package com.carambolos.carambolosapi.domain.entity.pedidosfornada;

import com.carambolos.carambolosapi.domain.enums.TipoEntregaEnum;
import java.time.LocalDate;

public class PedidoFornada {
  private Integer id;
  private Integer fornadaDaVez;
  private Integer endereco;
  private Integer usuario;
  private Integer quantidade;
  private LocalDate dataPrevisaoEntrega;
  private TipoEntregaEnum tipoEntrega;
  private String nomeCliente;
  private String telefoneCliente;
  private String horarioRetirada;
  private String observacoes;
  private Boolean ativo;
  // getters/setters
}
```

`src/main/java/com/carambolos/carambolosapi/domain/entity/fornadas/FornadaDaVez.java` (opcional: se quiser domínio separado; hoje usamos a Entity JPA movida para infra)
```java
package com.carambolos.carambolosapi.domain.entity.fornadas;

public class FornadaDaVez {
  private Integer id; private Integer produtoFornada; private Integer fornada; private Integer quantidade; private Boolean ativo;
  // getters/setters
}
```

---

### 2) application/gateways — contratos mínimos

`application/gateways/fornadas/FornadaGateway.java`
```java
package com.carambolos.carambolosapi.application.gateways.fornadas;

import com.carambolos.carambolosapi.domain.entity.fornadas.Fornada;
import java.time.LocalDate; import java.util.*;

public interface FornadaGateway {
  Fornada save(Fornada fornada);
  boolean existsAtivaById(Integer id);
  List<Fornada> findAll(); List<Fornada> findAllAtivas();
  List<Fornada> findByDataInicioBetweenOrderByDataInicioAsc(LocalDate inicio, LocalDate fim);
  Optional<Fornada> findTop1ByAtivaTrueOrderByDataInicioDesc();
  List<Fornada> findAllByAtivaTrueOrderByDataInicioAsc();
  Optional<Fornada> findById(Integer id);
}
```

`application/gateways/pedidosfornada/PedidoFornadaGateway.java`
```java
package com.carambolos.carambolosapi.application.gateways.pedidosfornada;

import com.carambolos.carambolosapi.domain.entity.pedidosfornada.PedidoFornada;
import java.util.*;

public interface PedidoFornadaGateway {
  PedidoFornada save(PedidoFornada p);
  Optional<PedidoFornada> findById(Integer id);
  List<PedidoFornada> findAll();
}
```

`application/gateways/fornadas/FornadaDaVezGateway.java` (já criado)
```java
package com.carambolos.carambolosapi.application.gateways.fornadas;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez;
import java.util.Optional;

public interface FornadaDaVezGateway { Optional<FornadaDaVez> findById(Integer id); FornadaDaVez save(FornadaDaVez e); }
```

Gateways temporários para teste sem depender do time de Usuário/Endereço (não precisa commitar depois):

`application/gateways/pedidosfornada/EnderecoGateway.java`
```java
package com.carambolos.carambolosapi.application.gateways.pedidosfornada;
public interface EnderecoGateway { java.util.Optional<com.carambolos.carambolosapi.domain.entity.Endereco> findById(Integer id); }
```

`application/gateways/pedidosfornada/UsuarioGateway.java`
```java
package com.carambolos.carambolosapi.application.gateways.pedidosfornada;
public interface UsuarioGateway { java.util.Optional<com.carambolos.carambolosapi.domain.entity.Usuario> findById(Integer id); }
```

---

### 3) application/usecases — UMA classe por contexto

`application/usecases/fornadas/FornadasUseCases.java`
```java
package com.carambolos.carambolosapi.application.usecases.fornadas;

import com.carambolos.carambolosapi.application.gateways.fornadas.FornadaGateway;
import com.carambolos.carambolosapi.domain.entity.fornadas.Fornada;
import java.time.*; import java.util.*;

public class FornadasUseCases {
  private final FornadaGateway gateway;
  public FornadasUseCases(FornadaGateway gateway) { this.gateway = gateway; }

  public Fornada criar(Integer id, LocalDate inicio, LocalDate fim) {
    if (id != null && gateway.existsAtivaById(id)) throw new RuntimeException("Fornada com cadastro "+id+" já existe.");
    var f = new Fornada(inicio, fim, true); if (id != null) f.setId(id); return gateway.save(f);
  }
  public Fornada atualizar(Integer id, LocalDate inicio, LocalDate fim) { var f = buscarPorId(id); f.setDataInicio(inicio); f.setDataFim(fim); return gateway.save(f); }
  public void encerrar(Integer id) { var f = gateway.findById(id).orElseThrow(() -> new RuntimeException("Fornada com cadastro "+id+" não encontrada.")); f.desativar(); gateway.save(f); }
  public java.util.List<Fornada> listarAtivas() { return gateway.findAllAtivas(); }
  public java.util.List<Fornada> listarTodas() { return gateway.findAll(); }
  public java.util.List<Fornada> listarPorMesAno(int ano, int mes) { var ym = java.time.YearMonth.of(ano, mes); return gateway.findByDataInicioBetweenOrderByDataInicioAsc(ym.atDay(1), ym.atEndOfMonth()); }
  public java.util.Optional<Fornada> buscarMaisRecente() { return gateway.findTop1ByAtivaTrueOrderByDataInicioDesc(); }
  public java.util.Optional<Fornada> buscarProxima() { var hoje = java.time.LocalDate.now(); return gateway.findAllByAtivaTrueOrderByDataInicioAsc().stream().filter(f -> f.getDataInicio()!=null && f.getDataInicio().isAfter(hoje)).filter(f -> f.getDataFim()==null || !f.getDataFim().isBefore(hoje)).findFirst(); }
  public Fornada buscarPorId(Integer id) { return gateway.findById(id).filter(f -> Boolean.TRUE.equals(f.getAtivo())).orElseThrow(() -> new RuntimeException("Fornada com cadastro "+id+" não encontrada.")); }
}
```

`application/usecases/pedidosfornada/PedidoFornadaUseCases.java`

```java
package com.carambolos.carambolosapi.application.usecases.pedidosfornada;

import com.carambolos.carambolosapi.application.gateways.FornadaDaVezGateway;
import com.carambolos.carambolosapi.application.gateways.pedidosfornada.*;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez;
import com.carambolos.carambolosapi.domain.entity.pedidosfornada.PedidoFornada;
import com.carambolos.carambolosapi.domain.enums.TipoEntregaEnum;

public class PedidoFornadaUseCases {
    private final PedidoFornadaGateway pedidos;
    private final FornadaDaVezGateway fdv;
    private final EnderecoGateway enderecos;
    private final UsuarioGateway usuarios;

    public PedidoFornadaUseCases(PedidoFornadaGateway p, FornadaDaVezGateway f, EnderecoGateway e, UsuarioGateway u) {
        this.pedidos = p;
        this.fdv = f;
        this.enderecos = e;
        this.usuarios = u;
    }

    public PedidoFornada criar(Integer fdvId, Integer qtd, TipoEntregaEnum tipoEntrega, Integer enderecoId, Integer usuarioId, String nome, String tel, String horarioRetirada, String observacoes, java.time.LocalDate previsao) {
        FornadaDaVez fz = fdv.findById(fdvId).filter(FornadaDaVez::isAtivo).orElseThrow(() -> new RuntimeException("FornadaDaVez com ID " + fdvId + " não encontrada."));
        if (fz.getQuantidade() < qtd)
            throw new RuntimeException("Estoque insuficiente. Disponível: " + fz.getQuantidade() + ", Solicitado: " + qtd);
        if (tipoEntrega == TipoEntregaEnum.ENTREGA && enderecoId == null)
            throw new RuntimeException("Tipo de entrega 'ENTREGA' requer um endereço válido.");
        if (enderecoId != null)
            enderecos.findById(enderecoId).filter(com.carambolos.carambolosapi.domain.entity.Endereco::isAtivo).orElseThrow(() -> new RuntimeException("Endereço com ID " + enderecoId + " não encontrado."));
        if (usuarioId != null)
            usuarios.findById(usuarioId).filter(com.carambolos.carambolosapi.domain.entity.Usuario::isAtivo).orElseThrow(() -> new RuntimeException("Usuário com ID " + usuarioId + " não encontrado."));
        fz.setQuantidade(fz.getQuantidade() - qtd);
        fdv.save(fz);
        var p = new PedidoFornada();
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

    public PedidoFornada atualizar(Integer id, Integer novaQtd, java.time.LocalDate novaPrev) {
        if (novaQtd == null || novaQtd <= 0)
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        var pedido = buscarPorId(id);
        int antiga = pedido.getQuantidade() != null ? pedido.getQuantidade() : 0;
        int delta = novaQtd - antiga;
        if (delta != 0) {
            FornadaDaVez fz = fdv.findById(pedido.getFornadaDaVez()).orElse(null);
            if (fz != null) {
                int atual = fz.getQuantidade() != null ? fz.getQuantidade() : 0;
                int ajustado = delta > 0 ? (atual - delta) : (atual + (-delta));
                if (ajustado < 0)
                    throw new RuntimeException("Estoque insuficiente para aumentar a quantidade do pedido.");
                fz.setQuantidade(ajustado);
                fdv.save(fz);
            }
        }
        pedido.setQuantidade(novaQtd);
        pedido.setDataPrevisaoEntrega(novaPrev);
        return pedidos.save(pedido);
    }

    public void excluir(Integer id) {
        var pedido = buscarPorId(id);
        try {
            FornadaDaVez fz = fdv.findById(pedido.getFornadaDaVez()).orElse(null);
            if (fz != null) {
                int atual = fz.getQuantidade() != null ? fz.getQuantidade() : 0;
                fz.setQuantidade(atual + (pedido.getQuantidade() != null ? pedido.getQuantidade() : 0));
                fdv.save(fz);
            }
        } catch (Exception ignored) {
        }
        pedido.setAtivo(false);
        pedidos.save(pedido);
    }

    public java.util.List<PedidoFornada> listar() {
        return pedidos.findAll().stream().filter(PedidoFornada::getAtivo).toList();
    }

    public PedidoFornada buscarPorId(Integer id) {
        return pedidos.findById(id).filter(PedidoFornada::getAtivo).orElseThrow(() -> new RuntimeException("PedidoFornada com ID " + id + " não encontrado."));
    }
}
```

---

### 4) infrastructure/persistence/jpa — Entities/Repos

- Mover `@Entity` atuais para `infrastructure/persistence/jpa/**` (você já moveu `FornadaDaVez` para `infrastructure/persistence/entity/FornadaDaVez.java`: mantenha consistente com os demais ou ajuste os imports do adapter)
- Repositórios permanecem em `infrastructure/persistence/jpa/**` (ex.: `FornadaDaVezRepository`)

---

### 5) infrastructure/gateways — adapters e mappers

- Adapter criado: `infrastructure/gateways/impl/FornadaDaVezRepositoryGateway.java` (usa `FornadaDaVezRepository`)
- Mapper criado: `infrastructure/gateways/mapperEntity/FornadaDaVezEntityMapper.java` (por enquanto identity; ajuste quando separar domínio vs JPA)
- Faltantes análogos: `FornadaRepositoryGateway`, `PedidoFornadaRepositoryGateway` e seus mappers (se necessário) seguindo o mesmo padrão.

---

### 6) main — configuração de beans

`main/PedidoFornadaConfig.java`: já expõe `FornadaDaVezGateway`. Adicione beans para `PedidoFornadaGateway` e (quando criar) `FornadasUseCases`/`PedidoFornadaUseCases` conforme você for implementando os adapters faltantes.

---

### 7) infrastructure/web — controllers

- Não mude rotas/DTOs. Substitua as chamadas a serviços pelos métodos das classes `FornadasUseCases` e `PedidoFornadaUseCases`.
- Ex.: `criarPedidoFornada` chama `pedidoFornadaUseCases.criar(...)` com os campos do DTO.

---

### 8) Gateways temporários (Endereço/Usuário) — somente para testes locais

Implemente adapters mínimos que só delegam aos repositórios, e remova antes de commitar se outro time cuidar disso:

`infrastructure/gateways/impl/EnderecoRepositoryGateway.java`
```java
package com.carambolos.carambolosapi.infrastructure.gateways.impl;
@org.springframework.stereotype.Component
public class EnderecoRepositoryGateway implements com.carambolos.carambolosapi.application.gateways.pedidosfornada.EnderecoGateway {
  private final com.carambolos.carambolosapi.infrastructure.persistence.jpa.EnderecoRepository repo;
  public EnderecoRepositoryGateway(com.carambolos.carambolosapi.infrastructure.persistence.jpa.EnderecoRepository repo) { this.repo = repo; }
  public java.util.Optional<com.carambolos.carambolosapi.domain.entity.Endereco> findById(Integer id) { return repo.findById(id); }
}
```

`infrastructure/gateways/impl/UsuarioRepositoryGateway.java`
```java
package com.carambolos.carambolosapi.infrastructure.gateways.impl;
@org.springframework.stereotype.Component
public class UsuarioRepositoryGateway implements com.carambolos.carambolosapi.application.gateways.pedidosfornada.UsuarioGateway {
  private final com.carambolos.carambolosapi.infrastructure.persistence.jpa.UsuarioRepository repo;
  public UsuarioRepositoryGateway(com.carambolos.carambolosapi.infrastructure.persistence.jpa.UsuarioRepository repo) { this.repo = repo; }
  public java.util.Optional<com.carambolos.carambolosapi.domain.entity.Usuario> findById(Integer id) { return repo.findById(id); }
}
```

---

### 9) Transações, exceções e testes

- Adicione `@Transactional` nos métodos mutadores das classes de use cases.
- Reuse `EntidadeNaoEncontradaException` e `EntidadeImprocessavelException` nos use cases para preservar o comportamento do `@ControllerAdvice`.
- Testes unitários: mock dos gateways e teste das regras de `FornadasUseCases` e `PedidoFornadaUseCases`.

---

### 10) Mapa rápido de equivalências (de → para)

- `FornadaService.criarFornada` → `FornadasUseCases.criar`
- `FornadaService.atualizarFornada` → `FornadasUseCases.atualizar`
- `FornadaService.excluirFornada` → `FornadasUseCases.encerrar`
- `FornadaService.listarFornada` → `FornadasUseCases.listarAtivas`
- `FornadaService.listarTodasFornadas` → `FornadasUseCases.listarTodas`
- `FornadaService.listarFornadasPorMesAno` → `FornadasUseCases.listarPorMesAno`
- `FornadaService.buscarFornadaMaisRecente` → `FornadasUseCases.buscarMaisRecente`
- `FornadaService.buscarProximaFornada` → `FornadasUseCases.buscarProxima`
- `FornadaService.buscarFornada` → `FornadasUseCases.buscarPorId`
- `PedidoFornadaService.criarPedidoFornada` → `PedidoFornadaUseCases.criar`
- `PedidoFornadaService.atualizarPedidoFornada` → `PedidoFornadaUseCases.atualizar`
- `PedidoFornadaService.excluirPedidoFornada` → `PedidoFornadaUseCases.excluir`
- `PedidoFornadaService.listarPedidosFornada` → `PedidoFornadaUseCases.listar`
- `PedidoFornadaService.buscarPedidoFornada` → `PedidoFornadaUseCases.buscarPorId`


