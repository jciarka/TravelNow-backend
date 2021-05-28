package com.travelnow.models;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class FilterInfo {

    private String city;

    private int priceRange;

    private int gradeFrom;

    private int gradeTo;

    private String sortedBy;

    private int capacityFrom;

    private int capacityTo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public int getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(int price) {
        this.priceRange = price;
    }


    public int getGradeFrom() {
        return gradeFrom;
    }

    public void setGradeFrom(int gradeFrom) {
        this.gradeFrom = gradeFrom;
    }

    public int getGradeTo() {
        return gradeTo;
    }

    public void setGradeTo(int gradeTo) {
        this.gradeTo = gradeTo;
    }

    public String getSortedBy() {
        return sortedBy;
    }

    public void setSortedBy(String filterBy) {
        this.sortedBy = filterBy;
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

    public int getCapacityTo() {
        return capacityTo;
    }

    public void setCapacityTo(int capacityTo) {
        this.capacityTo = capacityTo;
    }

    public int getCapacityFrom() {
        return capacityFrom;
    }

    public void setCapacityFrom(int capacityFrom) {
        this.capacityFrom = capacityFrom;
    }
}
