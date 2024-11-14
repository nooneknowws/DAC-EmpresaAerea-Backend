package com.funcionarios.funcionarios.models.dto;

public class FuncionarioDTO {

  private Long id;
    private String cpf;
    private String nome;
    private String email;
    private String perfil;
    private String telefone;
    private String senha;

    public FuncionarioDTO() {}

    public FuncionarioDTO(Long id, String cpf, String nome, String email, String perfil, String telefone) {
        this.id = id;
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
        this.telefone = telefone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
