package com.autobots.msvendas.domain.repository;

import com.autobots.msvendas.domain.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    List<Venda> findByIdEmpresaAndDataVendaBetween(Long idEmpresa,
                                                    LocalDateTime inicio,
                                                    LocalDateTime fim);
}