package br.com.ifba.adsnotify.model;

/**
 * Created by Robson on 27/05/2016.
 */
public class Professor {
    private String matricula;
    private String nome;
    private String curriculo;

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCurriculo() {
        return curriculo;
    }

    public void setCurriculo(String curriculo) {
        this.curriculo = curriculo;
    }
}
