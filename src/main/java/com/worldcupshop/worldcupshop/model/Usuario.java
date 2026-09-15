package com.worldcupshop.worldcupshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Usuario {

    private String nome;
    private Integer senha;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Usuario() {
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId() {
        this.id = id;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public Integer getSenha() {
        return senha;
    }
    
    private void setSenha(Integer senha) {
        this.senha = senha;
    }
}
