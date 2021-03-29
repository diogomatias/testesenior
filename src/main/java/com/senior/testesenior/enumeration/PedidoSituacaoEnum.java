package com.senior.testesenior.enumeration;

/**
 *
 * @author Diogo Matias
 */
public enum PedidoSituacaoEnum {
    ABERTO(0, "Aberto"),
    FECHADO(1, "Fechado");

    private Integer codigo;
    private String descricao;

    private PedidoSituacaoEnum(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public static PedidoSituacaoEnum getEnum(Integer codigo) {
        try {
            return PedidoSituacaoEnum.values()[codigo];
        } catch (Exception e) {
            return null;
        }
    }

    public boolean equals(Integer codigo) {
        return this.getCodigo().equals(codigo);
    }
}
