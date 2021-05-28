package com.travelnow.models;

import java.math.BigDecimal;

public class Room {
    private int id;
    private BigDecimal price;
    private int number;
    private int capacity;
    private Integer hotelsId;

    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getHotelsId() {
        return hotelsId;
    }

    public void setHotelsId(int hotelsId) {
        this.hotelsId = hotelsId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
