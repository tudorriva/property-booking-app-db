package org.code.Entities;

import javax.persistence.*;

@Entity
@Table(name = "amenities")
public class Amenity implements HasId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int amenityID;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    public Amenity() {
    }

    public Amenity(int amenityID, String name, String description) {
        this.amenityID = amenityID;
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getAmenityID() {
        return amenityID;
    }

    public void setAmenityID(int amenityID) {
        this.amenityID = amenityID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getId() {
        return amenityID;
    }

    @Override
    public void setId(int id) {
        this.amenityID = id;
    }

    @Override
    public String toString() {
        return "Amenity{" +
                "amenityID=" + amenityID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}