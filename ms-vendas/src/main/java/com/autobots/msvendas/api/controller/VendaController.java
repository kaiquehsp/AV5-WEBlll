package com.autobots.msvendas.api.controller;

import com.autobots.msvendas.domain.entity.Venda;
import com.autobots.msvendas.domain.repository.VendaRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vendas")
public class VendaController {

    private final VendaRepository repository;

    public VendaController(VendaRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/empresa/{idEmpresa}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENDEDOR')")
    public ResponseEntity<Venda> registrarVenda(@PathVariable Long idEmpresa, @RequestBody Venda venda) {
        venda.setIdEmpresa(idEmpresa);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(venda));
    }
    @GetMapping("/empresa/{idEmpresa}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public ResponseEntity<List<Venda>> listarVendasPorPeriodo(
            @PathVariable Long idEmpresa,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        
        List<Venda> vendas = repository.findByIdEmpresaAndDataVendaBetween(idEmpresa, inicio, fim);
        return vendas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(vendas);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public ResponseEntity<Venda> atualizarVenda(@PathVariable Long id, @RequestBody Venda dadosAtualizados) {
        return repository.findById(id).map(vendaExistente -> {
            vendaExistente.setIdCliente(dadosAtualizados.getIdCliente());
            vendaExistente.setIdFuncionario(dadosAtualizados.getIdFuncionario());
            vendaExistente.setIdsMercadorias(dadosAtualizados.getIdsMercadorias());
            vendaExistente.setIdsServicos(dadosAtualizados.getIdsServicos());
            return ResponseEntity.ok(repository.save(vendaExistente));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')") 
    public ResponseEntity<Void> cancelarVenda(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}