package com.example.project3.Genre;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.Date;


public class VideoRecyclerItem implements Serializable {
    private int genre;
    private String videoId;
    private String title;
    private Drawable thumbnail;
    private String author;
    private String description;
    private Date createdAt;
//    private ArrayList<Comment> comment;

    public VideoRecyclerItem() {
    }

    public void setGenre(int genre){ this.genre = genre; }
    public void setVideoId(String Id) {
        this.videoId = Id;
    }
    public void setTitle(String Title){
        this.title = Title;
    }
    public void setThumbnail(Drawable thumbnail) {
        this.thumbnail = thumbnail;
    }
    public void setAuthor(String name){
        this.author = name;
    }
    public void setDescription(String desc) {
        this.description = desc;
    }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
//    public void setComment(ArrayList<Comment> comment){
//        this.comment = comment;
//    }

    public int getGenre(){ return this.genre;}
    public String getVideoId() {
        return this.videoId;
    }
    public String getTitle() {
        return this.title;
    }
    public Drawable getThumbnail() {
        return this.thumbnail;
    }
    public String getAuthor(){
        return this.author;
    }
    public String getDescription(){
        return this.description;
    }
    public Date getCreatedAt() { return this.createdAt; }
//    public ArrayList<Comment> getComment(){
//        return comment;
//    }
}

