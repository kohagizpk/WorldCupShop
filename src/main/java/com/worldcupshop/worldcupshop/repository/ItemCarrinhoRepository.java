package com.worldcupshop.worldcupshop.repository;
import com.worldcupshop.worldcupshop.model.ItemCarrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ItemCarrinhoRepository extends JpaRepository<ItemCarrinho, Long> {
    List<ItemCarrinho> findByCarrinhoId(Long carrinhoId);
    Optional<ItemCarrinho> findByCarrinhoIdAndProdutoId(Long carrinhoId, Long produtoId);
}
