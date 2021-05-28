package com.travelnow.controllers;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import com.travelnow.core.security.models.MyUserDetails;
import com.travelnow.models.FullReservationInfo;
import com.travelnow.models.Reservation;
import com.travelnow.models.ReservationQuery;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.travelnow.services.ReservationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
public class ReservationsApiControler {
    
    ReservationsService reservationsservice;

    @Autowired
    public ReservationsApiControler(ReservationsService reservationsservice) {
        super();
        this.reservationsservice = reservationsservice;
    }

    @PostMapping("api/reservations")
    @RolesAllowed("ROLE_USER")
    public List<FullReservationInfo> reservations(@RequestBody ReservationQuery reservationquery) {
        MyUserDetails user = getAutheticatedUser();
        List<FullReservationInfo> reservations = reservationsservice.getByUserId(user.getId(), reservationquery.getDateFrom(), reservationquery.getDateTo());    
        return reservations;    
    }


    @PostMapping("api/reservations/{id}")
    @RolesAllowed("ROLE_OWNER")
    public List<FullReservationInfo> reservations(@PathVariable("id") int id) {
        List<FullReservationInfo> reservations = reservationsservice.getByHotelsId(id);

        return reservations;         
    }

    @DeleteMapping("api/reservations/{id}")
    @RolesAllowed({"ROLE_USER", "ROLE_OWNER"})

    
    public void removereservation(@PathVariable("id") int id) {
        reservationsservice.removereservation(id);     
    }


    @PostMapping("api/reservations/temp")
    @RolesAllowed("ROLE_OWNER")

    public List<FullReservationInfo> reservationsInTimeFrame(@RequestBody ReservationQuery reservationquery) {
        List<FullReservationInfo> reservations = reservationsservice.getByHotelsIdInTimeFrame(reservationquery);
        return reservations;         
    }

    @PutMapping("api/reservations/add")
    @RolesAllowed("ROLE_USER")
    public Reservation putReservation(@RequestBody Reservation reservation) throws Exception {

        MyUserDetails user = getAutheticatedUser();
        reservation.setUserId(user.getId());

        reservation = reservationsservice.putReservation(reservation);

        return reservation;
    }

    @PutMapping("api/reservation")
    @RolesAllowed("ROLE_USER")
    public Reservation test(@RequestBody Reservation reservation) {
        return reservation;
    }

    public MyUserDetails getAutheticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (MyUserDetails) auth.getPrincipal();
    }
}
