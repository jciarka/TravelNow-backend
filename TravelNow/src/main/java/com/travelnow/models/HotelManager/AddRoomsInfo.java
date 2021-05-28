package com.travelnow.models.HotelManager;

import java.util.List;

import com.travelnow.models.Room;

public class AddRoomsInfo {
    int hotelId;
    List<Room> rooms;

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Room> getRooms() {
        return rooms;
    }
}
