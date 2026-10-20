package com.worldcupshop.worldcupshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String nome;
    private String senha;
    private String cep;
    private String endereco;
    private String bairro;
    private String cidade;
    private String uf;
    private String numero;
    private String complemento;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;

    public Usuario() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }
    public String getCep() { return cep; }
    public void setCep(String valor) { this.cep = valor; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String valor) { this.endereco = valor; }
    public String getBairro() { return bairro; }
    public void setBairro(String valor) { this.bairro = valor; }
    public String getCidade() { return cidade; }
    public void setCidade(String valor) { this.cidade = valor; }
    public String getUf() { return uf; }
    public void setUf(String valor) { this.uf = valor; }
    public String getNumero() { return numero; }
    public void setNumero(String valor) { this.numero = valor; }
    public String getComplemento() { return complemento; }
    public void setComplemento(String valor) { this.complemento = valor; }
}