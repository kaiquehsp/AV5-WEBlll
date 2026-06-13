package com.autobots.msfuncionarios.api.controller;

import com.autobots.msfuncionarios.domain.entity.Funcionario;
import com.autobots.msfuncionarios.domain.repository.FuncionarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/funcionarios")
public class FuncionarioController {

    private final FuncionarioRepository repository;

    public FuncionarioController(FuncionarioRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/empresa/{idEmpresa}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public ResponseEntity<Funcionario> cadastrar(@PathVariable Long idEmpresa, @RequestBody Funcionario funcionario) {
        funcionario.setIdEmpresa(idEmpresa);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(funcionario));
    }

    @GetMapping("/empresa/{idEmpresa}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public ResponseEntity<List<Funcionario>> listarPorEmpresa(@PathVariable Long idEmpresa) {
        List<Funcionario> funcionarios = repository.findByIdEmpresa(idEmpresa);
        return funcionarios.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(funcionarios);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public ResponseEntity<Funcionario> atualizar(@PathVariable Long id, @RequestBody Funcionario dados) {
        return repository.findById(id).map(f -> {
            f.setNome(dados.getNome());
            f.setPerfil(dados.getPerfil());
            return ResponseEntity.ok(repository.save(f));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')") 
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}