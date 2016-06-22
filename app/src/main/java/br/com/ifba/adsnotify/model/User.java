package br.com.ifba.adsnotify.model;

import java.io.Serializable;

/**
 * Created by Robson on 10/05/2016.
 */
public class User implements Serializable{
    private String id;
    private String email;
    private String senha;
    private String nome;

    private String accountType;
    private String authTokenType;
    private String accountName;
    private String token;

    public User(){}

    public User(String id,String nome, String email ){
        this.id = id;
        this.nome = nome;
        this.email =email;
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAuthTokenType() {
        return authTokenType;
    }

    public void setAuthTokenType(String authTokenType) {
        this.authTokenType = authTokenType;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
