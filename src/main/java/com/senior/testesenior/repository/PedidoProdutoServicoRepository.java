package com.senior.testesenior.repository;

import com.senior.testesenior.entity.PedidoEntity;
import com.senior.testesenior.entity.PedidoProdutoServicoEntity;
import com.senior.testesenior.entity.ProdutoServicoEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Diogo Matias
 */
public interface PedidoProdutoServicoRepository extends JpaRepository<PedidoProdutoServicoEntity, UUID> {

    @Query("SELECT pedidoProdutoServico FROM com.senior.testesenior.entity.PedidoProdutoServicoEntity pedidoProdutoServico")
    @Override
    Page<PedidoProdutoServicoEntity> findAll(Pageable pageable);

    @Query("  SELECT pedidoProdutoServico "
            + " FROM com.senior.testesenior.entity.PedidoProdutoServicoEntity pedidoProdutoServico "
            + "WHERE pedidoProdutoServico.produtoServico.nome LIKE ?1")
    Page<PedidoProdutoServicoEntity> findByKeyWord(String keyWord, Pageable pageable);

    @Query("   SELECT pedidoProdutoServico "
            + "  FROM com.senior.testesenior.entity.PedidoProdutoServicoEntity pedidoProdutoServico "
            + " WHERE pedidoProdutoServico.pedido = ?1 ")
    List<PedidoProdutoServicoEntity> findByPedido(PedidoEntity pedido);

    @Query("   SELECT CASE WHEN COUNT(1) > 0 THEN TRUE ELSE FALSE END "
            + "  FROM com.senior.testesenior.entity.PedidoProdutoServicoEntity pedidoProdutoServico "
            + " WHERE pedidoProdutoServico.produtoServico = ?1 ")
    boolean existsByProdutoServico(ProdutoServicoEntity produtoServico);
}
