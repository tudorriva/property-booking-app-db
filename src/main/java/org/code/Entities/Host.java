package org.code.Entities;

import javax.persistence.*;

@Entity
@Table(name = "\"hosts\"")
public class Host extends User implements HasId {
    @Column(name = "host_rating")
    private double hostRating;

    public Host() {
        // Default constructor required by Hibernate
    }

    public Host(int userID, String name, String email, String phone, double hostRating) {
        super(userID, name, email, phone);
        this.hostRating = hostRating;
    }

    public double getHostRating() {
        return hostRating;
    }

    public void setHostRating(double hostRating) {
        this.hostRating = hostRating;
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
        return "Host{" +
                "hostRating=" + hostRating +
                ", name='" + name + '\'' +
                ", userID=" + userID +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}