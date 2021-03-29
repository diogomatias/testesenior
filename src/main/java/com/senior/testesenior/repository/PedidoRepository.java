package com.senior.testesenior.repository;

import com.senior.testesenior.entity.PedidoEntity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Diogo Matias
 */
public interface PedidoRepository extends JpaRepository<PedidoEntity, UUID> {

    @Query("SELECT pedido FROM com.senior.testesenior.entity.PedidoEntity pedido")
    @Override
    Page<PedidoEntity> findAll(Pageable pageable);

    @Query("  SELECT pedido "
            + " FROM com.senior.testesenior.entity.PedidoEntity pedido "
            + "WHERE EXISTS(SELECT 1 "
            + "               FROM com.senior.testesenior.entity.PedidoProdutoServicoEntity pedidoProdutoServico "
            + "              WHERE pedidoProdutoServico.pedido = pedido "
            + "                AND pedidoProdutoServico.produtoServico.nome LIKE ?1)")
    Page<PedidoEntity> findByKeyWord(String keyWord, Pageable pageable);
}
