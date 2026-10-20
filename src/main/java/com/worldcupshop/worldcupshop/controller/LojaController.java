package com.worldcupshop.worldcupshop.controller;

import com.worldcupshop.worldcupshop.repository.CategoriaRepository;
import com.worldcupshop.worldcupshop.repository.SelecaoRepository;
import com.worldcupshop.worldcupshop.service.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LojaController {

    private final ProdutoService produtos;
    private final CategoriaRepository categorias;
    private final SelecaoRepository selecoes;

    public LojaController(ProdutoService produtos, CategoriaRepository categorias,
                          SelecaoRepository selecoes) {
        this.produtos = produtos;
        this.categorias = categorias;
        this.selecoes = selecoes;
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/produtos";
    }

    @GetMapping("/produtos")
    public String listar(@RequestParam(required = false) String nome,
                         @RequestParam(required = false) Long categoria,
                         @RequestParam(required = false) Long selecao, Model model) {
        model.addAttribute("produtos", produtos.listar(nome, categoria, selecao));
        model.addAttribute("categorias", categorias.findAll());
        model.addAttribute("selecoes", selecoes.findAll());
        model.addAttribute("nome", nome);
        return "produto/listar";
    }

    @GetMapping("/produtos/{id}")
    public String detalhe(@PathVariable Long id, Model model) {
        model.addAttribute("produto", produtos.buscar(id));
        return "produto/detalhe";
    }
}
