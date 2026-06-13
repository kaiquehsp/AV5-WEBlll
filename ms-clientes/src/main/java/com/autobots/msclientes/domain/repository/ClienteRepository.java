package com.autobots.msclientes.domain.repository;

import com.autobots.msclientes.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByIdEmpresa(Long idEmpresa);
}