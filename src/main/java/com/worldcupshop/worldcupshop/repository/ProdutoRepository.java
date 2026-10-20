package com.worldcupshop.worldcupshop.repository;
import com.worldcupshop.worldcupshop.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import jakarta.persistence.LockModeType;
import org.springframework.data.repository.query.Param;
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Produto p where p.id = :id")
    Optional<Produto> bloquearPorId(@Param("id") Long id);
    List<Produto> findByNomeContainingIgnoreCase(String nome);
}
