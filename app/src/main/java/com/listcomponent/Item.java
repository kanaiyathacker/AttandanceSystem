package com.listcomponent;

/**
 * Created by kanaiyathacker on 28/10/2014.
 */
public class Item {

    private String title;
    private String description;
    private String date;


    public Item(String title, String description) {
        super();
        this.title = title;
        this.description = description;
    }

    public Item(String title, String description , String date) {
        super();
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
