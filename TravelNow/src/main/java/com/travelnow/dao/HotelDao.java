package com.travelnow.dao;

import java.time.LocalDate;
import java.util.List;

import com.travelnow.models.Address;
import com.travelnow.models.BasicHotelInfo;
import com.travelnow.models.Comment;
import com.travelnow.models.CreateHotelInfo;
import com.travelnow.models.FullHotelInfo;
import com.travelnow.models.Room;



public interface HotelDao {
    List<String> listCities();
    FullHotelInfo getHotelById(int hotelsId);
    List<BasicHotelInfo> getHotelsByCityName(String city);
    BasicHotelInfo getHotelbasicById(int hotelsId);
    List<BasicHotelInfo> getHotelsByOwnerId(int ownerId);
    List<Comment> getCommentsByHotelId(int hotelId);
    List<Room> getRoomsByHotelsId(int hotelsId);
    List<Room> getFreeRoomsByHotelsIdAtDates(int hotelId, LocalDate dateForm, LocalDate dateTo);

    int insertPhoto(int hotelId, byte[] bytearray);
    void deletePhoto(int photoId);
    void setMainPhoto(int photoId, int hotelsId);

    int insertRoom(Room room);
    void updateRoom(int hotelId, Room room);
    void deleteRoom(int hotelId, int roomId);

    void updateAddress(int hotelId, Address address);
    void updateHotelName(int hotelId, String name);
    void updateHotelDescription(int hotelId, String description);
    void updateTelNumber(int hotelId, int telNumber);

    CreateHotelInfo insertHotel(CreateHotelInfo hotelInfo, int ownerId);

}
