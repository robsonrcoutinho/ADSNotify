package br.com.ifba.adsnotify.model;

import java.util.Comparator;

/**
 * Created by Robson on 01/06/2016.
 */
public class Documento  implements Comparator<Documento> {
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

    @Override
    public String toString() {
        return getTitulo();
    }

    @Override
    public int compare(Documento d1, Documento d2) {
        return d1.getTitulo().compareTo(d2.getTitulo().toString());
    }
}
