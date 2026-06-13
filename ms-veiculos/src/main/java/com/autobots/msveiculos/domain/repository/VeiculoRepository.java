package com.autobots.msveiculos.domain.repository;

import com.autobots.msveiculos.domain.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    List<Veiculo> findByIdEmpresa(Long idEmpresa);
}