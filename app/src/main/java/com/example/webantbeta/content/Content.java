package com.example.webantbeta.content;

public class Content {
    public static final String mUrl = "http://gallery.dev.webant.ru/media/";
    public static int countOfPages;

    private String name;
    private String url;
    private String typeNew;
    private String typePopular;
    private String description;

    public Content(String name, String url, String description,String typeNew,String typePopular) {
        this.name = name;
        this.url = url;
        this.description = description;
        this.typeNew = typeNew;
        this.typePopular = typePopular;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeNew() {
        return typeNew;
    }

    public void setTypeNew(String typeNew) {
        this.typeNew = typeNew;
    }

    public String getTypePopular() {
        return typePopular;
    }

    public void setTypePopular(String typePopular) {
        this.typePopular = typePopular;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
