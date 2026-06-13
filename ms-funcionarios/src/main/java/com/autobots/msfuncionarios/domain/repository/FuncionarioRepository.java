package com.autobots.msfuncionarios.domain.repository;

import com.autobots.msfuncionarios.domain.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    List<Funcionario> findByIdEmpresa(Long idEmpresa);
    Optional<Funcionario> findByCredencialNomeUsuario(String nomeUsuario);
}