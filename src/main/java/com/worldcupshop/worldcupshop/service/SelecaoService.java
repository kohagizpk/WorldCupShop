package com.worldcupshop.worldcupshop.service;

import com.worldcupshop.worldcupshop.model.Selecao;
import com.worldcupshop.worldcupshop.repository.SelecaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SelecaoService {
    
    @Autowired
    private SelecaoRepository selecaoRepository;

    public List<Selecao> listarTodas() {
        return selecaoRepository.findAll();
    }
    
    public Selecao buscarPorId(Long id) {
        return selecaoRepository.findById(id).orElse(null);
    }
    
    public Selecao salvar(Selecao selecao) {
        return selecaoRepository.save(selecao);
    }
    
    public void deletar(Long id) {
        selecaoRepository.deleteById(id);
    }
}