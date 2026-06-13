package com.autobots.msempresas.config;

import com.autobots.msempresas.domain.entity.Empresa;
import com.autobots.msempresas.domain.repository.EmpresaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class EmpresaSeeder implements CommandLineRunner {

    private final EmpresaRepository repository;

    public EmpresaSeeder(EmpresaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            
            Empresa empresa = new Empresa();
            empresa.setRazaoSocial("Volkswagen Automóveis S.A.");
            empresa.setNomeFantasia("VW Matriz - Taubaté");
            empresa.setCnpj("11.111.111/0001-11");
            empresa.setCadastro(LocalDateTime.now());
            
            repository.save(empresa);
            
            System.out.println("✅ [AUTO-MANAGER] Empresa Matriz (VW) injetada com sucesso! (ID: 1)");
        }
    }
}