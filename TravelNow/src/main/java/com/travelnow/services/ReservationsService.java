package com.travelnow.services;
import java.util.List;
import java.lang.Exception;
import java.time.LocalDate;

import com.travelnow.models.FullReservationInfo;
import com.travelnow.models.Reservation;
import com.travelnow.models.ReservationQuery;

import org.springframework.stereotype.Service;
import com.travelnow.dao.ReservationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
@Service
public class ReservationsService {
    ReservationDao reservationDao;
    @Autowired
    public ReservationsService(@Qualifier("ReservationDaoImplementation") ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public List<FullReservationInfo> getByHotelsId(int id){
        return reservationDao.listReservationsByHotelId(id);
    }

    public Reservation putReservation(Reservation reservation) throws Exception {
        if (reservation.getDateFrom().compareTo(reservation.getDateTo()) < 0){
            if (reservationDao.checkIfValidReservation(reservation)){
                reservation = reservationDao.putReservation(reservation);
                return reservation;
            } else {
                throw new Exception("Room already booked during selected time.");
            }
        } else {
            throw new Exception("From and To dates are reversed.");
        }
    }

    public List<FullReservationInfo> getByHotelsIdInTimeFrame(ReservationQuery reservationquery) {
        return reservationDao.listReservationsByHotelIdInTimeFrame(reservationquery);
    }

    public void removereservation(int hotelsId) {
        reservationDao.removereservation(hotelsId);
    }

    public List<FullReservationInfo> getByUserId(int id, LocalDate dateFrom, LocalDate dateTo) {
        return reservationDao.listGetByUserId(id, dateFrom, dateTo);
    }
}
