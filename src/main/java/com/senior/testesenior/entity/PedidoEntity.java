package com.senior.testesenior.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.senior.testesenior.enumeration.PedidoSituacaoEnum;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

/**
 *
 * @author Diogo Matias
 */
@Entity
@Table(name = "pedidos", schema = "public")
public class PedidoEntity implements Serializable {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "percentual_desconto")
    private Double percentualDesconto;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date entrada;

    @NotBlank(message = "Situação é obrigatória")
    @Column(nullable = false)
    private Integer situacao;

    @OneToMany(mappedBy = "pedido")
    Set<PedidoProdutoServicoEntity> pedidosProdutosServicos;

    @Transient
    private Set<UUID> idsProdutosServicos;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getPercentualDesconto() {
        return percentualDesconto;
    }

    public void setPercentualDesconto(Double percentualDesconto) {
        this.percentualDesconto = percentualDesconto;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getEntrada() {
        return entrada;
    }

    public void setEntrada(Date entrada) {
        this.entrada = entrada;
    }

    public Integer getSituacao() {
        return situacao;
    }

    public void setSituacao(Integer situacao) {
        this.situacao = situacao;
    }

    public PedidoSituacaoEnum getSituacaoEnum() {
        return PedidoSituacaoEnum.getEnum(getSituacao());
    }

    public void setSituacaoEnum(PedidoSituacaoEnum situacaoEnum) {
        this.situacao = situacaoEnum.getCodigo();
    }

    public Set<PedidoProdutoServicoEntity> getPedidosProdutosServicos() {
        return pedidosProdutosServicos;
    }

    public void setPedidosProdutosServicos(Set<PedidoProdutoServicoEntity> pedidosProdutosServicos) {
        this.pedidosProdutosServicos = pedidosProdutosServicos;
    }

    public Set<UUID> getIdsProdutosServicos() {
        return idsProdutosServicos;
    }

    public void setIdsProdutosServicos(Set<UUID> idsProdutosServicos) {
        this.idsProdutosServicos = idsProdutosServicos;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.id);
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
        final PedidoEntity other = (PedidoEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
