package br.com.ifba.adsnotify.model;

import java.util.Comparator;

/**
 * Created by Robson on 27/05/2016.
 */
public class Professor implements Comparator<Professor> {
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

    @Override
    public String toString() {
        return getNome();
    }

    @Override
    public int compare(Professor p1, Professor p2) {
        return p1.getNome().compareTo(p2.getNome().toString());
    }
}
