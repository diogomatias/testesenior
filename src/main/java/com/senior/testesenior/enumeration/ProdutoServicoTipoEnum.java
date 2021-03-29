package com.senior.testesenior.enumeration;

/**
 *
 * @author Diogo Matias
 */
public enum ProdutoServicoTipoEnum {
    PRODUTO(0, "Produto"),
    SERVICO(1, "Serviço");

    private Integer codigo;
    private String descricao;

    private ProdutoServicoTipoEnum(Integer codigo, String descricao) {
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

    public static ProdutoServicoTipoEnum getEnum(Integer codigo) {
        try {
            return ProdutoServicoTipoEnum.values()[codigo];
        } catch (Exception e) {
            return null;
        }
    }

    public boolean equals(Integer codigo) {
        return this.getCodigo().equals(codigo);
    }
}
