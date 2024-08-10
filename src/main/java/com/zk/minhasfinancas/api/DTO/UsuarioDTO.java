package com.zk.minhasfinancas.api.DTO;

public class UsuarioDTO {

    private String email;
    private String name;
    private String senha;

    public UsuarioDTO(String email, String name, String senha) {
        this.email = email;
        this.name = name;
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
