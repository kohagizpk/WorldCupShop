package com.worldcupshop.worldcupshop.repository;
import com.worldcupshop.worldcupshop.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    List<ItemPedido> findByPedidoId(Long pedidoId);
    boolean existsByProdutoIdAndPedidoStatusNot(Long produtoId, com.worldcupshop.worldcupshop.model.StatusPedido status);
}
