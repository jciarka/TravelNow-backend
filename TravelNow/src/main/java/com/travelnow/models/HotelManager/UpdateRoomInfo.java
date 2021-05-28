package com.travelnow.models.HotelManager;

import com.travelnow.models.Room;

public class UpdateRoomInfo {

    private int hotelId;
    private Room room;

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
    
}
