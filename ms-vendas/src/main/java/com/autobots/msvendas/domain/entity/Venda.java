package com.autobots.msvendas.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "vendas")
public class Venda {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idEmpresa;
    private Long idCliente;
    private Long idFuncionario;

    private LocalDateTime dataVenda;


    @ElementCollection
    @CollectionTable(name = "venda_mercadorias", joinColumns = @JoinColumn(name = "venda_id"))
    @Column(name = "id_mercadoria")
    private List<Long> idsMercadorias = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "venda_servicos", joinColumns = @JoinColumn(name = "venda_id"))
    @Column(name = "id_servico")
    private List<Long> idsServicos = new ArrayList<>();

    @PrePersist
    public void prePersist() { this.dataVenda = LocalDateTime.now(); }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdEmpresa() { return idEmpresa; }
    public void setIdEmpresa(Long idEmpresa) { this.idEmpresa = idEmpresa; }
    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }
    public Long getIdFuncionario() { return idFuncionario; }
    public void setIdFuncionario(Long idFuncionario) { this.idFuncionario = idFuncionario; }
    public LocalDateTime getDataVenda() { return dataVenda; }
    public List<Long> getIdsMercadorias() { return idsMercadorias; }
    public void setIdsMercadorias(List<Long> ids) { this.idsMercadorias = ids; }
    public List<Long> getIdsServicos() { return idsServicos; }
    public void setIdsServicos(List<Long> ids) { this.idsServicos = ids; }
}