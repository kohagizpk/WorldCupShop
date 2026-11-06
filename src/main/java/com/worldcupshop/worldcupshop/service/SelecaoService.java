package com.worldcupshop.worldcupshop.service;

import com.worldcupshop.worldcupshop.model.Selecao;
import com.worldcupshop.worldcupshop.repository.SelecaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class SelecaoService {
    
    private final SelecaoRepository selecaoRepository;

    public SelecaoService(SelecaoRepository selecaoRepository) {
        this.selecaoRepository = selecaoRepository;
    }

    public List<Selecao> listarTodas() {
        return selecaoRepository.findAll();
    }
    
    public Selecao buscarPorId(Long id) {
        return selecaoRepository.findById(Objects.requireNonNull(id, "ID da seleção obrigatório")).orElse(null);
    }
    
    public Selecao salvar(Selecao selecao) {
        return selecaoRepository.save(Objects.requireNonNull(selecao, "Seleção obrigatória"));
    }
    
    public void deletar(Long id) {
        selecaoRepository.deleteById(Objects.requireNonNull(id, "ID da seleção obrigatório"));
    }
}
