package br.com.ifba.adsnotify.model;

import java.io.Serializable;

/**
 * Created by Robson on 10/05/2016.
 */
public class User implements Serializable{
    private String id;
    private String matricula;
    private String senha;
    private String nome;

    public User(String id,String nome, String matricula ){
        this.id = id;
        this.nome = nome;
        this.matricula =matricula;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
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
}
