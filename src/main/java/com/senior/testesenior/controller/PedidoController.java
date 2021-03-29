package com.senior.testesenior.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senior.testesenior.entity.PedidoEntity;
import com.senior.testesenior.entity.PedidoProdutoServicoEntity;
import com.senior.testesenior.entity.ProdutoServicoEntity;
import com.senior.testesenior.enumeration.PedidoSituacaoEnum;
import com.senior.testesenior.enumeration.ProdutoServicoTipoEnum;
import com.senior.testesenior.exception.BusinessException;
import com.senior.testesenior.repository.PedidoProdutoServicoRepository;
import com.senior.testesenior.repository.PedidoRepository;
import com.senior.testesenior.repository.ProdutoServicoRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;
import javassist.NotFoundException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Diogo Matias
 */
@RestController
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoServicoRepository produtoServicoRepository;

    @Autowired
    private PedidoProdutoServicoRepository pedidoProdutoServicoRepository;

    @RequestMapping(value = "/pedidos", method = RequestMethod.GET)
    public List<Map> Get(@RequestParam(name = "q", required = false) String keyWord,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "fields", required = false) List<String> fields) {
        Page<PedidoEntity> pagePedido;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "entrada");

        if (keyWord == null) {
            pagePedido = pedidoRepository.findAll(pageRequest);
        } else {
            pagePedido = pedidoRepository.findByKeyWord('%' + keyWord + '%', pageRequest);
        }

        ObjectMapper oMapper = new ObjectMapper();
        return pagePedido.getContent().stream().map(u -> oMapper.convertValue(u, Map.class))
                .peek(map -> {
                    if (fields != null) {
                        map.keySet().retainAll(fields);
                    }
                }).collect(Collectors.toList());
    }

    @RequestMapping(value = "/pedidos/{id}", method = RequestMethod.GET)
    public ResponseEntity<PedidoEntity> GetById(@PathVariable(value = "id") UUID id) throws NotFoundException {
        Optional<PedidoEntity> pedidoEntity = pedidoRepository.findById(id);
        if (pedidoEntity.isPresent()) {
            return new ResponseEntity<>(pedidoEntity.get(), HttpStatus.OK);
        } else {
            throw new NotFoundException("Pedido não encontrado.");
        }
    }

    @RequestMapping(value = "/pedidos", method = RequestMethod.POST)
    public PedidoEntity Post(@Valid @RequestBody PedidoEntity pedidoEntity) {
        return save(pedidoEntity);
    }

    @RequestMapping(value = "/pedidos/{id}", method = RequestMethod.PUT)
    public PedidoEntity Put(@PathVariable(value = "id") UUID id, @Valid @RequestBody PedidoEntity newPedidoEntity) throws NotFoundException {
        Optional<PedidoEntity> oldPedidoEntity = pedidoRepository.findById(id);
        if (oldPedidoEntity.isPresent()) {
            PedidoEntity pedidoEntity = oldPedidoEntity.get();
            pedidoEntity.setPercentualDesconto(newPedidoEntity.getPercentualDesconto());
            pedidoEntity.setSituacao(newPedidoEntity.getSituacao());
            pedidoEntity.setIdsProdutosServicos(newPedidoEntity.getIdsProdutosServicos());
            return save(pedidoEntity);
        } else {
            throw new NotFoundException("Pedido não encontrado.");
        }
    }

    private PedidoEntity save(PedidoEntity pedidoEntity) {
        Set<UUID> idsProdutosServicos = pedidoEntity.getIdsProdutosServicos();

        if (idsProdutosServicos == null || idsProdutosServicos.isEmpty()) {
            throw new BusinessException("Pedido sem produtos ou serviços.");

        } else if (pedidoEntity.getSituacaoEnum() == null) {
            throw new BusinessException("Situação inexistente.");
        }

        List<ProdutoServicoEntity> produtosServicos = produtoServicoRepository.findById(idsProdutosServicos);
        List<PedidoProdutoServicoEntity> pedidoProdutosServicos = new ArrayList<>();
        Set<UUID> idsProdutosServicosEncontrados = new TreeSet<>();
        Double valorPedido = 0.0;
        Double valorProdutos = 0.0;

        for (ProdutoServicoEntity produtoServicoEntity : produtosServicos) {
            if (!produtoServicoEntity.getAtivo()) {
                throw new BusinessException("O produto/serviço " + produtoServicoEntity.getNome() + " está desativado.");

            } else if (pedidoEntity.getPercentualDesconto() != null && pedidoEntity.getPercentualDesconto() > 0.0) {
                if (!PedidoSituacaoEnum.ABERTO.equals(pedidoEntity.getSituacaoEnum())) {
                    throw new BusinessException("É possível aplicar percentual de desconto somente a pedidos abertos.");

                } else if (ProdutoServicoTipoEnum.PRODUTO.equals(produtoServicoEntity.getTipoEnum())) {
                    valorProdutos += produtoServicoEntity.getValor();
                }
            }

            valorPedido += produtoServicoEntity.getValor();
            idsProdutosServicosEncontrados.add(produtoServicoEntity.getId());

            PedidoProdutoServicoEntity pedidoProdutoServico = new PedidoProdutoServicoEntity();
            pedidoProdutoServico.setPedido(pedidoEntity);
            pedidoProdutoServico.setProdutoServico(produtoServicoEntity);
            pedidoProdutosServicos.add(pedidoProdutoServico);
        }

        if (idsProdutosServicosEncontrados.size() < idsProdutosServicos.size()) {
            idsProdutosServicos.removeAll(idsProdutosServicosEncontrados);
            StringBuilder produtosServicosNaoEncontrados = new StringBuilder();
            for (UUID idProdutoServico : idsProdutosServicos) {
                if (produtosServicosNaoEncontrados.length() > 0) {
                    produtosServicosNaoEncontrados.append(", ");
                }
                produtosServicosNaoEncontrados.append(idProdutoServico);
            }

            throw new BusinessException("Os seguintes produtos/serviços não foram encontrados: " + produtosServicosNaoEncontrados + ".");
        }

        if (valorProdutos > 0.0) {
            valorPedido -= (valorProdutos * pedidoEntity.getPercentualDesconto() / 100);
        }

        if (pedidoEntity.getId() != null) {
            List<PedidoProdutoServicoEntity> pedidoProdutosServicosExcluir = pedidoProdutoServicoRepository.findByPedido(pedidoEntity);
            pedidoProdutoServicoRepository.deleteAll(pedidoProdutosServicosExcluir);
        }

        pedidoEntity.setValor(valorPedido);
        pedidoEntity.setEntrada(new Date());

        pedidoRepository.save(pedidoEntity);
        pedidoProdutoServicoRepository.saveAll(pedidoProdutosServicos);

        return pedidoEntity;
    }

    @RequestMapping(value = "/pedidos/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> Delete(@PathVariable(value = "id") UUID id) throws NotFoundException {
        Optional<PedidoEntity> pedidoEntity = pedidoRepository.findById(id);
        if (pedidoEntity.isPresent()) {
            List<PedidoProdutoServicoEntity> pedidoProdutosServicosExcluir = pedidoProdutoServicoRepository.findByPedido(pedidoEntity.get());
            pedidoProdutoServicoRepository.deleteAll(pedidoProdutosServicosExcluir);
            pedidoRepository.delete(pedidoEntity.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new NotFoundException("Pedido não encontrado.");
        }
    }
}
