package com.worldcupshop.worldcupshop.repository;

import com.worldcupshop.worldcupshop.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}