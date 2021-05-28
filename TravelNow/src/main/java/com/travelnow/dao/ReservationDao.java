package com.travelnow.dao;
import com.travelnow.models.FullReservationInfo;
import com.travelnow.models.Reservation;
import com.travelnow.models.ReservationQuery;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDao {
    List<FullReservationInfo> listReservationsByHotelId(int id);
    boolean checkIfValidReservation(Reservation reservation);
    Reservation putReservation(Reservation reservation) throws Exception;
    List<FullReservationInfo> listReservationsByHotelIdInTimeFrame(ReservationQuery reservationquery);
	void removereservation(int hotelsId);
    List<FullReservationInfo> listGetByUserId(int id, LocalDate dateFrom, LocalDate dateTo);
}

