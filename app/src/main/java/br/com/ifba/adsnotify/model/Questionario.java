package br.com.ifba.adsnotify.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robson on 12/06/2016.
 */
public class Questionario implements Serializable {
    private List<Pergunta> perguntas;

    public List<Pergunta> getPerguntas() {
        return perguntas;
    }

    public void setPerguntas(List<Pergunta> perguntas) {
        this.perguntas = perguntas;
    }
}
