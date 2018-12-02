package com.example.webantbeta.content;

public class Content {
    public static final String mUrl = "http://gallery.dev.webant.ru/media/";

    private String name;
    private String url;
    private String description;
    private int countOfPages;

    public Content(){}

    public Content(String name, String url, String description) {
        this.name = name;
        this.url = url;
        this.description = description;
    }
    public Content(String name, String url, String description,int countOfPages) {
        this.name = name;
        this.url = url;
        this.description = description;
        this.countOfPages = countOfPages;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCountOfPages() { return countOfPages; }

    public void setCountOfPages(int countOfPages) {
        this.countOfPages = countOfPages;
    }
}
