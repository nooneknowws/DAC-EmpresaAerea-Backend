package br.ufpr.dac.MSAuth.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "auth_sessions")
public class AuthSession {
	
    public AuthSession() {
		super();
	}

	public AuthSession(String id, String userId, String email, String token, String perfil, String statusFunc,
			Long createdAt) {
		super();
		this.id = id;
		this.userId = userId;
		this.email = email;
		this.token = token;
		this.perfil = perfil;
		this.statusFunc = statusFunc;
		this.createdAt = createdAt;
	}

	@Id
    private String id;
    private String userId;
    private String email;
    private String token;
    private String perfil;
    private String statusFunc;
    private Long createdAt;

    public AuthSession(String userId, String email, String token, String perfil, String statusFunc) {
        this.userId = userId;
        this.email = email;
        this.token = token;
        this.perfil = perfil;
        this.statusFunc = statusFunc;
        this.createdAt = System.currentTimeMillis();
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public String getStatusFunc() {
		return statusFunc;
	}

	public void setStatusFunc(String statusFunc) {
		this.statusFunc = statusFunc;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

}