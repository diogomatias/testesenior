package com.senior.testesenior.repository;

import com.senior.testesenior.entity.ProdutoServicoEntity;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Diogo Matias
 */
public interface ProdutoServicoRepository extends JpaRepository<ProdutoServicoEntity, UUID> {

    @Query("SELECT produtoServico FROM com.senior.testesenior.entity.ProdutoServicoEntity produtoServico")
    @Override
    Page<ProdutoServicoEntity> findAll(Pageable pageable);

    @Query("  SELECT produtoServico "
            + " FROM com.senior.testesenior.entity.ProdutoServicoEntity produtoServico "
            + "WHERE produtoServico.nome LIKE ?1")
    Page<ProdutoServicoEntity> findByKeyWord(String keyWord, Pageable pageable);

    @Query("  SELECT produtoServico "
            + " FROM com.senior.testesenior.entity.ProdutoServicoEntity produtoServico "
            + "WHERE produtoServico.id IN ?1")
    List<ProdutoServicoEntity> findById(Set<UUID> idsProdutosServicos);
}
