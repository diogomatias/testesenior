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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javassist.NotFoundException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public class PedidoProdutoServicoController {

    @Autowired
    private PedidoProdutoServicoRepository pedidoProdutoServicoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @RequestMapping(value = "/pedidosProdutosServicos", method = RequestMethod.GET)
    public List<Map> Get(
            @RequestParam(name = "q", required = false) String keyWord,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "fields", required = false) List<String> fields) {
        Page<PedidoProdutoServicoEntity> pagePedidoProdutoServico;
        PageRequest pageRequest = PageRequest.of(page, size);

        if (keyWord == null) {
            pagePedidoProdutoServico = pedidoProdutoServicoRepository.findAll(pageRequest);
        } else {
            pagePedidoProdutoServico = pedidoProdutoServicoRepository.findByKeyWord('%' + keyWord + '%', pageRequest);
        }

        ObjectMapper oMapper = new ObjectMapper();
        return pagePedidoProdutoServico.getContent().stream().map(u -> oMapper.convertValue(u, Map.class))
                .peek(map -> {
                    if (fields != null) {
                        map.keySet().retainAll(fields);
                    }
                }).collect(Collectors.toList());
    }

    @RequestMapping(value = "/pedidosProdutosServicos/{id}", method = RequestMethod.GET)
    public ResponseEntity<PedidoProdutoServicoEntity> GetById(@PathVariable(value = "id") UUID id) throws NotFoundException {
        Optional<PedidoProdutoServicoEntity> pedidoProdutoServicoEntity = pedidoProdutoServicoRepository.findById(id);
        if (pedidoProdutoServicoEntity.isPresent()) {
            return new ResponseEntity<>(pedidoProdutoServicoEntity.get(), HttpStatus.OK);
        } else {
            throw new NotFoundException("Pedido e produto/serviço não encontrado.");
        }
    }

    private void atualizarValorTotalPedido(PedidoEntity pedidoEntity) {
        Optional<PedidoEntity> optionalPedidoEntity = pedidoRepository.findById(pedidoEntity.getId());
        if (optionalPedidoEntity.isPresent()) {
            pedidoEntity = optionalPedidoEntity.get();
        }

        List<PedidoProdutoServicoEntity> pedidosProdutosServicos = pedidoProdutoServicoRepository.findByPedido(pedidoEntity);
        Double valorPedido = 0.0;
        Double valorProdutos = 0.0;

        for (PedidoProdutoServicoEntity pedidoProdutoServicoEntity : pedidosProdutosServicos) {
            ProdutoServicoEntity produtoServicoEntity = pedidoProdutoServicoEntity.getProdutoServico();

            if (pedidoEntity.getPercentualDesconto() != null
                    && pedidoEntity.getPercentualDesconto() > 0.0
                    && PedidoSituacaoEnum.ABERTO.equals(pedidoEntity.getSituacaoEnum())
                    && ProdutoServicoTipoEnum.PRODUTO.equals(produtoServicoEntity.getTipoEnum())) {
                valorProdutos += produtoServicoEntity.getValor();
            }

            valorPedido += produtoServicoEntity.getValor();
        }

        if (valorProdutos > 0.0) {
            valorPedido -= (valorProdutos * pedidoEntity.getPercentualDesconto() / 100);
        }

        pedidoEntity.setValor(valorPedido);
        pedidoRepository.save(pedidoEntity);
    }

    @RequestMapping(value = "/pedidosProdutosServicos/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> Delete(@PathVariable(value = "id") UUID id) throws NotFoundException {
        Optional<PedidoProdutoServicoEntity> pedidoProdutoServicoEntity = pedidoProdutoServicoRepository.findById(id);
        if (pedidoProdutoServicoEntity.isPresent()) {
            if (pedidoProdutoServicoRepository.findByPedido(pedidoProdutoServicoEntity.get().getPedido()).size() == 1) {
                throw new BusinessException("Não é possível excluir o vínculo do pedido e produto/serviço porque o pedido ficara sem produtos/servicos.");
            }
            PedidoEntity pedidoEntity = pedidoProdutoServicoEntity.get().getPedido();
            pedidoProdutoServicoRepository.delete(pedidoProdutoServicoEntity.get());
            atualizarValorTotalPedido(pedidoEntity);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new NotFoundException("Pedido e produto/serviço não encontrado.");
        }
    }
}
