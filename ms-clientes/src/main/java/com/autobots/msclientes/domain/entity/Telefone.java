package com.autobots.msclientes.domain.entity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "telefones_cliente")
public class Telefone {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ddd;
    private String numero;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDdd() { return ddd; }
    public void setDdd(String ddd) { this.ddd = ddd; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
}