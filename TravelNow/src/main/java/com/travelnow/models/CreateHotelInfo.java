package com.travelnow.models;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CreateHotelInfo extends BasicHotelInfo {
    private List<Room> rooms;
    private List<byte[]> photos;
    Integer mainIndex;
    
    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<byte[]> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = new ArrayList<byte[]>();
        for(String photoStr : photos) {
            byte[] decodedBytes = Base64.getDecoder().decode(photoStr.replaceFirst("data:image/jpeg;base64,", ""));
            this.photos.add(decodedBytes);
        }
    }

    public Integer getMainIndex() {
        return mainIndex;
    }

    public void setMainIndex(Integer mainIndex) {
        this.mainIndex = mainIndex;
    }
}
