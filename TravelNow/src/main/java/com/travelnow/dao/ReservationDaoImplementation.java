package com.travelnow.dao;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.time.ZoneId;
import org.springframework.jdbc.core.RowMapper;
import com.travelnow.models.FullReservationInfo;
import com.travelnow.models.ReservationQuery;
import com.travelnow.models.Reservation;
import com.travelnow.models.Room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.beans.BeanUtils;
import com.travelnow.models.FullHotelInfo;

@Repository("ReservationDaoImplementation")
public class ReservationDaoImplementation implements ReservationDao{

    RowMapper<Reservation> reservationMapper =  (rs, i) -> {
        Reservation f = new Reservation();
        f.setId(rs.getInt("id"));
        Date dateFromToConvert = rs.getDate("startdate");
        LocalDate convertedDateFrom = Instant.ofEpochMilli(dateFromToConvert.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        f.setDateFrom(convertedDateFrom);

        Date dateToToConvert = rs.getDate("finishdate");
        LocalDate convertedDateTo = Instant.ofEpochMilli(dateToToConvert.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        f.setDateTo(convertedDateTo);
        f.setUserId(rs.getInt("users_id"));
        f.setRoomsId(rs.getInt("rooms_id"));
        return f;
    };

    RowMapper<Room> roomMapper =  (rs, i) -> {
        Room r = new Room();
        r.setId(rs.getInt("id"));
        r.setCapacity(rs.getInt("capacity"));
        r.setPrice(rs.getBigDecimal("price"));
        r.setHotelsId(rs.getInt("hotels_id"));
        return r;
    };

    private JdbcTemplate jdbcTemplate;
    private HotelDao hoteldao;

    @Autowired
    public ReservationDaoImplementation(JdbcTemplate jdbcTemplate, HotelDao hoteldao) {
        this.jdbcTemplate = jdbcTemplate;
        this.hoteldao = hoteldao;
    }

    @Override
    public List<FullReservationInfo> listReservationsByHotelId(int id) {
        final String sql = "select id, startdate, finishdate, users_id, rooms_id from reservations where rooms_id in (select id from rooms where hotels_id = ?)";
        List<Reservation> reservations = jdbcTemplate.query(sql, reservationMapper, id);
        List<FullReservationInfo> fullreservationinfo = new ArrayList<FullReservationInfo>();
        Iterator<Reservation> reservationsiterator = reservations.iterator();
        String sqlroom = "select id, capacity, price, hotels_id from rooms where id = ?";
        while(reservationsiterator.hasNext()) {
            Reservation reservationBase = reservationsiterator.next();
            FullReservationInfo reservation = new FullReservationInfo();
            BeanUtils.copyProperties(reservationBase, reservation);
            int roomId =reservationBase.getRoomsId();
            Room room = jdbcTemplate.queryForObject(sqlroom, roomMapper, roomId);
            reservation.setRoom(room);
            int hotelId = reservation.getRoom().getHotelsId();
            FullHotelInfo hotel = hoteldao.getHotelById(hotelId);
            reservation.setHotel(hotel);
            fullreservationinfo.add(reservation);
        }
        
        return fullreservationinfo;
    }


    @Override
    public boolean checkIfValidReservation(Reservation reservation) {
        final String sql = "select id from reservations where ((rooms_id = ?) and ((startdate < ? and finishdate > ?) or (startdate > ? and finishdate < ?) or (startdate < ? and finishdate > ?)))";
        Date dateFrom = java.util.Date.from(reservation.getDateFrom().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date dateTo = java.util.Date.from(reservation.getDateTo().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        List<Integer> reservationIds = jdbcTemplate.queryForList(sql, int.class, reservation.getRoomsId(), dateFrom, dateFrom, dateFrom, dateTo, dateTo, dateTo);
        return reservationIds.isEmpty();
    }

    @Override
    public Reservation putReservation(Reservation reservation) {
        Date dateFrom = java.util.Date.from(reservation.getDateFrom().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date dateTo = java.util.Date.from(reservation.getDateTo().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        final String sqlinsert = "insert into reservations (startdate, finishdate, users_id, rooms_id) values (?, ?, ?, ?)";
        jdbcTemplate.update(sqlinsert, dateFrom, dateTo, reservation.getUserId(), reservation.getRoomsId());
        final String sqlget = "select id from reservations where (rooms_id = ?) and (startdate = ?) and (finishdate = ?) and (users_id = ?)";
        Integer id = jdbcTemplate.queryForObject(sqlget, int.class, reservation.getRoomsId(), dateFrom, dateTo, reservation.getUserId());
        reservation.setId(id);
        return reservation;
    }

    @Override
    public List<FullReservationInfo> listReservationsByHotelIdInTimeFrame(ReservationQuery reservationquery) {
        Date dateFrom = java.util.Date.from(reservationquery.getDateFrom().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date dateTo = java.util.Date.from(reservationquery.getDateTo().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        int id = reservationquery.getHotelsId();
        final String sql = "select id, startdate, finishdate, users_id, rooms_id from reservations where (rooms_id in (select id from rooms where hotels_id = ?)) and finishdate >= ? and startdate <= ?";
        List<Reservation> reservations = jdbcTemplate.query(sql, reservationMapper, id, dateFrom, dateTo);
        List<FullReservationInfo> fullreservationinfolist = new ArrayList<FullReservationInfo>();
        Iterator<Reservation> reservationsiterator = reservations.iterator();
        String sqlroom = "select id, capacity, price, hotels_id from rooms where id = ?";
        while(reservationsiterator.hasNext()) {
            Reservation reservationBase = reservationsiterator.next();
            FullReservationInfo reservation = new FullReservationInfo();
            BeanUtils.copyProperties(reservationBase, reservation);
            int roomId =reservationBase.getRoomsId();
            Room room = jdbcTemplate.queryForObject(sqlroom, roomMapper, roomId);
            reservation.setRoom(room);
            int hotelId = reservation.getRoom().getHotelsId();
            FullHotelInfo hotel = hoteldao.getHotelById(hotelId);
            reservation.setHotel(hotel);
            fullreservationinfolist.add(reservation);
        }
        return fullreservationinfolist;
    }

    @Override
    public void removereservation(int id) {
        final String sqldelete = "delete from reservations where id = ?";
        jdbcTemplate.update(sqldelete, id);
    }

    @Override
    public List<FullReservationInfo> listGetByUserId(int id, LocalDate dateFrom, LocalDate dateTo) {
        Date dateFromFormat = java.util.Date.from(dateFrom.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date dateToFormat = java.util.Date.from(dateTo.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        final String sql = "select id, startdate, finishdate, users_id, rooms_id from reservations where users_id  = ? and finishdate >= ? and startdate <= ?";
        List<Reservation> reservations = jdbcTemplate.query(sql, reservationMapper, id, dateFromFormat, dateToFormat);
        List<FullReservationInfo> fullreservationinfolist = new ArrayList<FullReservationInfo>();
        Iterator<Reservation> reservationsiterator = reservations.iterator();
        String sqlroom = "select id, capacity, price, hotels_id from rooms where id = ?";
        while(reservationsiterator.hasNext()) {
            Reservation reservationBase = reservationsiterator.next();
            FullReservationInfo reservation = new FullReservationInfo();
            BeanUtils.copyProperties(reservationBase, reservation);
            int roomId = reservationBase.getRoomsId();
            Room room = jdbcTemplate.queryForObject(sqlroom, roomMapper, roomId);
            reservation.setRoom(room);
            int hotelId = reservation.getRoom().getHotelsId();
            FullHotelInfo hotel = hoteldao.getHotelById(hotelId);
            reservation.setHotel(hotel);
            fullreservationinfolist.add(reservation);
        }
        return fullreservationinfolist;
    }
}
