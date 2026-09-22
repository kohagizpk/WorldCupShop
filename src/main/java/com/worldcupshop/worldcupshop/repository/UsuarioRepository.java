package com.worldcupshop.worldcupshop.repository;

import com.worldcupshop.worldcupshop.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}