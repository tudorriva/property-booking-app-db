package org.code.Entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "locations")
public class Location implements HasId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int locationID;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    public Location(int locationID, String city, String country) {
        this.locationID = locationID;
        this.city = city;
        this.country = country;
    }

    public Location(String city, String country) {
        this.city = city;
        this.country = country;
    }

    public void getPropertiesByLocation() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    @Override
    public int getId() {
        return locationID;
    }

    @Override
    public void setId(int id) {
        this.locationID = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(city, location.city) && Objects.equals(country, location.country);
    }

    @Override
    public String toString() {
        return "Location{" +
                "locationID=" + locationID +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    public Location() {
    }
}