package com.travelnow.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.travelnow.configuration.AppConfiguration.PriceRanges;
import com.travelnow.dao.HotelDao;
import com.travelnow.models.Address;
import com.travelnow.models.BasicHotelInfo;
import com.travelnow.models.CreateHotelInfo;
import com.travelnow.models.FilterInfo;
import com.travelnow.models.FullHotelInfo;
import com.travelnow.models.Room;
import com.travelnow.models.SortOptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class HotelsService {


    HotelDao hotelDao;

    @Autowired
    public HotelsService(@Qualifier("OracleHotelsDao") HotelDao hotelDao) {
        this.hotelDao = hotelDao;
    }

    public List<String> listCities() {
        return hotelDao.listCities();
    }
    
    public List<BasicHotelInfo> getHotelsByCityName(String city) {
        return hotelDao.getHotelsByCityName(city);
    }

    public List<BasicHotelInfo> getHotelsByFilterInfo(FilterInfo filterInfo) {
        if(filterInfo==null || 
           filterInfo.getCity() == null || 
           filterInfo.getCity().equals("")) {
               return new ArrayList<BasicHotelInfo>();
           }

        List<BasicHotelInfo> hotels = getHotelsByCityName(filterInfo.getCity());
        Stream<BasicHotelInfo> hotelsStream = hotels.stream();

        // Price filter
        switch (filterInfo.getPriceRange()) {
            case 1:
                hotels = hotelsStream
                            .filter(x -> (x.getAvgPrice() <  PriceRanges.LOW.upLimit &&
                                          x.getAvgPrice() >= PriceRanges.LOW.downLimit))
                            .collect(Collectors.toList());
                break;     
            case 2:
                hotels = hotelsStream
                            .filter(x -> (x.getAvgPrice() <  PriceRanges.MEDIUM.upLimit &&
                                          x.getAvgPrice() >= PriceRanges.MEDIUM.downLimit))
                            .collect(Collectors.toList());
                break;
            case 3:
                hotels = hotelsStream
                .filter(x -> (x.getAvgPrice() <  PriceRanges.HIGH.upLimit &&
                              x.getAvgPrice() >= PriceRanges.HIGH.downLimit))
                .collect(Collectors.toList());
                break;
            default:
                break;
        }

        // gradeFrom filter
        hotelsStream = hotels.stream();
        if (filterInfo.getGradeFrom() >= 1 &&
                filterInfo.getGradeFrom() <=5){
            hotels = hotelsStream.filter(x -> x.getAvgRating()>=filterInfo.getGradeFrom())
                .collect(Collectors.toList());
            }

        hotelsStream = hotels.stream();
        // gradeFrom filter
        if (filterInfo.getGradeTo() >= 1 &&
                filterInfo.getGradeFrom() <=5){
            hotels = hotelsStream.filter(x -> x.getAvgRating()<=filterInfo.getGradeTo())
            .collect(Collectors.toList());
            }

        if(filterInfo.getCapacityFrom() > 0)
        {
            hotelsStream = hotels.stream();
            hotels = hotelsStream.filter(x -> {
                List<Room> rooms = hotelDao.getRoomsByHotelsId(x.getId());
                Stream<Room> roomsStream = rooms.stream();
                rooms = roomsStream.filter(y -> y.getCapacity() >= filterInfo.getCapacityFrom())
                                   .collect(Collectors.toList());
                return rooms.size() > 0;
            })
            .collect(Collectors.toList());
        }

        if(filterInfo.getCapacityTo() > 0)
        {
            hotelsStream = hotels.stream();
            hotels = hotelsStream.filter(x -> {
                List<Room> rooms = hotelDao.getRoomsByHotelsId(x.getId());
                Stream<Room> roomsStream = rooms.stream();
                rooms = roomsStream.filter(y -> y.getCapacity() <= filterInfo.getCapacityTo())
                                   .collect(Collectors.toList());
                return rooms.size() > 0;
            })
            .collect(Collectors.toList());
        }

        // sort
        hotelsStream = hotels.stream();
        if (filterInfo.getSortedBy() != null) {
            if(filterInfo.getSortedBy().equals(SortOptions.GRADE_ASC.option)) {
                hotels = hotelsStream.sorted((first, second) -> {
                    return  Integer.valueOf(first.getAvgRating())
                                .compareTo(Integer.valueOf(second.getAvgRating()));
                }).collect(Collectors.toList());

            } else if (filterInfo.getSortedBy().equals(SortOptions.GRADE_DESC.option)) {
                hotels = hotelsStream.sorted((first, second) -> {
                    return  Integer.valueOf(second.getAvgRating())
                                .compareTo(Integer.valueOf(first.getAvgRating()));
                }).collect(Collectors.toList());

            } else if (filterInfo.getSortedBy().equals(SortOptions.PRICE_ASC.option)) {
                hotels = hotelsStream.sorted((first, second) -> {
                    return  Integer.valueOf(first.getAvgPrice())
                                .compareTo(Integer.valueOf(second.getAvgPrice()));
                }).collect(Collectors.toList());

            } else if(filterInfo.getSortedBy().equals(SortOptions.PRICE_DESC.option)) {
                hotels = hotelsStream.sorted((first, second) -> {
                    return  Integer.valueOf(second.getAvgPrice())
                                .compareTo(Integer.valueOf(first.getAvgPrice()));
                }).collect(Collectors.toList());
            }
        }

        return hotels;
    }
    

    public FullHotelInfo getHotelById(int hotelsId) {
        return hotelDao.getHotelById(hotelsId);
    }

    public List<BasicHotelInfo> getHotelsByOwnerId(int ownerId) {
        return hotelDao.getHotelsByOwnerId(ownerId);
    }

    public List<Room> getFreeRoomsByHotelsIdAtDates(int hotelId, LocalDate dateFrom, LocalDate dateTo) {
        if(dateTo.compareTo(dateFrom) < 0) {
            return new ArrayList<Room>();
        }
        return hotelDao.getFreeRoomsByHotelsIdAtDates(hotelId, dateFrom, dateTo);
    }

    public List<Room> getFreeRoomsByHotelsIdAtDates(int hotelId, LocalDate dateFrom, LocalDate dateTo, 
                                                    int capacityFrom, int capacityTo) {
        
        List<Room> rooms = getFreeRoomsByHotelsIdAtDates(hotelId, dateFrom, dateTo);

        if(capacityFrom > 0)
        {
            Stream<Room> roomsStream = rooms.stream();
            rooms = roomsStream.filter(x -> x.getCapacity() >= capacityFrom)
                               .collect(Collectors.toList());
        }

        if(capacityTo > 0)
        {
            Stream<Room> roomsStream = rooms.stream();
            rooms = roomsStream.filter(x -> x.getCapacity() <= capacityTo)
                               .collect(Collectors.toList());
        }

        return  rooms;
    }

    public CreateHotelInfo insertHotel(CreateHotelInfo hotelInfo, int ownerId) throws Exception {
        StringBuilder errors = new StringBuilder();
        boolean isValid = true;
        isValid &= validateHotelName(hotelInfo.getName(), errors);
        isValid &= validateDescription(hotelInfo.getDescription(), errors);
        isValid &= validateTelNumber(hotelInfo.getTelephoneNum(), errors);
        isValid &= validateAddress(hotelInfo.getAddress(), errors);
        isValid &= validateRoomsAtHotelCreate(hotelInfo.getRooms(),errors);

        if(!isValid) {
            throw new Exception(errors.toString());
        }

        return hotelDao.insertHotel(hotelInfo, ownerId);        
    }

    public int insertPhoto(int hotelId, byte[] bytearray, int userId) throws Exception {
        checkUserToHotelPrivilages(hotelId, userId);
        return hotelDao.insertPhoto(hotelId, bytearray);
    }


    public void deletePhoto(int hotelId, int photoId, int userId) throws Exception {
        checkUserToHotelPrivilages(hotelId, userId);
        hotelDao.deletePhoto(photoId);
    }

    public void setMainPhoto(int hotelId, int photoId, int userId) throws Exception {
        checkUserToHotelPrivilages(hotelId, userId);
        hotelDao.setMainPhoto(photoId, hotelId);
    }

    public void addRooms(int hotelId, List<Room> rooms, int userId) throws Exception {
        checkUserToHotelPrivilages(hotelId, userId);

        StringBuilder errors = new StringBuilder();
        boolean isValid = true;
        List<Room> existingRooms = hotelDao.getRoomsByHotelsId(hotelId);

        isValid &= validateRoomsAtHotelCreate(rooms, errors);
        
        for(Room room : rooms) {
            isValid &= validateRoomAtExistingHotel(room, existingRooms, errors);
        }

        if(!isValid) {
            throw new Exception(errors.toString());
        }
        
        for(Room room : rooms) {
            room.setHotelsId(hotelId);
            hotelDao.insertRoom(room);
        }
    }

    public void updateRoom(int hotelId, Room room, int userId) throws Exception {
        checkUserToHotelPrivilages(hotelId, userId);

        StringBuilder errors = new StringBuilder();
        List<Room> existingRooms = hotelDao.getRoomsByHotelsId(hotelId);

        boolean isValid = validateRoomAtExistingHotel(room, existingRooms, errors);

        if(!isValid) {
            throw new Exception(errors.toString());
        }

        hotelDao.updateRoom(hotelId, room);
    }

    public void deleteRoom(int hotelId, int roomId, int userId) throws Exception {
        checkUserToHotelPrivilages(hotelId, userId);
        hotelDao.deleteRoom(hotelId, roomId);
    }

    public void updateAddress(int hotelId, Address address, int userId) throws Exception {
        checkUserToHotelPrivilages(hotelId, userId); 
        
        StringBuilder errors = new StringBuilder();
        boolean isValid = validateAddress(address, errors);

        if(!isValid) {
            throw new Exception(errors.toString());
        }        
        
        hotelDao.updateAddress(hotelId, address);
    }

    public void updateBasicHotelInfo(int hotelId, String name, String description, int telNumber, int userId) throws Exception {
        checkUserToHotelPrivilages(hotelId, userId); 
        StringBuilder errors = new StringBuilder();
        boolean isValid = true;
        isValid &= validateHotelName(name, errors);
        isValid &= validateDescription(description, errors);
        isValid &= validateTelNumber(telNumber, errors);
        if(!isValid) {
            throw new Exception(errors.toString());
        }
        hotelDao.updateHotelName(hotelId, name);
        hotelDao.updateHotelDescription(hotelId, description);
        hotelDao.updateTelNumber(hotelId, telNumber);
    }
    
    private void checkUserToHotelPrivilages(int hotelId, int userId) throws Exception {
        List<BasicHotelInfo> ownersHotels = getHotelsByOwnerId(userId);

        BasicHotelInfo hotel = ownersHotels.stream()
                                           .filter(h -> h.getId() == hotelId)
                                           .findAny()
                                           .orElse(null);
        
        if(hotel == null) {
            throw new Exception("User has no privilages to this hotel");
        }
    }

    private boolean validateString(String value, StringBuilder errors, String errorToAdd) {
        if(value==null || value.isEmpty()) {
            if(!errors.toString().isEmpty()) {
                errors.append(";");
            }
            errors.append(errorToAdd);
            return false;
        }
        return true;        
    }

    private boolean validateHotelName(String hotelName, StringBuilder errors) {
        return validateString(hotelName, errors, "Hotel name can't be empty");
    }

    private boolean validateDescription(String description, StringBuilder errors) {
        return validateString(description, errors, "Description can't be empty");
    }

    private boolean validateTelNumber(Integer telNumber, StringBuilder errors) {
        if(telNumber==null || telNumber < 1e8 || telNumber >= 1e10) {
            if(!errors.toString().isEmpty()) {
                errors.append(";");
            }
            errors.append("Tel number has wrong number of digits");
            return false;
        }
        return true;
    }

    private boolean validateAddress(Address address, StringBuilder errors) {
        boolean isValid = true;
        isValid &= validateCity(address.getCity(), errors);
        isValid &= validateStreet(address.getStreet(), errors);
        isValid &= validateBuildingNum(address.getNumber(), errors);
        isValid &= validatePostalCode(address.getPostalcode(), errors);
        isValid &= validateRegion(address.getRegion(), errors);
        isValid &= validateCountry(address.getCountry(), errors);
        return isValid;
    }

    private boolean validateCity(String city, StringBuilder errors) {
        return validateString(city, errors, "City can't be empty");
    }

    private boolean validateStreet(String street, StringBuilder errors) {
        return validateString(street, errors, "Street can't be empty");
    }

    private boolean validateBuildingNum(Integer buildingNum, StringBuilder errors) {
        if(buildingNum==null || buildingNum==0 || buildingNum >= 1e5) {
            if(!errors.toString().isEmpty()) {
                errors.append(";");
            }
            errors.append("Building num cant be empty");
            return false;
        }
        return true;
    }

    private boolean validatePostalCode(String postalCode, StringBuilder errors) {
        return validateString(postalCode, errors, "Postal code can't be empty");
    }

    private boolean validateRegion(String region, StringBuilder errors) {
        return validateString(region, errors, "Region can't be empty");
    }

    private boolean validateCountry(String country, StringBuilder errors) {
        return validateString(country, errors, "Country can't be empty");
    }

    private boolean validateRoomAtExistingHotel(Room room, List<Room> existingRooms, StringBuilder errors) {
        boolean isValid = true;

        // is number unique
        boolean roomNumExist = existingRooms.stream().anyMatch(r -> (r.getNumber() == room.getNumber() && r.getId() != room.getId()));
        if(roomNumExist) {
            if(!errors.toString().isEmpty()) {
                errors.append(";");
            }
            errors.append("Room number " + room.getNumber() + " is not unique");
            isValid = false;   
        }
        return isValid;
    }

    private boolean validateRoomsAtHotelCreate(List<Room> rooms, StringBuilder errors) {
        boolean isValid = true;

        // is number unique
        Set<Integer> roomNumbers = rooms.stream()
                                        .map(room -> room.getNumber())
                                        .collect(Collectors.toSet());
        if(roomNumbers.size() < rooms.size()) {
            if(!errors.toString().isEmpty()) {
                errors.append(";");
            }
            errors.append("Room numbers are not unique");
            isValid = false;           
        }

        // is max capacity not exided
        for(var room : rooms) {
            if(room.getCapacity() >= 1e3) {
                if(!errors.toString().isEmpty()) {
                    errors.append(";");
                }
                errors.append("Room " + room.getNumber() + " capacity is to big");
                isValid = false;           
            }
        }

        return isValid;
    }
}
