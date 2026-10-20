package com.worldcupshop.worldcupshop.service;

import com.worldcupshop.worldcupshop.model.*;
import com.worldcupshop.worldcupshop.repository.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtos;
    private final ItemPedidoRepository itensPedido;

    public ProdutoService(ProdutoRepository produtos, ItemPedidoRepository itensPedido) {
        this.produtos = produtos;
        this.itensPedido = itensPedido;
    }

    public List<Produto> listar(String nome, Long categoria, Long selecao) {
        List<Produto> resultado = new ArrayList<>();
        for (Produto produto : produtos.findAll()) {
            if (nome != null && !produto.getNome().toLowerCase().contains(nome.toLowerCase())) continue;
            if (categoria != null && !produto.getCategoria().getId().equals(categoria)) continue;
            if (selecao != null && !produto.getSelecao().getId().equals(selecao)) continue;
            resultado.add(produto);
        }
        return resultado;
    }

    public Produto buscar(Long id) {
        return produtos.findById(id).orElse(null);
    }

    public Produto salvar(Produto produto) {
        if (produto.getNome() == null || produto.getNome().isBlank()
                || produto.getPreco() == null || produto.getPreco().signum() < 0
                || produto.getEstoque() == null || produto.getEstoque() < 0
                || produto.getCategoria() == null || produto.getSelecao() == null) {
            throw new IllegalArgumentException("Dados do produto inválidos.");
        }
        return produtos.save(produto);
    }

    public void excluir(Long id) {
        if (itensPedido.existsByProdutoIdAndPedidoStatusNot(id, StatusPedido.CANCELADO)) {
            throw new IllegalArgumentException("Produto já utilizado em pedido.");
        }
        produtos.deleteById(id);
    }
}
