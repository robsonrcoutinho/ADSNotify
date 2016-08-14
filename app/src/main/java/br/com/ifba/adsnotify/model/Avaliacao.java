package br.com.ifba.adsnotify.model;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Robson on 12/06/2016.
 */
public class Avaliacao implements Serializable {
    private int idAvaliacao;
    private String inicio;
    private String termino;
    private ArrayList<Questionario> questionarios;


    public int getIdAvaliacao() {
        return idAvaliacao;
    }

    public void setIdAvaliacao(int idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getTermino() {
        return termino;
    }

    public void setTermino(String termino) {
        this.termino = termino;
    }

    public ArrayList<Questionario> getQuestionarios() {
        return questionarios;
    }

    public void setQuestionarios(ArrayList<Questionario> questionarios) {
        this.questionarios = questionarios;
    }
}
