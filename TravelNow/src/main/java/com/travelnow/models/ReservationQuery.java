package com.travelnow.models;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class ReservationQuery {
    private Integer hotelsId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;

    public Integer getHotelsId() {
        return hotelsId;
    }
    
    public void setHotelsId(Integer hotelsId) {
        this.hotelsId = hotelsId;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }
}
