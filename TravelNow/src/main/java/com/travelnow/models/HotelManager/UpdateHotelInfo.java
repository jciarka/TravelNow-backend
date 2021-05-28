package com.travelnow.models.HotelManager;

public class UpdateHotelInfo {
    private int hotelId;
    private String name;
    private int telephoneNum;
    private String description;

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTelephoneNum(int telephoneNum) {
        this.telephoneNum = telephoneNum;
    }

    public int getTelephoneNum() {
        return telephoneNum;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
