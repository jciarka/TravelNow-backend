package com.travelnow.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import com.travelnow.core.security.models.MyUserDetails;
import com.travelnow.models.BasicHotelInfo;
import com.travelnow.models.CreateHotelInfo;
import com.travelnow.models.FilterInfo;
import com.travelnow.models.FullHotelInfo;
import com.travelnow.models.Room;
import com.travelnow.models.HotelManager.AddPhotoInfo;
import com.travelnow.models.HotelManager.AddRoomsInfo;
import com.travelnow.models.HotelManager.DeleteRoomInfo;
import com.travelnow.models.HotelManager.SetDelPhotoInfo;
import com.travelnow.models.HotelManager.UpdateAddressInfo;
import com.travelnow.models.HotelManager.UpdateHotelInfo;
import com.travelnow.models.HotelManager.UpdateRoomInfo;
import com.travelnow.services.HotelsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HotelsApiController {

    HotelsService hotelsService;

    @Autowired
    public HotelsApiController(HotelsService hotelsService) {
        super();
        this.hotelsService = hotelsService;
    }

    @GetMapping("/api/cities")
    public List<String> cities() {
        List<String> cities = hotelsService.listCities();      
        return cities;
    }

    @GetMapping("/api/hotels/{city}")
    public List<BasicHotelInfo> listByCity(@PathVariable("city") String city) {
        List<BasicHotelInfo> hotels = hotelsService.getHotelsByCityName(city);
        return hotels;
    }

    @PostMapping("/api/hotels")
    public List<BasicHotelInfo> listByFilterInfo(@RequestBody FilterInfo filterInfo) {
        List<BasicHotelInfo> hotels = hotelsService.getHotelsByFilterInfo(filterInfo);
        return hotels;
    }

    @GetMapping("/api/hotelDetails/{id}")
    public FullHotelInfo hotelDetails(@PathVariable("id") int id) {
        return hotelsService.getHotelById(id);        
    }

    @GetMapping("/api/hotelDetails/freeRooms/{id}")
    public List<Room> hotelFreeRooms(@PathVariable("id") int id, 
                                        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom, 
                                        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo,
                                        int capacityFrom, int capacityTo) {
        List<Room> freeRooms = hotelsService.getFreeRoomsByHotelsIdAtDates(id, dateFrom, dateTo, capacityFrom, capacityTo); 
        return freeRooms;         
    }
    
    @GetMapping("/api/ownersHotels/{id}")
    @RolesAllowed("ROLE_OWNER")
    public List<BasicHotelInfo> getOwnersHotels() {
        MyUserDetails user = getAutheticatedUser();

        List<BasicHotelInfo> ownerHotels = hotelsService.getHotelsByOwnerId(user.getId());
        return ownerHotels;
    }

    public MyUserDetails getAutheticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (MyUserDetails) auth.getPrincipal();
    }

    @GetMapping(
        value = "/hotelsImage/{id}",
        produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getImageWithMediaType(@PathVariable("id") int imageId) throws IOException, URISyntaxException {
       String filePath = "static/images/" + String.valueOf(imageId)+".jpg"; 
       URL res = getClass().getClassLoader().getResource(filePath);
       File imageFile = new File(res.toURI());
       byte[] imageArray = Files.readAllBytes(imageFile.toPath());
       return imageArray;
        
       /* 
        InputStream imageStream = getClass()
            //.getResourceAsStream("/../resources/static/images/hilton.jpg");
            //.getResourceAsStream("/com/travelnow/controllers/hilton.jpg");
        return imageStream.readAllBytes();
        */
    }

    @PutMapping("api/addHotel")
    @RolesAllowed("ROLE_OWNER")
    public @ResponseBody BasicHotelInfo insertNewHotel(@RequestBody CreateHotelInfo hotel) throws Exception {
        hotelsService.insertHotel(hotel, getAutheticatedUser().getId());
        return hotel;
    }

    @PutMapping("api/hotels/newPhoto")
    @RolesAllowed("ROLE_OWNER")
    public @ResponseBody HashMap<String, String> insertPhoto(@RequestBody AddPhotoInfo photoInfo) throws Exception {
        MyUserDetails user = getAutheticatedUser();
        int photoId = hotelsService.insertPhoto(photoInfo.getHotelId(), photoInfo.getPhoto(), user.getId());
        HashMap<String, String> response = new HashMap<>();
        response.put("hotelId", Integer.toString(photoInfo.getHotelId()));
        response.put("photoId", Integer.toString(photoId));
        response.put("message", "Photo added");
        return response;
    }

    @DeleteMapping("api/hotels/deletePhoto")
    @RolesAllowed("ROLE_OWNER")
    public @ResponseBody HashMap<String, String> deletePhoto(@RequestBody SetDelPhotoInfo photoInfo) throws Exception {
        MyUserDetails user = getAutheticatedUser();
        hotelsService.deletePhoto(photoInfo.getHotelId(), photoInfo.getPhotoId(), user.getId());
        HashMap<String, String> response = new HashMap<>();
        response.put("hotelId", Integer.toString(photoInfo.getHotelId()));
        response.put("message", "Photo deleted");
        return response;
    }

    @PutMapping("api/hotels/setMainPhoto")
    @RolesAllowed("ROLE_OWNER")
    public @ResponseBody HashMap<String, String> setMainPhoto(@RequestBody SetDelPhotoInfo photoInfo) throws Exception {
        MyUserDetails user = getAutheticatedUser();
        hotelsService.setMainPhoto(photoInfo.getHotelId(), photoInfo.getPhotoId(), user.getId());
        HashMap<String, String> response = new HashMap<>();
        response.put("hotelId", Integer.toString(photoInfo.getPhotoId()));
        response.put("message", "Main photo set");
        return response;
    }

    @PutMapping("api/hotels/newRooms")
    @RolesAllowed("ROLE_OWNER")
    public @ResponseBody HashMap<String, String> addNewRooms(@RequestBody AddRoomsInfo roomsInfo) throws Exception {
        MyUserDetails user = getAutheticatedUser();
        hotelsService.addRooms(roomsInfo.getHotelId(), roomsInfo.getRooms(), user.getId());
        HashMap<String, String> response = new HashMap<>();
        response.put("hotelId", Integer.toString(roomsInfo.getHotelId()));
        response.put("message", "Rooms added");
        return response;
    }

    @PutMapping("api/hotels/updateRoom")
    @RolesAllowed("ROLE_OWNER")
    public @ResponseBody HashMap<String, String> updateRoom(@RequestBody UpdateRoomInfo roomsInfo) throws Exception {
        MyUserDetails user = getAutheticatedUser();
        hotelsService.updateRoom(roomsInfo.getHotelId(), roomsInfo.getRoom(), user.getId());
        HashMap<String, String> response = new HashMap<>();
        response.put("hotelId", Integer.toString(roomsInfo.getHotelId()));
        response.put("message", "Rooms updated");
        return response;
    }

    @DeleteMapping("api/hotels/deleteRoom")
    @RolesAllowed("ROLE_OWNER")
    public @ResponseBody HashMap<String, String> deleteRoom(@RequestBody DeleteRoomInfo roomsInfo) throws Exception {
        MyUserDetails user = getAutheticatedUser();
        hotelsService.deleteRoom(roomsInfo.getHotelId(), roomsInfo.getRoomId(), user.getId());
        HashMap<String, String> response = new HashMap<>();
        response.put("hotelId", Integer.toString(roomsInfo.getHotelId()));
        response.put("message", "Rooms deleted");
        return response;
    }

    @PutMapping("api/hotels/updateAddress")
    @RolesAllowed("ROLE_OWNER")
    public @ResponseBody HashMap<String, String> updateAddress(@RequestBody UpdateAddressInfo addressInfo) throws Exception {
        MyUserDetails user = getAutheticatedUser();
        hotelsService.updateAddress(addressInfo.getHotelId(), addressInfo.getAddress(), user.getId());
        HashMap<String, String> response = new HashMap<>();
        response.put("hotelId", Integer.toString(addressInfo.getHotelId()));
        response.put("message", "address modified");
        return response;
    }

    @PutMapping("api/hotels/updateHotelInfo")
    @RolesAllowed("ROLE_OWNER")
    public @ResponseBody HashMap<String, String> updateHotelInfo(@RequestBody UpdateHotelInfo hotelInfo) throws Exception {
        MyUserDetails user = getAutheticatedUser();

        hotelsService.updateBasicHotelInfo(hotelInfo.getHotelId(), hotelInfo.getName(),
                                    hotelInfo.getDescription(), hotelInfo.getTelephoneNum(),
                                    user.getId());

        HashMap<String, String> response = new HashMap<>();
        response.put("hotelId", Integer.toString(hotelInfo.getHotelId()));
        response.put("message", "hotel info modified");
        return response;
    }


}
