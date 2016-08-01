package br.com.ifba.adsnotify.model;

/**
 * Created by Robson on 29/07/2016.
 */
public class Resposta {
    private int idPerguntaRespondida;
    private int idResposta;
    private String respostaUsuário;
    private String emailUsurAvaliador;
    private int idDiscplinaAvaliada;

    public String getEmailUsurAvaliador() {
        return emailUsurAvaliador;
    }

    public void setEmailUsurAvaliador(String emailUsurAvaliador) {
        this.emailUsurAvaliador = emailUsurAvaliador;
    }

    public String getRespostaUsuário() {
        return respostaUsuário;
    }

    public void setRespostaUsuário(String respostaUsuário) {
        this.respostaUsuário = respostaUsuário;
    }

    public int getIdResposta() {
        return idResposta;
    }

    public void setIdResposta(int idResposta) {
        this.idResposta = idResposta;
    }

    public int getIdPerguntaRespondida() {
        return idPerguntaRespondida;
    }

    public void setIdPerguntaRespondida(int idPerguntaRespondida) {
        this.idPerguntaRespondida = idPerguntaRespondida;
    }

    public int getIdDiscplinaAvaliada() {
        return idDiscplinaAvaliada;
    }

    public void setIdDiscplinaAvaliada(int idDiscplinaAvaliada) {
        this.idDiscplinaAvaliada = idDiscplinaAvaliada;
    }
}
