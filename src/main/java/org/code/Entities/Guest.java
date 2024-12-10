package org.code.Entities;

import javax.persistence.*;

@Entity
@Table(name = "guests")
public class Guest extends User implements HasId {
    @Column(name = "guest_rating")
    private double guestRating;

    public Guest(int userID, String name, String email, String phone, double guestRating) {
        super(userID, name, email, phone);
        this.guestRating = guestRating;
    }

    public double getGuestRating() {
        return guestRating;
    }

    public void setGuestRating(double guestRating) {
        this.guestRating = guestRating;
    }

    @Override
    public int getId() {
        return userID;
    }

    @Override
    public void setId(int id) {
        this.userID = id;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "guestRating=" + guestRating +
                ", name='" + name + '\'' +
                ", userID=" + userID +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public Guest() {}
}