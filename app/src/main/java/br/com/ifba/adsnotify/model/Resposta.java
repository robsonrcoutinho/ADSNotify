package br.com.ifba.adsnotify.model;

/**
 * Created by Robson on 29/07/2016.
 */
public class Resposta {
    private int idPerguntaRespondida;
    private String respostaUsuário;
    private String emailUsurAvaliador;
    private int idDiscplinaAvaliada;
    private int idAvaliacao;
    private String identificador;
    private boolean flagTipoPergunta;

    public boolean isFlagTipoPergunta() {
        return flagTipoPergunta;
    }

    public void setFlagTipoPergunta(boolean flagTipoPergunta) {
        this.flagTipoPergunta = flagTipoPergunta;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public int getIdAvaliacao() {
        return idAvaliacao;
    }

    public void setIdAvaliacao(int idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Resposta)) {
            return false;
        }
        final Resposta other = (Resposta) obj;
        return this.getIdentificador().equals(other.getIdentificador());
    }
}
