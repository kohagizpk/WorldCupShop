package com.worldcupshop.worldcupshop.service;

import com.worldcupshop.worldcupshop.model.Categoria;
import com.worldcupshop.worldcupshop.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(Objects.requireNonNull(id, "ID da categoria obrigatório")).orElse(null);
    }

    public Categoria salvar(Categoria categoria) {
        return categoriaRepository.save(Objects.requireNonNull(categoria, "Categoria obrigatória"));
    }

    public void deletar(Long id) {
        categoriaRepository.deleteById(Objects.requireNonNull(id, "ID da categoria obrigatório"));
    }
}
