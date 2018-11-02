package com.example.songiang.xmanga.Model;

import java.io.Serializable;

public class Chapter implements Serializable{
    String name;
    String url;
    public Chapter()
    {

    }
    public Chapter(String name, String url)
    {
        this.name = name;
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
