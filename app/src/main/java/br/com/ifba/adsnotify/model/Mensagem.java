package br.com.ifba.adsnotify.model;

import java.io.Serializable;

/**
 * Created by Robson on 24/04/2016.
 */
public class Mensagem implements Serializable{

    private String avisoTitle;
    private String avisoBody;
    private String createAviso;


    public String getCreateAviso() {
        return createAviso;
    }

    public void setCreateAviso(String createAviso) {
        this.createAviso = createAviso;
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





