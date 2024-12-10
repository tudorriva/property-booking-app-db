package org.code.Entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payments")
public class Payment implements HasId, Payable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentID;

    @Column(name = "amount")
    private double amount;

    @Column(name = "date")
    private Date date;

    @Column(name = "processed")
    private boolean processed;

    public Payment(int paymentID, double amount, Date date) {
        this.paymentID = paymentID;
        this.amount = amount;
        this.date = date;
        this.processed = false;
    }

    @Override
    public void processPayment() {
        this.processed = true;
    }

    public boolean isProcessed() {
        return processed;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int getId() {
        return paymentID;
    }

    @Override
    public void setId(int id) {
        this.paymentID = id;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentID=" + paymentID +
                ", amount=" + amount +
                ", date=" + date +
                ", processed=" + processed +
                '}';
    }
    public Payment() {}
}