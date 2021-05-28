package com.travelnow.models.HotelManager;

import com.travelnow.models.Address;

public class UpdateAddressInfo {
    private int hotelId;
    private Address address;
    
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }
}
