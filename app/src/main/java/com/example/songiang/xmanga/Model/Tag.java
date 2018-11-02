package com.example.songiang.xmanga.Model;

import java.io.Serializable;

public class Tag implements Serializable {
    String tagName, url;

    public Tag(String tagName, String url) {
        this.tagName = tagName;
        this.url = url;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTagName() {
        return tagName;
    }

    public String getUrl() {
        return url;
    }
}
