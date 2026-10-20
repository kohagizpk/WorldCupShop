package com.worldcupshop.worldcupshop.controller;

import com.worldcupshop.worldcupshop.model.*;
import com.worldcupshop.worldcupshop.repository.PedidoRepository;
import com.worldcupshop.worldcupshop.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProdutoService produtos;
    private final CategoriaService categorias;
    private final SelecaoService selecoes;
    private final PedidoRepository pedidos;
    private final ImagemService imagens;

    public AdminController(ProdutoService produtos, CategoriaService categorias,
                           SelecaoService selecoes, PedidoRepository pedidos, ImagemService imagens) {
        this.produtos = produtos;
        this.categorias = categorias;
        this.selecoes = selecoes;
        this.pedidos = pedidos;
        this.imagens = imagens;
    }

    @GetMapping
    public String inicio(Model model) {
        model.addAttribute("pedidos", pedidos.findAll());
        return "admin/inicio";
    }

    @GetMapping("/produtos")
    public String listarProdutos(Model model) {
        model.addAttribute("produtos", produtos.listar(null, null, null));
        return "admin/produtos";
    }

    private void listarOpcoes(Model model) {
        model.addAttribute("categorias", categorias.listarTodas());
        model.addAttribute("selecoes", selecoes.listarTodas());
    }

    @GetMapping("/produtos/criar")
    public String criarProduto(Model model) {
        model.addAttribute("produto", new Produto());
        listarOpcoes(model);
        return "admin/produto-form";
    }

    @GetMapping("/produtos/editar/{id}")
    public String editarProduto(@PathVariable Long id, Model model) {
        model.addAttribute("produto", produtos.buscar(id));
        listarOpcoes(model);
        return "admin/produto-form";
    }

    @PostMapping("/produtos/salvar")
    public String salvarProduto(@ModelAttribute Produto produto, @RequestParam Long categoriaId,
                                @RequestParam Long selecaoId,
                                @RequestParam(required = false) MultipartFile imagemArquivo,
                                Model model) {
        try {
            produto.setCategoria(categorias.buscarPorId(categoriaId));
            produto.setSelecao(selecoes.buscarPorId(selecaoId));
            if (produto.getId() != null) {
                Produto antigo = produtos.buscar(produto.getId());
                produto.setImagem(antigo.getImagem());
            } else {
                produto.setImagem(null);
            }
            if (imagemArquivo != null && !imagemArquivo.isEmpty()) {
                produto.setImagem(imagens.salvar(imagemArquivo));
            }
            produtos.salvar(produto);
            return "redirect:/admin/produtos";
        } catch (Exception e) {
            model.addAttribute("produto", produto);
            model.addAttribute("erro", e.getMessage());
            listarOpcoes(model);
            return "admin/produto-form";
        }
    }

    @PostMapping("/produtos/excluir/{id}")
    public String excluirProduto(@PathVariable Long id, Model model) {
        try {
            produtos.excluir(id);
            return "redirect:/admin/produtos";
        } catch (Exception e) {
            model.addAttribute("erro", "Não foi possível excluir: " + e.getMessage());
            return "erro";
        }
    }

    @GetMapping("/categorias")
    public String listarCategorias(Model model) {
        model.addAttribute("categorias", categorias.listarTodas());
        return "admin/categorias";
    }

    @GetMapping("/categorias/criar")
    public String criarCategoria(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "admin/categoria-form";
    }

    @GetMapping("/categorias/editar/{id}")
    public String editarCategoria(@PathVariable Long id, Model model) {
        model.addAttribute("categoria", categorias.buscarPorId(id));
        return "admin/categoria-form";
    }

    @PostMapping("/categorias/salvar")
    public String salvarCategoria(@ModelAttribute Categoria categoria) {
        categorias.salvar(categoria);
        return "redirect:/admin/categorias";
    }

    @PostMapping("/categorias/excluir/{id}")
    public String excluirCategoria(@PathVariable Long id, Model model) {
        try {
            categorias.deletar(id);
            return "redirect:/admin/categorias";
        } catch (Exception e) {
            model.addAttribute("erro", "Categoria está em uso e não pode ser excluída.");
            return "erro";
        }
    }

    @GetMapping("/selecoes")
    public String listarSelecoes(Model model) {
        model.addAttribute("selecoes", selecoes.listarTodas());
        return "admin/selecoes";
    }

    @GetMapping("/selecoes/criar")
    public String criarSelecao(Model model) {
        model.addAttribute("selecao", new Selecao());
        return "admin/selecao-form";
    }

    @GetMapping("/selecoes/editar/{id}")
    public String editarSelecao(@PathVariable Long id, Model model) {
        model.addAttribute("selecao", selecoes.buscarPorId(id));
        return "admin/selecao-form";
    }

    @PostMapping("/selecoes/salvar")
    public String salvarSelecao(@ModelAttribute Selecao selecao) {
        selecoes.salvar(selecao);
        return "redirect:/admin/selecoes";
    }
}
