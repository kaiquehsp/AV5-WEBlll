package com.autobots.msveiculos.api.controller;

import com.autobots.msveiculos.domain.entity.Veiculo;
import com.autobots.msveiculos.domain.repository.VeiculoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/veiculos")
public class VeiculoController {

    private final VeiculoRepository repository;

    public VeiculoController(VeiculoRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/empresa/{idEmpresa}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENDEDOR')")
    public ResponseEntity<Veiculo> cadastrar(@PathVariable Long idEmpresa, @RequestBody Veiculo veiculo) {
        veiculo.setIdEmpresa(idEmpresa);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(veiculo));
    }

    @GetMapping("/empresa/{idEmpresa}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENDEDOR')")
    public ResponseEntity<List<Veiculo>> listarPorEmpresa(@PathVariable Long idEmpresa) {
        List<Veiculo> veiculos = repository.findByIdEmpresa(idEmpresa);
        return veiculos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(veiculos);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENDEDOR')")
    public ResponseEntity<Veiculo> atualizar(@PathVariable Long id, @RequestBody Veiculo dados) {
        return repository.findById(id).map(v -> {
            v.setPlaca(dados.getPlaca());
            v.setModelo(dados.getModelo());
            return ResponseEntity.ok(repository.save(v));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENDEDOR')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    //a
}