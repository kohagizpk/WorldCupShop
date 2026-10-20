package com.worldcupshop.worldcupshop.service;

import com.worldcupshop.worldcupshop.model.TipoUsuario;
import com.worldcupshop.worldcupshop.model.Usuario;
import com.worldcupshop.worldcupshop.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdministradorInicial implements CommandLineRunner {

    private final UsuarioRepository usuarios;

    public AdministradorInicial(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public void run(String... args) {
        if (!usuarios.existsByNome("admin")) {
            Usuario admin = new Usuario();
            admin.setNome("admin");
            admin.setSenha(new BCryptPasswordEncoder().encode("123456"));
            admin.setTipo(TipoUsuario.ADMIN);
            usuarios.save(admin);
        }
    }
}
