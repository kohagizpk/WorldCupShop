package com.worldcupshop.worldcupshop.repository;

import com.worldcupshop.worldcupshop.model.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
}