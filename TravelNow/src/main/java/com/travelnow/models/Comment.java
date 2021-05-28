package com.travelnow.models;

import java.time.LocalDate;

public class Comment {
    int id;
    String text;
    int rating;
    LocalDate postDate;
    int hotelsId;
    String authorUserName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDate getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDate date) {
        this.postDate = date;
    }

    public int getHotelsId() {
        return hotelsId;
    }

    public void setHotelsId(int hotelsId) {
        this.hotelsId = hotelsId;
    }

    public String getAuthorUserName() {
        return authorUserName;
    }

    public void setAuthorUserName(String authorUserName) {
        this.authorUserName = authorUserName;
    }
}
