package org.code.Entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Comparator;

@Entity
@Table(name = "properties")
public class Property implements HasId, Bookable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int propertyID;

    @Column(name = "host_id")
    private int hostID;

    @Column(name = "address")
    private String address;

    @Column(name = "price_per_night")
    private double pricePerNight;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ElementCollection
    @Column(name = "amenity_ids")
    private List<Integer> amenityIDs;

    @ManyToOne
    @JoinColumn(name = "cancellation_policy_id")
    private CancellationPolicy cancellationPolicy;

    @OneToMany(mappedBy = "propertyID")
    private List<Review> reviews;

    public Property(int propertyID, String address, double pricePerNight, String description, Location location,
                    List<Integer> amenityIDs, CancellationPolicy cancellationPolicy, int hostID) {
        this.propertyID = propertyID;
        this.hostID = hostID;
        this.address = address;
        this.pricePerNight = pricePerNight;
        this.description = description;
        this.location = location;
        this.amenityIDs = new ArrayList<>(amenityIDs);
        this.cancellationPolicy = cancellationPolicy;
        this.reviews = new ArrayList<>();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setHostID(int hostID) {
        this.hostID = hostID;
    }

    public List<Integer> getAmenityIDs() {
        return amenityIDs;
    }

    public void setAmenityIDs(List<Integer> amenityIDs) {
        this.amenityIDs = new ArrayList<>(amenityIDs);
    }

    public CancellationPolicy getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(CancellationPolicy cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHostID() {
        return hostID;
    }

    @Override
    public boolean checkAvailability(Date checkIn, Date checkOut) {
        return false;
    }

    @Override
    public int getId() {
        return propertyID;
    }

    @Override
    public void setId(int id) {
        this.propertyID = id;
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public void sortReviews(String criteria) {
        switch (criteria.toLowerCase()) {
            case "rating" -> reviews.sort(Comparator.comparingDouble(Review::getRating).reversed());
            case "date" -> reviews.sort(Comparator.comparing(Review::getDate).reversed());
            default -> throw new IllegalArgumentException("Invalid sorting criteria. Use 'rating' or 'date'.");
        }
    }

    public List<Review> getReviews() {
        return new ArrayList<>(reviews);
    }

    public Property() {}
}