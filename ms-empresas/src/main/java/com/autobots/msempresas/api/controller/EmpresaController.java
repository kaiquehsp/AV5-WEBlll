package com.autobots.msempresas.api.controller;

import com.autobots.msempresas.domain.entity.Empresa;
import com.autobots.msempresas.domain.repository.EmpresaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

import java.util.List;

@RestController
@RequestMapping("/api/v1/empresas")
public class EmpresaController {

    private final EmpresaRepository repository;

    public EmpresaController(EmpresaRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Empresa> cadastrar(@RequestBody Empresa empresa) {
        empresa.setCadastro(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(empresa));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public ResponseEntity<List<Empresa>> listarTodas() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    public ResponseEntity<Empresa> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Empresa> atualizar(@PathVariable Long id, @RequestBody Empresa dados) {
        return repository.findById(id).map(empresa -> {
            empresa.setRazaoSocial(dados.getRazaoSocial());
            empresa.setNomeFantasia(dados.getNomeFantasia());
            empresa.setCnpj(dados.getCnpj());
            

            if (dados.getEndereco() != null) {
                empresa.setEndereco(dados.getEndereco());
            }
            if (dados.getTelefones() != null) {
                empresa.getTelefones().clear();
                empresa.getTelefones().addAll(dados.getTelefones());
            }
            
            return ResponseEntity.ok(repository.save(empresa));
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