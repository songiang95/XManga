package com.example.songiang.xmanga.Model;
import java.io.Serializable;
public class Manga implements Serializable {
    private String name,url,img,status,description,chapterNum;
    private String tags;


    public Manga() {
    }

    public String getChapterNum() {
        return chapterNum;
    }

    public void setChapterNum(String chapterNum) {
        this.chapterNum = chapterNum;
    }

    public Manga(String title, String url, String thumbnail, String tags) {
        this.name = title;
        this.url = url;
        this.img = thumbnail;

        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getTags()
    {
        return tags;
    }
    public void setTags(String tags)
    {
        this.tags= tags;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
