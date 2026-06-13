package com.autobots.msprodutos.api.controller;

import com.autobots.msprodutos.domain.entity.Mercadoria;
import com.autobots.msprodutos.domain.repository.MercadoriaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mercadorias")
public class MercadoriaController {

    private final MercadoriaRepository repository;

    public MercadoriaController(MercadoriaRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/empresa/{idEmpresa}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')") 
    public ResponseEntity<Mercadoria> cadastrar(@PathVariable Long idEmpresa, @RequestBody Mercadoria mercadoria) {
        mercadoria.setIdEmpresa(idEmpresa);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(mercadoria));
    }

    @GetMapping("/empresa/{idEmpresa}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    public ResponseEntity<List<Mercadoria>> listarPorEmpresa(@PathVariable Long idEmpresa) {
        List<Mercadoria> produtos = repository.findByIdEmpresa(idEmpresa);
        return produtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(produtos);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public ResponseEntity<Mercadoria> atualizar(@PathVariable Long id, @RequestBody Mercadoria dados) {
        return repository.findById(id).map(m -> {
            m.setNome(dados.getNome());
            m.setValor(dados.getValor());
            return ResponseEntity.ok(repository.save(m));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}