package com.travelnow.models;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Address {
    private int id;
    private String street;
    private int number;
    private String city;
    private String postalcode;
    private String region;
    private String country;

    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
}
