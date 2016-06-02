package br.com.ifba.adsnotify.model;

/**
 * Created by Robson on 01/06/2016.
 */
public class Documento {
    private String id;
    private String url;
    private String titulo;
    private int foto;

    public Documento(String titulo, int foto){
        this.titulo = titulo;

        this.foto = foto;

    }

    public Documento(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }
}
