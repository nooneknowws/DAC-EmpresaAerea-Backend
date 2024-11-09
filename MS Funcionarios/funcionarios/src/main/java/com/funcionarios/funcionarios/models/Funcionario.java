package com.funcionarios.funcionarios.models;

import jakarta.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Entity
public class Funcionario {

  @id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 11)
  private String cpf;

  @Column(nullable = false)
  private String nome;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String senha;

  @Column(nullable = false)
  private String salt;

  @Column(nullable = false)
  private String perfil;

  @Column
  private String telefone;

  public Funcionario() {}

    public Funcionario(String cpf, String nome, String email, String senha, String perfil, String telefone) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.salt = gerarSalt();
        this.senha = hashSenha(senha, this.salt); 
        this.perfil = perfil;
        this.telefone = telefone;
    }

    private String gerarSalt() {
      byte[] salt = new byte[16];
      SecureRandom random = new SecureRandom();
      random.nextBytes(salt);
      return Base64.getEncoder().encodeToString(salt);
  }

  private String hashSenha(String senha, String salt) {
      try {
          MessageDigest md = MessageDigest.getInstance("SHA-256");
          md.update(Base64.getDecoder().decode(salt)); // Aplica o salt
          byte[] hashedPassword = md.digest(senha.getBytes());
          return Base64.getEncoder().encodeToString(hashedPassword);
      } catch (NoSuchAlgorithmException e) {
          throw new RuntimeException("Erro ao criptografar a senha", e);
      }
  }
  public boolean verificarSenha(String senhaEntrada) {
      String senhaEntradaHash = hashSenha(senhaEntrada, this.salt);
      return senhaEntradaHash.equals(this.senha);
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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
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

}
