package com.travelnow.models.HotelManager;

import java.util.Base64;

public class AddPhotoInfo {

    int hotelId;
    byte[] photo;

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setPhoto(String photo) {
        this.photo = Base64.getDecoder().decode(photo.replaceFirst("data:image/jpeg;base64,", ""));
    }

    public byte[] getPhoto() {
        return photo;
    }
}
