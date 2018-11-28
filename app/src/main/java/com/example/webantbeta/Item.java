package com.example.webantbeta;

import java.util.ArrayList;

public class Item {
    public static final String mUrl = "http://gallery.dev.webant.ru/media/";

    private String name;
    private String url;
    private String description;

    public Item(String name, String url, String description) {
        this.name = name;
        this.url = url;
        this.description = description;
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
}
