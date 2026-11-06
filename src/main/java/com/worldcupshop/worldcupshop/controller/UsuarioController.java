package com.worldcupshop.worldcupshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.worldcupshop.worldcupshop.model.Usuario;
import com.worldcupshop.worldcupshop.service.CepService;
import com.worldcupshop.worldcupshop.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UsuarioController {

    private final UsuarioService usuarios;
    private final CepService ceps;

    public UsuarioController(UsuarioService usuarios, CepService ceps) {
        this.usuarios = usuarios;
        this.ceps = ceps;
    }

    @GetMapping("/login")
    public String login() {
        return "usuario/login";
    }

    @PostMapping("/login")
    public String entrar(@RequestParam String username, @RequestParam String password,
                         HttpServletRequest request) {
        Usuario usuario = usuarios.autenticar(username, password);
        if (usuario == null) return "redirect:/login?error";
        HttpSession antiga = request.getSession(false);
        if (antiga != null) antiga.invalidate();
        request.getSession(true).setAttribute("usuarioId", usuario.getId());
        return "redirect:/produtos";
    }

    @PostMapping("/logout")
    public String sair(HttpSession sessao) {
        sessao.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/cadastro")
    public String cadastro(@RequestParam(required = false) String cep, Model model) {
        if (cep != null && !cep.isBlank()) {
            try {
                var dados = ceps.consultar(cep);
                model.addAttribute("cep", dados.path("cep").asString(""));
                model.addAttribute("endereco", dados.path("logradouro").asString(""));
                model.addAttribute("bairro", dados.path("bairro").asString(""));
                model.addAttribute("cidade", dados.path("localidade").asString(""));
                model.addAttribute("uf", dados.path("uf").asString(""));
            } catch (IllegalArgumentException e) {
                model.addAttribute("erro", e.getMessage());
                model.addAttribute("cep", cep);
            }
        }
        return "usuario/cadastro";
    }

    @PostMapping("/cadastro")
    public String salvar(@RequestParam String nome, @RequestParam String senha,
                         @RequestParam(defaultValue = "") String cep,
                         @RequestParam(defaultValue = "") String endereco,
                         @RequestParam(defaultValue = "") String bairro,
                         @RequestParam(defaultValue = "") String cidade,
                         @RequestParam(defaultValue = "") String uf,
                         @RequestParam(defaultValue = "") String numero,
                         @RequestParam(defaultValue = "") String complemento, Model model) {
        try {
            usuarios.cadastrar(nome, senha, cep, endereco, bairro, cidade, uf, numero, complemento);
            return "redirect:/login?cadastro";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "usuario/cadastro";
        }
    }

    @GetMapping("/perfil")
    public String perfil(@RequestAttribute("usuarioLogado") Usuario usuario, Model model) {
        model.addAttribute("usuario", usuario);
        return "usuario/perfil";
    }

    @PostMapping("/perfil")
    public String atualizar(@RequestAttribute("usuarioLogado") Usuario usuario,
                            @RequestParam String nome,
                            @RequestParam(required = false) String senha,
                            Model model, HttpSession sessao) {
        try {
            usuarios.atualizar(usuario, nome, senha);
            sessao.invalidate();
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("usuario", usuario);
            return "usuario/perfil";
        }
    }

    @GetMapping("/logout-pendente")
    public String alterado() {
        return "usuario/alterado";
    }
}
