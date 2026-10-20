package com.worldcupshop.worldcupshop.service;

import com.worldcupshop.worldcupshop.model.TipoUsuario;
import com.worldcupshop.worldcupshop.model.Usuario;
import com.worldcupshop.worldcupshop.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class AcessoService extends OncePerRequestFilter {

    private final UsuarioRepository usuarios;

    public AcessoService(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String rota = request.getRequestURI();
        boolean privada = rota.startsWith("/admin") || rota.startsWith("/carrinho")
                || rota.startsWith("/pedidos") || rota.equals("/perfil");

        Usuario usuario = null;
        var sessao = request.getSession(false);
        if (sessao != null && sessao.getAttribute("usuarioId") instanceof Long id) {
            usuario = usuarios.findById(id).orElse(null);
        }
        if (privada && usuario == null) {
            response.sendRedirect("/login");
            return;
        }
        if (rota.startsWith("/admin") && usuario.getTipo() != TipoUsuario.ADMIN) {
            response.sendError(403);
            return;
        }
        if (usuario != null) {
            request.setAttribute("usuarioLogado", usuario);
        }
        chain.doFilter(request, response);
    }
}
