package com.worldcupshop.worldcupshop.repository;
import com.worldcupshop.worldcupshop.model.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    Optional<Carrinho> findByUsuarioId(Long usuarioId);
}
