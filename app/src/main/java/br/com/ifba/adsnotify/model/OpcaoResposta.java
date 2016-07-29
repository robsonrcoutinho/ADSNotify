package br.com.ifba.adsnotify.model;

import java.io.Serializable;

/**
 * Created by Robson on 12/06/2016.
 */
public class OpcaoResposta implements Serializable {
    private Long idOpcao;
    private int idPergunta;
    private String resposta;

    public Long getIdOpcao() {
        return idOpcao;
    }

    public void setIdOpcao(Long idOpcao) {
        this.idOpcao = idOpcao;
    }

    public int getIdPergunta() {
        return idPergunta;
    }

    public void setIdPergunta(int idPergunta) {
        this.idPergunta = idPergunta;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }
}
