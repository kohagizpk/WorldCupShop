package com.worldcupshop.worldcupshop.controller;

import com.worldcupshop.worldcupshop.model.TipoUsuario;
import com.worldcupshop.worldcupshop.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class MenuControllerAdvice {

    @ModelAttribute("administrador")
    public boolean administrador(HttpServletRequest request) {
        return request.getAttribute("usuarioLogado") instanceof Usuario usuario
                && usuario.getTipo() == TipoUsuario.ADMIN;
    }
}
