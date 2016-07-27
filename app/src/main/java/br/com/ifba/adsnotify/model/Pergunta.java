package br.com.ifba.adsnotify.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Robson on 11/06/2016.
 */
public class Pergunta implements Serializable {
    private Long idPergunta;
    private String enunciado;
    private int tipoPergunta;
    private List<OpcaoResposta> opcaoRespostas;

    public Long getIdPergunta() {
        return idPergunta;
    }

    public void setIdPergunta(Long idPergunta) {
        this.idPergunta = idPergunta;
    }


    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public int getTipoPergunta() {
        return tipoPergunta;
    }

    public void setTipoPergunta(int tipoPergunta) {
        this.tipoPergunta = tipoPergunta;
    }

    public List<OpcaoResposta> getOpcaoRespostas() {
        return opcaoRespostas;
    }

    public void setOpcaoRespostas(List<OpcaoResposta> opcaoRespostas) {
        this.opcaoRespostas = opcaoRespostas;
    }


}
