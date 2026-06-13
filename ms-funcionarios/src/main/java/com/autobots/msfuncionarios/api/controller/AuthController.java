package com.autobots.msfuncionarios.api.controller;

import com.autobots.msfuncionarios.domain.entity.Funcionario;
import com.autobots.msfuncionarios.domain.repository.FuncionarioRepository;
import com.autobots.msfuncionarios.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final FuncionarioRepository repository;
    private final JwtService jwtService;

    public AuthController(FuncionarioRepository repository, JwtService jwtService) {
        this.repository = repository;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credenciais) {
        String usuario = credenciais.get("nomeUsuario");
        String senha = credenciais.get("senha");
        Funcionario funcionario = repository.findByCredencialNomeUsuario(usuario).orElse(null);
        if (funcionario != null && funcionario.getCredencial().getSenha().equals(senha)) {
            String token = jwtService.gerarToken(usuario, funcionario.getPerfil());
            return ResponseEntity.ok(token);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
    }
}