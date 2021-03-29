package com.senior.testesenior.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senior.testesenior.entity.ProdutoServicoEntity;
import com.senior.testesenior.exception.BusinessException;
import com.senior.testesenior.repository.PedidoProdutoServicoRepository;
import com.senior.testesenior.repository.ProdutoServicoRepository;
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
public class ProdutoServicoController {

    @Autowired
    private ProdutoServicoRepository produtoServicoRepository;

    @Autowired
    private PedidoProdutoServicoRepository pedidoProdutoServicoRepository;

    @RequestMapping(value = "/produtosServicos", method = RequestMethod.GET)
    public List<Map> Get(
            @RequestParam(name = "q", required = false) String keyWord,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "fields", required = false) List<String> fields) {
        Page<ProdutoServicoEntity> pageProdutoServico;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "nome");

        if (keyWord == null) {
            pageProdutoServico = produtoServicoRepository.findAll(pageRequest);
        } else {
            pageProdutoServico = produtoServicoRepository.findByKeyWord('%' + keyWord + '%', pageRequest);
        }

        ObjectMapper oMapper = new ObjectMapper();
        return pageProdutoServico.getContent().stream().map(u -> oMapper.convertValue(u, Map.class))
                .peek(map -> {
                    if (fields != null) {
                        map.keySet().retainAll(fields);
                    }
                }).collect(Collectors.toList());
    }

    @RequestMapping(value = "/produtosServicos/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProdutoServicoEntity> GetById(@PathVariable(value = "id") UUID id) throws NotFoundException {
        Optional<ProdutoServicoEntity> produtoServicoEntity = produtoServicoRepository.findById(id);
        if (produtoServicoEntity.isPresent()) {
            return new ResponseEntity<>(produtoServicoEntity.get(), HttpStatus.OK);
        } else {
            throw new NotFoundException("Produto/serviço não encontrado.");
        }
    }

    @RequestMapping(value = "/produtosServicos", method = RequestMethod.POST)
    public ProdutoServicoEntity Post(@Valid @RequestBody ProdutoServicoEntity produtoServicoEntity) {
        return save(produtoServicoEntity);
    }

    @RequestMapping(value = "/produtosServicos/{id}", method = RequestMethod.PUT)
    public ProdutoServicoEntity Put(@PathVariable(value = "id") UUID id, @Valid @RequestBody ProdutoServicoEntity newProdutoServicoEntity) throws NotFoundException {
        Optional<ProdutoServicoEntity> oldProdutoServicoEntity = produtoServicoRepository.findById(id);
        if (oldProdutoServicoEntity.isPresent()) {
            ProdutoServicoEntity produtoServicoEntity = oldProdutoServicoEntity.get();
            produtoServicoEntity.setNome(newProdutoServicoEntity.getNome());
            produtoServicoEntity.setTipo(newProdutoServicoEntity.getTipo());
            produtoServicoEntity.setValor(newProdutoServicoEntity.getValor());
            produtoServicoEntity.setAtivo(newProdutoServicoEntity.getAtivo());
            return save(produtoServicoEntity);
        } else {
            throw new NotFoundException("Produto/serviço não encontrado.");
        }
    }

    private ProdutoServicoEntity save(ProdutoServicoEntity produtoServicoEntity) {
        if (produtoServicoEntity.getTipoEnum() == null) {
            throw new BusinessException("Tipo inexistente.");
        }

        return produtoServicoRepository.save(produtoServicoEntity);
    }

    @RequestMapping(value = "/produtosServicos/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> Delete(@PathVariable(value = "id") UUID id) throws NotFoundException {
        Optional<ProdutoServicoEntity> produtoServicoEntity = produtoServicoRepository.findById(id);
        if (produtoServicoEntity.isPresent()) {
            if (pedidoProdutoServicoRepository.existsByProdutoServico(produtoServicoEntity.get())) {
                throw new BusinessException("Não é possível excluir um produto/serviço associado a algum pedido.");
            }

            produtoServicoRepository.delete(produtoServicoEntity.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new NotFoundException("Produto/serviço não encontrado.");
        }
    }
}
