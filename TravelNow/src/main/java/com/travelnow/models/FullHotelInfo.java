package com.travelnow.models;

import java.util.List;

public class FullHotelInfo extends BasicHotelInfo{

    private List<Room> rooms;
    private List<Integer> picturesIds;
    private List<Comment> comments;

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Integer> getPicturesIds() {
        return picturesIds;
    }

    public void setPicturesIds(List<Integer> picturesIds) {
        this.picturesIds = picturesIds;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
