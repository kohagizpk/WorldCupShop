package com.worldcupshop.worldcupshop.controller;

import com.worldcupshop.worldcupshop.model.Pedido;
import com.worldcupshop.worldcupshop.model.Usuario;
import com.worldcupshop.worldcupshop.service.CompraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CompraController {

    private final CompraService compras;

    public CompraController(CompraService compras) {
        this.compras = compras;
    }

    @GetMapping("/carrinho")
    public String carrinho(@RequestAttribute("usuarioLogado") Usuario usuario, Model model) {
        model.addAttribute("itens", compras.itens(usuario));
        model.addAttribute("total", compras.total(usuario));
        return "carrinho/listar";
    }

    @PostMapping("/carrinho/adicionar")
    public String adicionar(@RequestAttribute("usuarioLogado") Usuario usuario,
                            @RequestParam Long produtoId,
                            @RequestParam(defaultValue = "1") Integer quantidade, Model model) {
        try {
            compras.adicionar(usuario, produtoId, quantidade);
            return "redirect:/carrinho";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "erro";
        }
    }

    @PostMapping("/carrinho/alterar/{id}")
    public String alterar(@RequestAttribute("usuarioLogado") Usuario usuario,
                          @PathVariable Long id, @RequestParam Integer quantidade, Model model) {
        try {
            compras.alterar(usuario, id, quantidade);
            return "redirect:/carrinho";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "erro";
        }
    }

    @PostMapping("/pedidos/finalizar")
    public String finalizar(@RequestAttribute("usuarioLogado") Usuario usuario, Model model) {
        try {
            compras.finalizar(usuario);
            return "redirect:/pedidos?sucesso";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "erro";
        }
    }

    @GetMapping("/pedidos")
    public String pedidos(@RequestAttribute("usuarioLogado") Usuario usuario, Model model) {
        model.addAttribute("pedidos", compras.historico(usuario));
        return "pedido/listar";
    }

    @GetMapping("/pedidos/{id}")
    public String detalhe(@RequestAttribute("usuarioLogado") Usuario usuario,
                          @PathVariable Long id, Model model) {
        Pedido pedido = null;
        for (Pedido atual : compras.historico(usuario)) {
            if (atual.getId().equals(id)) pedido = atual;
        }
        if (pedido == null) return "redirect:/pedidos";
        model.addAttribute("pedido", pedido);
        model.addAttribute("itens", compras.itensPedido(id));
        return "pedido/detalhe";
    }
}
