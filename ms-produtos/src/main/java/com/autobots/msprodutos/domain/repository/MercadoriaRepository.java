package com.autobots.msprodutos.domain.repository;

import com.autobots.msprodutos.domain.entity.Mercadoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MercadoriaRepository extends JpaRepository<Mercadoria, Long> {
    List<Mercadoria> findByIdEmpresa(Long idEmpresa);
}