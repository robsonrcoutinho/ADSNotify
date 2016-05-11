package br.com.ifba.adsnotify.model;

import java.io.Serializable;

/**
 * Created by Robson on 24/04/2016.
 */
public class Mensagem implements Serializable{

    private Integer avisoId;
    private String avisoTitle;
    private String avisoBody;


    public Integer getAvisoId() {
        return avisoId;
    }

    public void setAvisoId(Integer avisoId) {
        this.avisoId = avisoId;
    }

    public String getAvisoTitle() {
        return avisoTitle;
    }

    public void setAvisoTitle(String avisoTitle) {
        this.avisoTitle = avisoTitle;
    }

    public String getAvisoBody() {
        return avisoBody;
    }

    public void setAvisoBody(String avisoBody) {
        this.avisoBody = avisoBody;
    }

}





