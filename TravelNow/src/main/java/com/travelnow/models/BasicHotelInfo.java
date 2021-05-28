package com.travelnow.models;

public class BasicHotelInfo {
    private int id;
    private String name;
    private int telephoneNum;
    private String description;
    private Address address;
    private Integer avgPrice;
    private Integer numOfRooms;
    private Integer avgRating;
    private Integer mainPhoto;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getTelephoneNum() {
        return telephoneNum;
    }

    public void setTelephoneNum(int telephoneNum) {
        this.telephoneNum = telephoneNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(int avgPrice) {
        this.avgPrice = avgPrice;
    }

    public Integer getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(int avgRating) {
        this.avgRating = avgRating;
    }

    public Integer getNumOfRooms() {
        return numOfRooms;
    }

    public void setNumOfRooms(int numOfRooms) {
        this.numOfRooms = numOfRooms;
    }

    public Integer getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(Integer mainPhoto) {
        this.mainPhoto = mainPhoto;
    }
}
