package com.worldcupshop.worldcupshop.repository;
import com.worldcupshop.worldcupshop.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioIdOrderByDataDesc(Long usuarioId);
}
