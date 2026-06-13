package com.autobots.msfuncionarios.config;

import com.autobots.msfuncionarios.domain.entity.Credencial;
import com.autobots.msfuncionarios.domain.entity.Funcionario;
import com.autobots.msfuncionarios.domain.repository.FuncionarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminSeeder implements CommandLineRunner {

    private final FuncionarioRepository repository;

    public AdminSeeder(FuncionarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.findByCredencialNomeUsuario("admin").isEmpty()) {
            
            Funcionario admin = new Funcionario();
            admin.setNome("Administrador Mestre");
            admin.setPerfil("ADMINISTRADOR");
            admin.setIdEmpresa(1L);
            
            Credencial credencial = new Credencial();
            credencial.setNomeUsuario("admin");
            credencial.setSenha("admin123"); 
            admin.setCredencial(credencial);

            repository.save(admin);
            System.out.println("[AUTO-MANAGER] Usuário ADMIN injetado com sucesso! (Login: admin / Senha: admin123)");
        }
    }
}