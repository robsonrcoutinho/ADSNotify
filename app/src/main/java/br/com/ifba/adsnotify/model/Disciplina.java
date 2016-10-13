package br.com.ifba.adsnotify.model;

import java.util.Comparator;

/**
 * Created by Robson on 27/05/2016.
 */
public class Disciplina implements Comparator<Disciplina> {
    private String codigo;
    private String nomeDisciplina;
    private String cargaHoraria;
    private String ementa;


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public String getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(String cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public String getEmenta() {
        return ementa;
    }

    public void setEmenta(String ementa) {
        this.ementa = ementa;
    }

    @Override
    public String toString() {
        return getNomeDisciplina();
    }

    @Override
    public int compare(Disciplina dic1, Disciplina dic2) {
        return dic1.getNomeDisciplina().compareTo(dic2.getNomeDisciplina().toString());
    }
}
