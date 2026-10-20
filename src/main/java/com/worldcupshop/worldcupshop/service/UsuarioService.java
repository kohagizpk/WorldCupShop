package com.worldcupshop.worldcupshop.service;

import com.worldcupshop.worldcupshop.model.TipoUsuario;
import com.worldcupshop.worldcupshop.model.Usuario;
import com.worldcupshop.worldcupshop.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final BCryptPasswordEncoder senha = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario encontrar(String nome) {
        return repository.findByNome(nome).orElseThrow();
    }

    public Usuario autenticar(String nome, String senhaDigitada) {
        Usuario usuario = repository.findByNome(nome).orElse(null);
        if (usuario != null && senha.matches(senhaDigitada, usuario.getSenha())) {
            return usuario;
        }
        return null;
    }

    public void cadastrar(String nome, String senhaDigitada, String cep, String endereco,
                          String bairro, String cidade, String uf, String numero, String complemento) {
        if (nome == null || nome.isBlank() || senhaDigitada.length() < 6) {
            throw new IllegalArgumentException("Informe nome e senha com pelo menos 6 caracteres.");
        }
        nome = nome.trim();
        if (repository.existsByNome(nome)) {
            throw new IllegalArgumentException("Esse usuário já existe.");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setSenha(senha.encode(senhaDigitada));
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuario.setCep(cep);
        usuario.setEndereco(endereco);
        usuario.setBairro(bairro);
        usuario.setCidade(cidade);
        usuario.setUf(uf);
        usuario.setNumero(numero);
        usuario.setComplemento(complemento);
        repository.save(usuario);
    }

    public void atualizar(Usuario usuario, String nome, String novaSenha) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome obrigatório.");
        }
        nome = nome.trim();
        if (!nome.equals(usuario.getNome()) && repository.existsByNome(nome)) {
            throw new IllegalArgumentException("Nome já utilizado.");
        }
        usuario.setNome(nome);
        if (novaSenha != null && !novaSenha.isBlank()) {
            if (novaSenha.length() < 6) {
                throw new IllegalArgumentException("A senha precisa de pelo menos 6 caracteres.");
            }
            usuario.setSenha(senha.encode(novaSenha));
        }
        repository.save(usuario);
    }
}
