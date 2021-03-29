package com.senior.testesenior.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.senior.testesenior.enumeration.ProdutoServicoTipoEnum;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Diogo Matias
 */
@Entity
@Table(name = "produtos_servicos", schema = "public")
public class ProdutoServicoEntity implements Serializable {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(nullable = false)
    private Integer tipo;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public ProdutoServicoTipoEnum getTipoEnum() {
        return ProdutoServicoTipoEnum.getEnum(getTipo());
    }

    public void setTipoEnum(ProdutoServicoTipoEnum tipoEnum) {
        this.tipo = tipoEnum.getCodigo();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProdutoServicoEntity other = (ProdutoServicoEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
