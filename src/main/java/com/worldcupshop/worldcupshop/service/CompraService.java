package com.worldcupshop.worldcupshop.service;

import com.worldcupshop.worldcupshop.model.*;
import com.worldcupshop.worldcupshop.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class CompraService {

    private final CarrinhoRepository carrinhos;
    private final ItemCarrinhoRepository itensCarrinho;
    private final PedidoRepository pedidos;
    private final ItemPedidoRepository itensPedido;
    private final ProdutoRepository produtos;

    public CompraService(CarrinhoRepository carrinhos, ItemCarrinhoRepository itensCarrinho,
                         PedidoRepository pedidos, ItemPedidoRepository itensPedido,
                         ProdutoRepository produtos) {
        this.carrinhos = carrinhos;
        this.itensCarrinho = itensCarrinho;
        this.pedidos = pedidos;
        this.itensPedido = itensPedido;
        this.produtos = produtos;
    }

    @Transactional
    public Carrinho carrinho(Usuario usuario) {
        Carrinho carrinho = carrinhos.findByUsuarioId(usuario.getId()).orElse(null);
        if (carrinho == null) {
            carrinho = new Carrinho();
            carrinho.setUsuario(usuario);
            carrinho = carrinhos.save(carrinho);
        }
        return carrinho;
    }

    public List<ItemCarrinho> itens(Usuario usuario) {
        return itensCarrinho.findByCarrinhoId(carrinho(usuario).getId());
    }

    public BigDecimal total(Usuario usuario) {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemCarrinho item : itens(usuario)) {
            total = total.add(item.getProduto().getPreco()
                    .multiply(BigDecimal.valueOf(item.getQuantidade())));
        }
        return total;
    }

    @Transactional
    public void adicionar(Usuario usuario, Long produtoId, Integer quantidade) {
        if (quantidade == null || quantidade < 1) throw new IllegalArgumentException("Quantidade inválida.");
        Produto produto = produtos.findById(produtoId).orElseThrow();
        Carrinho carrinho = carrinho(usuario);
        ItemCarrinho item = itensCarrinho.findByCarrinhoIdAndProdutoId(carrinho.getId(), produtoId).orElse(null);
        if (item == null) {
            item = new ItemCarrinho();
            item.setCarrinho(carrinho);
            item.setProduto(produto);
            item.setQuantidade(0);
        }
        int novaQuantidade = item.getQuantidade() + quantidade;
        if (novaQuantidade > produto.getEstoque()) throw new IllegalArgumentException("Estoque insuficiente.");
        item.setQuantidade(novaQuantidade);
        itensCarrinho.save(item);
    }

    @Transactional
    public void alterar(Usuario usuario, Long itemId, Integer quantidade) {
        ItemCarrinho item = itensCarrinho.findById(itemId).orElseThrow();
        if (!item.getCarrinho().getUsuario().getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("Item inválido.");
        }
        if (quantidade == null || quantidade < 1) {
            itensCarrinho.delete(item);
            return;
        }
        if (quantidade > item.getProduto().getEstoque()) throw new IllegalArgumentException("Estoque insuficiente.");
        item.setQuantidade(quantidade);
        itensCarrinho.save(item);
    }

    @Transactional
    public void finalizar(Usuario usuario) {
        List<ItemCarrinho> itens = itens(usuario);
        if (itens.isEmpty()) throw new IllegalArgumentException("Carrinho vazio.");
        BigDecimal total = BigDecimal.ZERO;

        // Verifica todos os produtos antes de criar o pedido.
        for (ItemCarrinho item : itens) {
            Produto produto = produtos.bloquearPorId(item.getProduto().getId()).orElseThrow();
            if (item.getQuantidade() < 1 || produto.getEstoque() < item.getQuantidade()) {
                throw new IllegalArgumentException("Estoque insuficiente para " + produto.getNome());
            }
            total = total.add(produto.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())));
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setValorTotal(total);
        pedidos.save(pedido);

        for (ItemCarrinho item : itens) {
            Produto produto = produtos.findById(item.getProduto().getId()).orElseThrow();
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(pedido);
            itemPedido.setProduto(produto);
            itemPedido.setQuantidade(item.getQuantidade());
            itemPedido.setPrecoUnitario(produto.getPreco());
            itensPedido.save(itemPedido);
            produto.setEstoque(produto.getEstoque() - item.getQuantidade());
            produtos.save(produto);
        }
        itensCarrinho.deleteAll(itens);
    }

    public List<Pedido> historico(Usuario usuario) {
        return pedidos.findByUsuarioIdOrderByDataDesc(usuario.getId());
    }

    public List<ItemPedido> itensPedido(Long id) {
        return itensPedido.findByPedidoId(id);
    }
}
