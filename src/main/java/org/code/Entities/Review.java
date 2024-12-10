package org.code.Entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reviews")
public class Review implements HasId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewID;

    @Column(name = "guest_id")
    private int guestID;

    @Column(name = "property_id")
    private int propertyID;

    @Column(name = "rating")
    private double rating;

    @Column(name = "comment")
    private String comment;

    @Column(name = "date")
    private Date date;

    public Review(int reviewID, int guestID, int propertyID, double rating, String comment, Date date) {
        this.reviewID = reviewID;
        this.guestID = guestID;
        this.propertyID = propertyID;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }

    public void addRating(double newRating) {
        this.rating = newRating;
    }

    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int getId() {
        return reviewID;
    }

    @Override
    public void setId(int id) {
        this.reviewID = id;
    }

    public int getGuestID() {
        return guestID;
    }

    public void setGuestID(int guestID) {
        this.guestID = guestID;
    }

    public void setPropertyID(int propertyID) {
        this.propertyID = propertyID;
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewID=" + reviewID +
                ", guestID=" + guestID +
                ", propertyID=" + propertyID +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", date=" + date +
                '}';
    }

    public int getPropertyID() {
        return propertyID;
    }

    public Review() {}
}