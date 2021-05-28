package com.travelnow.dao;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.travelnow.models.Address;
import com.travelnow.models.BasicHotelInfo;
import com.travelnow.models.Comment;
import com.travelnow.models.CreateHotelInfo;
import com.travelnow.models.FullHotelInfo;
import com.travelnow.models.Room;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository("OracleHotelsDao")
public class HotelDaoOracle implements HotelDao{

    RowMapper<BasicHotelInfo> hotelBasicMapper =  (rs, i) -> {
        BasicHotelInfo h = new BasicHotelInfo();
        h.setId(rs.getInt("id"));
        h.setName(rs.getString("hname"));
        h.setTelephoneNum(rs.getInt("tel"));
        h.setDescription(rs.getString("description"));
        h.setMainPhoto(rs.getInt("mphoto"));
        h.setAddress(new Address());
        h.getAddress().setId(rs.getInt("aid"));
        h.getAddress().setCity(rs.getString("city"));
        h.getAddress().setStreet(rs.getString("street"));
        h.getAddress().setNumber(rs.getInt("number"));
        h.getAddress().setPostalcode(rs.getString("POSTALCODE"));
        h.getAddress().setRegion(rs.getString("rname"));   
        h.getAddress().setCountry(rs.getString("cname"));
        h.setAvgPrice((int) rs.getFloat("avgprice")); 
        h.setNumOfRooms(rs.getInt("numofrooms"));
        h.setAvgRating((int) rs.getFloat("rating"));
        if(h.getAvgRating() == 0){
            h.setAvgRating(5);
        } 
        return h;
    };

    RowMapper<Room> roomMapper =  (rs, i) -> {
        Room r = new Room();
        r.setCapacity(rs.getInt("capacity"));
        r.setId(rs.getInt("id"));
        r.setHotelsId(rs.getInt("hotelsid"));
        r.setNumber(rs.getInt("number"));
        r.setPrice(BigDecimal.valueOf(rs.getInt("price")));
        return r;
    };

    RowMapper<Comment> commentMapper =  (rs, i) -> {
        Comment c = new Comment();
        c.setId(rs.getInt("id"));
        c.setText(rs.getString("text"));
        c.setRating(rs.getInt("rating"));
        LocalDateTime dateTime = LocalDateTime.parse(rs.getString("postdate"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        c.setPostDate(dateTime.toLocalDate());
        c.setHotelsId(rs.getInt("hotelsId"));
        c.setAuthorUserName(rs.getString("authorUserName"));
        return c;
    };

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public HotelDaoOracle(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<String> listCities() {
        final String sql = "SELECT DISTINCT city FROM addresses";

        List<String> cities = jdbcTemplate.queryForList(sql, String.class);
        return cities;
    }

    public FullHotelInfo getHotelById(int hotelsId){
        final String sqlBasicInfo = "SELECT hotels.id as id, hotels.name as hname, tel, description, main_photo_id as mphoto, addresses.id as aid, city, street, \"number\", POSTALCODE, regions.name as rname, countries.name as cname, numofrooms, avgprice, (SELECT AVG(rating) FROM comments WHERE hotels_id=hotels.id) as rating FROM hotels, addresses, regions, countries, (SELECT hotels_id, SUM(1) as numofrooms, AVG(price/capacity) as avgprice FROM rooms GROUP BY hotels_id) roominfo WHERE hotels.addresses_id = addresses.id AND addresses.regions_id = regions.id AND regions.countries_id = countries.id AND hotels.id = roominfo.hotels_id AND hotels.id=? ";
       
        // Basic info
        BasicHotelInfo basicHotelInfo = jdbcTemplate.queryForObject(sqlBasicInfo, hotelBasicMapper, hotelsId);
        FullHotelInfo fullHotelInfo = new FullHotelInfo();
        BeanUtils.copyProperties(basicHotelInfo, fullHotelInfo);

        // Rooms
        fullHotelInfo.setRooms(getRoomsByHotelsId(hotelsId));
        
        // Comments
        fullHotelInfo.setComments(getCommentsByHotelId(hotelsId));

        // Pictures Ids
        final String sqlPicturesIds = "SELECT id FROM photos WHERE hotels_id = ?";
        fullHotelInfo.setPicturesIds(jdbcTemplate.queryForList(sqlPicturesIds, int.class, hotelsId));

        return fullHotelInfo;
    }

   
    public List<BasicHotelInfo> getHotelsByCityName(String city) {

        //final String sql = "SELECT hotels.id as id, hotels.name as hname, tel, description, main_photo_id as mphoto, addresses.id as aid, city, street, \"number\", POSTALCODE, regions.name as rname, countries.name as cname, numofrooms, avgprice, (SELECT AVG(rating) FROM comments WHERE hotels_id=hotels.id) as rating FROM hotels, addresses, regions, countries, (SELECT hotels_id, SUM(1) as numofrooms, AVG(price/capacity) as avgprice FROM rooms GROUP BY hotels_id) roominfo WHERE hotels.addresses_id = addresses.id AND addresses.regions_id = regions.id AND regions.countries_id = countries.id AND hotels.id = roominfo.hotels_id AND city=? ";
        final String sql = "SELECT * FROM hotel_info WHERE city=?";

        List<BasicHotelInfo> hInfos = jdbcTemplate.query(sql, hotelBasicMapper, city); 

        return hInfos;
    }

    public BasicHotelInfo getHotelbasicById(int hotelsId) {
        //final String sqlBasicInfo = "SELECT hotels.id as id, hotels.name as hname, tel, description,addresses.id as aid, city, street, \"number\", POSTALCODE, regions.name as rname, countries.name as cname, numofrooms, avgprice, (SELECT AVG(rating) FROM comments WHERE hotels_id=hotels.id) as rating FROM hotels, addresses, regions, countries, (SELECT hotels_id, SUM(1) as numofrooms, AVG(price/capacity) as avgprice FROM rooms GROUP BY hotels_id) roominfo WHERE hotels.addresses_id = addresses.id AND addresses.regions_id = regions.id AND regions.countries_id = countries.id AND hotels.id = roominfo.hotels_id AND hotels.id=? ";

        final String sqlBasicInfo = "SELECT * FROM hotel_info WHERE id=?";
        
        BasicHotelInfo basicHotelInfo = jdbcTemplate.queryForObject(sqlBasicInfo, hotelBasicMapper, hotelsId);
        return basicHotelInfo;
    }

    public List<BasicHotelInfo> getHotelsByOwnerId(int ownerId) {

        //final String sql = "SELECT hotels.id as id, hotels.name as hname, tel, description, main_photo_id as mphoto, addresses.id as aid, city, street, \"number\", POSTALCODE, regions.name as rname, countries.name as cname, numofrooms, avgprice, (SELECT AVG(rating) FROM comments WHERE hotels_id=hotels.id) as rating FROM hotels, addresses, regions, countries, (SELECT hotels_id, SUM(1) as numofrooms, AVG(price/capacity) as avgprice FROM rooms GROUP BY hotels_id) roominfo WHERE hotels.addresses_id = addresses.id AND addresses.regions_id = regions.id AND regions.countries_id = countries.id AND hotels.id = roominfo.hotels_id AND users_id=? ";

        final String sql = "SELECT * FROM hotel_info WHERE users_id=?";

        List<BasicHotelInfo> hInfos = jdbcTemplate.query(sql, hotelBasicMapper, ownerId); 

        return hInfos;
    }   

    public List<Room> getRoomsByHotelsId(int hotelId){
        final String sql = "SELECT id, capacity, \"number\", price, hotels_id as hotelsid FROM rooms WHERE hotels_id = ? ";

        List<Room> rooms = jdbcTemplate.query(sql, roomMapper, hotelId);
        return rooms;
    }
    

    public List<Comment> getCommentsByHotelId(int hotelId){
        final String sql = "SELECT comments.id as id, text, rating, postdate, hotels_id as hotelsid, username as authorUserName, email FROM comments INNER JOIN users ON users.id = comments.users_id WHERE hotels_id = ?";

        //List<Comment> comments = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Comment.class), hotelId);
        List<Comment> comments = jdbcTemplate.query(sql, commentMapper, hotelId);

        return comments;
    }

    public List<Room> getFreeRoomsByHotelsIdAtDates(int hotelId, LocalDate dateFrom, LocalDate dateTo){     

       final String sql = "SELECT id, capacity, \"number\", price, hotels_id as hotelsid " + 
                            "FROM rooms " +
                            "WHERE hotels_id=? AND id NOT IN ( " +
                                "SELECT rooms_id " + 
                                "FROM reservations " + 
                                "WHERE (startdate <= ? and finishdate > ?) OR (startdate < ? and finishdate >= ?))";
        

        List<Room> rooms = null;
        try{
            rooms = jdbcTemplate.query(sql, roomMapper, hotelId, dateFrom, dateFrom, dateTo, dateTo);
        } catch(Exception e) {
            System.out.println(e);
        }
        return rooms; 
    }

    private int insertCountry(String countryName) {
        Number countryId;
        try {
            countryId = jdbcTemplate.queryForObject("SELECT id FROM countries WHERE name = ?", 
                                                    Integer.class, countryName);
        } catch(EmptyResultDataAccessException se) {
            countryId = null;
        }

        if(countryId == null) {
            String insertCountryQuery = "INSERT INTO countries (name, dialingcode, abbreviation) VALUES (:nm, :dc, :abbr)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            SqlParameterSource namedParameters = new MapSqlParameterSource()
                                                       .addValue("nm", countryName)
                                                       .addValue("dc", 0)
                                                       .addValue("abbr", countryName.substring(0,3));
            
            namedParameterJdbcTemplate.update(insertCountryQuery, namedParameters, keyHolder, new String[]{"id"});
            countryId = keyHolder.getKey();
        }

            // UWAGA bug w bazie nadnych oracle -> poniża metoda dostępu też powinna działać
            // ale nie zwraca klucza - używać metody powyżej
            /*
            String insertCountryQuery = "INSERT INTO countries (name, dialingcode, abbreviation) VALUES ( ?, ?, ?)";
             KeyHolder keyHolder = new GeneratedKeyHolder();           
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                    .prepareStatement(insertCountryQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, countryName);
                ps.setInt(2, 0);
                ps.setString(3, countryName.substring(0,3));
                return ps;
            }, keyHolder);

            countryId = keyHolder.getKey();
        } */

        return countryId.intValue();
    }

    private int insertRegion(String regionName, int countryId) {
        Number regionId;
        try {
            regionId = jdbcTemplate.queryForObject("SELECT id FROM regions WHERE name = ?", 
                                                    Integer.class, regionName);
        } catch(EmptyResultDataAccessException se) {
            regionId = null;
        }

        if(regionId == null){
            String insertQuery = "INSERT INTO regions (name, COUNTRIES_ID) VALUES (:nm, :cid )";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            
            SqlParameterSource namedParameters = new MapSqlParameterSource()
                                                       .addValue("nm", regionName)
                                                       .addValue("cid", countryId);
            
            namedParameterJdbcTemplate.update(insertQuery, namedParameters, keyHolder, new String[]{"id"});
            regionId = keyHolder.getKey();
        }
        return regionId.intValue();
    }

    public int insertPhoto(int hotelId, byte[] bytearray) {
        String insertHotelQuery = "INSERT INTO photos (HOTELS_ID) " +
                             "VALUES (:hid)";

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("hid", hotelId);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(insertHotelQuery, namedParameters, keyHolder, new String[]{"id"});
        
        int photoId = keyHolder.getKey().intValue();


        Path pathtoDir = Paths.get(getClass().getClassLoader().getResource("static/images/").getPath());
        Path imageFilePath = Paths.get(pathtoDir.toAbsolutePath() + "/" + String.valueOf(photoId)+".jpg");
        File imageFile = new File(imageFilePath.toUri());

        try {
            boolean result = imageFile.createNewFile();
            System.out.println(result);
        } catch(IOException e) {
            return -1;
        }
        
        try(FileOutputStream outputStream = new FileOutputStream(imageFile)) {
            outputStream.write(bytearray);
            outputStream.close();
        } catch(IOException e) {
            return -1;
        }

        return photoId;
    }

    // insertRoom
    public int insertRoom(Room room) {
        String insertQuery = "INSERT INTO rooms (capacity, \"number\", price, hotels_id) " +
                             "VALUES (:cap, :num, :pri, :hid)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                                                .addValue("cap", room.getCapacity())
                                                .addValue("num", room.getNumber())
                                                .addValue("pri", room.getPrice())
                                                .addValue("hid", room.getHotelsId());
        

        namedParameterJdbcTemplate.update(insertQuery, namedParameters, keyHolder, new String[]{"id"});
        return keyHolder.getKey().intValue();
       
    }

    @Transactional // avoid partial initiation
    public CreateHotelInfo insertHotel(CreateHotelInfo hotelInfo, int ownerId) {
        // insert address
        int addressId = insertAddress(hotelInfo.getAddress());

        // insert hotel data
        String insertHotelQuery = "INSERT INTO hotels (name, tel, description, addresses_id, users_id) " +
                             "VALUES (:nm, :tel, :desc, :addId, :usrId)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                                    .addValue("nm", hotelInfo.getName())
                                    .addValue("tel", hotelInfo.getTelephoneNum())
                                    .addValue("desc", hotelInfo.getDescription())
                                    .addValue("addId", addressId)
                                    .addValue("usrId", ownerId);

        namedParameterJdbcTemplate.update(insertHotelQuery, namedParameters, keyHolder, new String[]{"id"});
        hotelInfo.setId(keyHolder.getKey().intValue());

        // insert photos and if main photo selected resolve its db id
        Integer mainPhotoId = null;

        if (hotelInfo.getPhotos() != null) {
            for(int i = 0; i < hotelInfo.getPhotos().size(); i++) {
                int phid = insertPhoto(hotelInfo.getId(), hotelInfo.getPhotos().get(i));
                
                if(hotelInfo.getMainIndex() != null && 
                hotelInfo.getMainIndex().equals(i) &&
                phid >= 0) {
                    mainPhotoId = Integer.valueOf(phid);
                }
            }
        }

        // set hotels main photo
        if(mainPhotoId != null) {
            String setMainPhotoQuery = "UPDATE hotels " +
            "SET main_photo_id = :phId " +
            "WHERE id = :hid ";

            KeyHolder setPhotoKeyHolder = new GeneratedKeyHolder();
        
            SqlParameterSource setPhotoNamedParameters = new MapSqlParameterSource()
                .addValue("phId", mainPhotoId)
                .addValue("hid", hotelInfo.getId());

            namedParameterJdbcTemplate.update(setMainPhotoQuery, setPhotoNamedParameters, setPhotoKeyHolder, new String[]{"id"});
            hotelInfo.setMainPhoto(keyHolder.getKey().intValue());
        }

        // insert rooms
        if (hotelInfo.getRooms() != null) {
            for(var room : hotelInfo.getRooms()) {
                room.setHotelsId(hotelInfo.getId());
                int roomId = insertRoom(room);
                room.setId(roomId);
            }
        }
        
        return hotelInfo;
    }

    public int insertAddress(Address hotelAddress) {
        int countryId = insertCountry(hotelAddress.getCountry());
        int regionId = insertRegion(hotelAddress.getRegion(), countryId);

        String insertQuery = "INSERT INTO addresses (street, \"number\", city, postalcode, regions_id) " +
                             "VALUES (:street, :num, :city, :pcode, :regid)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                                                   .addValue("street", hotelAddress.getStreet())
                                                   .addValue("num", hotelAddress.getNumber())
                                                   .addValue("city", hotelAddress.getCity())
                                                   .addValue("pcode", hotelAddress.getPostalcode())
                                                   .addValue("regid", regionId);
        
        namedParameterJdbcTemplate.update(insertQuery, namedParameters, keyHolder, new String[]{"id"});
        return keyHolder.getKey().intValue();
    }

    @Transactional
    public void deletePhoto(int photoId) {
        String deleteQuery = "DELETE FROM photos WHERE id = ?";
        jdbcTemplate.update(deleteQuery, photoId);
    }


    public void setMainPhoto(int photoId, int hotelsId) {
        String sql = "UPDATE hotels SET MAIN_PHOTO_ID = :pid WHERE id = :hid";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("pid", photoId);
        params.addValue("hid", hotelsId);

        namedParameterJdbcTemplate.update(sql, params);  
    }

    @Override
    public void updateRoom(int hotelId, Room room) {
        String sql = "UPDATE rooms " +
                     "SET \"number\" = :num, CAPACITY = :cap, PRICE = :pri " +
                     "WHERE id = :rid";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("rid", room.getId());
        params.addValue("num", room.getNumber());
        params.addValue("cap", room.getCapacity());
        params.addValue("pri", room.getPrice());

        namedParameterJdbcTemplate.update(sql, params); 
    }

    @Override
    public void deleteRoom(int hotelId, int roomId) {
        String deleteQuery = "DELETE FROM rooms WHERE id = ?";
        jdbcTemplate.update(deleteQuery, roomId);
    }

    @Override
    public void updateAddress(int hotelId, Address address) {

        int countryId = insertCountry(address.getCountry());
        int regionId = insertRegion(address.getRegion(), countryId);

        String sql = "UPDATE ADDRESSES " +
                     "SET STREET = :str, \"number\" = :num, CITY = :city, POSTALCODE = :pstc, REGIONS_ID = :rid " +
                     "WHERE id = :aid";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("str", address.getStreet());
        params.addValue("num", address.getNumber());
        params.addValue("city", address.getCity());
        params.addValue("pstc", address.getPostalcode());
        params.addValue("rid", regionId);
        params.addValue("aid", address.getId());

        namedParameterJdbcTemplate.update(sql, params);  
    }

    @Override
    public void updateHotelName(int hotelId, String name) {
        String sql = "UPDATE hotels SET name = :name WHERE id = :hid";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name);
        params.addValue("hid", hotelId);

        namedParameterJdbcTemplate.update(sql, params); 
    }

    @Override
    public void updateHotelDescription(int hotelId, String description) {
        String sql = "UPDATE hotels SET DESCRIPTION = :desc WHERE id = :hid";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("desc", description);
        params.addValue("hid", hotelId);

        namedParameterJdbcTemplate.update(sql, params); 
    }

    @Override
    public void updateTelNumber(int hotelId, int telNumber) {
        String sql = "UPDATE hotels SET TEL = :tel WHERE id = :hid";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("tel", telNumber);
        params.addValue("hid", hotelId);

        namedParameterJdbcTemplate.update(sql, params); 
    }
}

