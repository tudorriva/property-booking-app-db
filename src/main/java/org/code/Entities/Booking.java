package org.code.Entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bookings")
public class Booking implements HasId, Payable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingID;

    @Column(name = "check_in_date")
    private Date checkInDate;

    @Column(name = "check_out_date")
    private Date checkOutDate;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "guest_id")
    private int guestID;

    @Column(name = "property_id")
    private int propertyID;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    public Booking(int bookingID, Date checkOutDate, Date checkInDate, double totalPrice, int guestID, int propertyID, Payment payment) {
        this.bookingID = bookingID;
        this.checkOutDate = checkOutDate;
        this.checkInDate = checkInDate;
        this.totalPrice = totalPrice;
        this.guestID = guestID;
        this.propertyID = propertyID;
        this.payment = payment;
    }

    public int getGuestID() {
        return guestID;
    }

    public void setGuestID(int guestID) {
        this.guestID = guestID;
    }

    public int getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(int propertyID) {
        this.propertyID = propertyID;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public double calculateTotal() {
        return totalPrice;
    }

    public void cancelBooking() {
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public int getId() {
        return bookingID;
    }

    @Override
    public void setId(int id) {
        this.bookingID = id;
    }

    @Override
    public void processPayment() {
        // Logic to process payment *We'll do it in the future*
    }

    public int getPropertyId() {
        return propertyID;
    }

    public Booking() {}
}