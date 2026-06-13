package com.autobots.msprodutos.api.controller;

import com.autobots.msprodutos.domain.entity.Servico;
import com.autobots.msprodutos.domain.repository.ServicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/servicos") 
public class ServicoController {

    private final ServicoRepository repository;
    public ServicoController(ServicoRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/empresa/{idEmpresa}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')") 
    public ResponseEntity<Servico> cadastrar(@PathVariable Long idEmpresa, @RequestBody Servico servico) {
        servico.setIdEmpresa(idEmpresa);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(servico));
    }

    @GetMapping("/empresa/{idEmpresa}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    public ResponseEntity<List<Servico>> listarPorEmpresa(@PathVariable Long idEmpresa) {
        List<Servico> servicos = repository.findByIdEmpresa(idEmpresa);
        return servicos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(servicos);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public ResponseEntity<Servico> atualizar(@PathVariable Long id, @RequestBody Servico dados) {
        return repository.findById(id).map(s -> {
            s.setNome(dados.getNome());
            s.setDescricao(dados.getDescricao());
            s.setValor(dados.getValor());
            return ResponseEntity.ok(repository.save(s));
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