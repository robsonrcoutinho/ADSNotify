package br.com.ifba.adsnotify.model;

/**
 * Created by Robson on 24/04/2016.
 */
public class ItemObjectMenu {

    private String name;
    private int photo;

    public ItemObjectMenu(String name, int photo) {
        this.name = name;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
