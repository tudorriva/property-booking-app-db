package org.code.Services;

import org.code.Entities.*;
import org.code.Repository.IRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing property bookings.
 */
public class PropertyBookingService {
    private final IRepository<Host> hostRepo;
    private final IRepository<Guest> guestRepo;
    private final IRepository<Property> propertyRepo;
    private final IRepository<Booking> bookingRepo;
    private final IRepository<Review> reviewRepo;
    private final IRepository<Amenity> amenityRepo;
    private final IRepository<Location> locationRepo;
    private final IRepository<CancellationPolicy> cancellationPolicyRepo;
    private final IRepository<Payment> paymentRepo;

    public PropertyBookingService(
            IRepository<Host> hostRepo,
            IRepository<Guest> guestRepo,
            IRepository<Property> propertyRepo,
            IRepository<Booking> bookingRepo,
            IRepository<Review> reviewRepo,
            IRepository<Amenity> amenityRepo,
            IRepository<Location> locationRepo,
            IRepository<CancellationPolicy> cancellationPolicyRepo,
            IRepository<Payment> paymentRepo) {
        this.hostRepo = hostRepo;
        this.guestRepo = guestRepo;
        this.propertyRepo = propertyRepo;
        this.bookingRepo = bookingRepo;
        this.reviewRepo = reviewRepo;
        this.amenityRepo = amenityRepo;
        this.locationRepo = locationRepo;
        this.cancellationPolicyRepo = cancellationPolicyRepo;
        this.paymentRepo = paymentRepo;
    }

    /**
     * Initializes the repositories with sample data.
     */
    public void initializeRepositories() {
        hostRepo.getAll();
        guestRepo.getAll();
        propertyRepo.getAll();
        bookingRepo.getAll();
        reviewRepo.getAll();
        amenityRepo.getAll();
    }

    // -------------------- Host and Guest Management --------------------

    /**
     * Adds a new host.
     *
     * @param host the host to be added
     */
    public void addHost(Host host) {
        if (host != null)
            hostRepo.create(host);
    }

    /**
     * Adds a new guest.
     *
     * @param guest the guest to be added
     */
    public void addGuest(Guest guest) {
        if (guest != null)
            guestRepo.create(guest);
    }

    /**
     * Retrieves all hosts.
     *
     * @return a list of all hosts
     */
    public List<Host> getAllHosts() { return hostRepo.getAll(); }

    /**
     * Retrieves all guests.
     *
     * @return a list of all guests
     */
    public List<Guest> getAllGuests() { return guestRepo.getAll(); }

    /**
     * Retrieves a host by their ID.
     *
     * @param id the ID of the host
     * @return the host with the specified ID
     */
    public Host getHostById(int id) {
        return hostRepo.read(id);
    }

    /**
     * Retrieves a guest by their ID.
     *
     * @param id the ID of the guest
     * @return the guest with the specified ID
     */
    public Guest getGuestById(int id) {
        return guestRepo.read(id);
    }

    /**
     * Retrieves bookings for a specific guest.
     *
     * @param guestId the ID of the guest
     * @return a list of bookings for the guest
     */
    public List<Booking> getBookingsForGuest(int guestId) {
        return bookingRepo.getAll().stream()
                .filter(booking -> booking.getGuestID() == guestId)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves properties managed by a specific host.
     *
     * @param hostId the ID of the host
     * @return a list of properties managed by the host
     */
    public List<Property> getPropertiesForHost(int hostId) {
        return propertyRepo.getAll().stream()
                .filter(property -> property.getHostID() == hostId)
                .collect(Collectors.toList());
    }

    // -------------------- Property and Amenity Management --------------------

    /**
     * Adds a new property.
     *
     * @param property the property to be added
     */
    public void addProperty(Property property) {
        if (property != null)
            propertyRepo.create(property);
    }

    /**
     * Retrieves all properties.
     *
     * @return a list of all properties
     */
    public List<Property> getAllProperties() { return propertyRepo.getAll(); }

    /**
     * Retrieves a property by its ID.
     *
     * @param id the ID of the property
     * @return the property with the specified ID
     */
    public Property getPropertyById(int id) {
        return propertyRepo.read(id);
    }

    /**
     * Retrieves properties in a specific location.
     *
     * @param location the location to search for properties
     * @return a list of properties in the specified location
     */
    public List<Property> getPropertiesByLocation(Location location) {
        return propertyRepo.getAll().stream()
                .filter(p -> p.getLocation().equals(location))
                .collect(Collectors.toList());
    }

    /**
     * Adds an amenity to a property.
     *
     * @param property the property to which the amenity is to be added
     * @param amenity the amenity to be added
     */
    public void addAmenityToProperty(Property property, Amenity amenity) {
        if (property != null && amenity != null) {
            amenityRepo.create(amenity);
            property.getAmenityIDs().add(amenity.getAmenityID());
            propertyRepo.update(property);
        }
    }

    /**
     * Retrieves amenities for a property.
     *
     * @param property the property whose amenities are to be retrieved
     * @return a list of amenities for the property
     */
    public List<Amenity> getAmenitiesForProperty(Property property) {
        return property.getAmenityIDs().stream()
                .map(amenityRepo::read)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the property associated with a booking.
     *
     * @param booking the booking whose property is to be retrieved
     * @return the property associated with the booking
     */
    public Property getPropertyForBooking(Booking booking) {
        return propertyRepo.read(booking.getPropertyID());
    }

    /**
     * Filters properties by location.
     *
     * @param location the location to filter properties by
     * @return a list of properties in the specified location
     */
    public List<Property> filterPropertiesByLocation(Location location) {
        return propertyRepo.getAll().stream()
                .filter(property -> property.getLocation().equals(location))
                .collect(Collectors.toList());
    }

    /**
     * Deletes a property by its ID.
     *
     * @param propertyId the ID of the property to be deleted
     */
    public void deleteProperty(int propertyId) {
        Property property = propertyRepo.read(propertyId);
        if (property != null) {
            propertyRepo.delete(propertyId);
        }
    }

    // -------------------- Booking Management --------------------

    /**
     * Books a property for a guest.
     *
     * @param guest the guest booking the property
     * @param property the property to be booked
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return true if the booking is successful, false otherwise
     */
    public boolean bookProperty(Guest guest, Property property, Date checkInDate, Date checkOutDate) {
        if (checkAvailability(property.getId(), checkInDate, checkOutDate)) {
            double totalPrice = property.getPricePerNight() * getDaysBetween(checkInDate, checkOutDate);

            int paymentId = generateUniqueId();
            Payment payment = new Payment(paymentId, totalPrice, new Date());
            payment.processPayment();

            int bookingId = generateUniqueId();
            Booking booking = new Booking(bookingId, checkOutDate, checkInDate, totalPrice, guest.getId(), property.getId(), payment);

            bookingRepo.create(booking);

            return true;
        }
        return false;
    }

    /**
     * Retrieves bookings for a property.
     *
     * @param propertyId the ID of the property
     * @return a list of bookings for the property
     */
    public List<Booking> getBookingsForProperty(int propertyId) {
        return bookingRepo.getAll().stream()
                .filter(booking -> booking.getPropertyID() == propertyId)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a booking by its ID.
     *
     * @param bookingId the ID of the booking
     * @return the booking with the specified ID
     */
    public Booking getBookingById(int bookingId) {
        return bookingRepo.read(bookingId);
    }

    /**
     * Filters guests by booking count.
     *
     * @param minBookings the minimum number of bookings
     * @return a list of guests with at least the specified number of bookings
     */
    public List<Guest> filterGuestsByBookingCount(int minBookings) {
        return guestRepo.getAll().stream()
                .filter(guest -> getBookingsForGuest(guest.getId()).size() >= minBookings)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an amenity by its ID.
     *
     * @param id the ID of the amenity
     * @return the amenity with the specified ID
     */
    public Amenity getAmenityById(int id) {
        return amenityRepo.read(id);
    }

    /**
     * Retrieves all amenities.
     *
     * @return a list of all amenities
     */
    public List<Amenity> getAllAmenities() {
        return amenityRepo.getAll();
    }

    // -------------------- Review Management --------------------

    /**
     * Adds a review for a property.
     *
     * @param guest the guest leaving the review
     * @param property the property being reviewed
     * @param rating the rating given by the guest
     * @param comment the comment given by the guest
     */
    public void addReview(Guest guest, Property property, double rating, String comment) {
        int reviewId = generateUniqueId();
        Review review = new Review(reviewId, guest.getId(), property.getId(), rating, comment, new Date());
        reviewRepo.create(review);
    }

    /**
     * Retrieves reviews for a property.
     *
     * @param propertyId the ID of the property
     * @return a list of reviews for the property
     */
    public List<Review> getReviewsForProperty(int propertyId, boolean sortByRating, boolean descending) {
        List<Review> reviews = reviewRepo.getAll().stream()
                .filter(review -> review.getPropertyID() == propertyId)
                .collect(Collectors.toList());

        // If sorting is enabled, perform the sorting
        if (sortByRating) {
            reviews.sort((r1, r2) -> {
                int comparison = Double.compare(r1.getRating(), r2.getRating());
                return descending ? -comparison : comparison; // Reverse comparison if descending
            });
        }

        return reviews;
    }


    // -------------------- Utility Methods --------------------

    /**
     * Calculates the number of days between two dates.
     *
     * @param start the start date
     * @param end the end date
     * @return the number of days between the start and end dates
     */
    private long getDaysBetween(Date start, Date end) {
        return (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24);
    }

    /**
     * Generates a unique ID.
     *
     * @return a unique ID
     */
    private int generateUniqueId() {
        return (int) (Math.random() * 10000);
    }

    /**
     * Processes a payment for a booking.
     *
     * @param booking the booking for which the payment is to be processed
     */
    public void processPaymentForBooking(Booking booking) {
        Payment payment = booking.getPayment();
        if (!payment.isProcessed()) {
            payment.processPayment();
            paymentRepo.update(payment);
        }
    }

    /**
     * Retrieves payments received by a host.
     *
     * @param hostId the ID of the host
     * @return a list of payments received by the host
     */
    public List<Payment> getPaymentsForHost(int hostId) {
        return bookingRepo.getAll().stream()
                .filter(booking -> getPropertyById(booking.getPropertyID()).getHostID() == hostId)
                .map(Booking::getPayment)
                .filter(Payment::isProcessed)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves transaction history for a host.
     *
     * @param hostId the ID of the host
     * @return a list of transactions for the host
     */
    public List<Payment> getTransactionHistoryForHost(int hostId) {
        return bookingRepo.getAll().stream()
                .filter(booking -> getPropertyById(booking.getPropertyID()).getHostID() == hostId)
                .map(Booking::getPayment)
                .collect(Collectors.toList());
    }

    /**
     * Checks the availability of a property for the given dates.
     *
     * @param propertyId the ID of the property
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return true if the property is available, false otherwise
     */
    public boolean checkAvailability(int propertyId, Date checkInDate, Date checkOutDate) {
        List<Booking> bookings = bookingRepo.getAll().stream()
                .filter(booking -> booking.getPropertyID() == propertyId)
                .collect(Collectors.toList());

        for (Booking booking : bookings) {
            if (booking.getCheckInDate().before(checkOutDate) && booking.getCheckOutDate().after(checkInDate)) {
                return false; // Overlapping booking found
            }
        }
        return true; // No overlapping bookings
    }

    /**
     * Retrieves available properties for a specific date sorted by price.
     *
     * @param date the date to check availability
     * @return a list of available properties sorted by price
     */
    public List<Property> getAvailablePropertiesByDateSortedByPrice(Date date) {
        return propertyRepo.getAll().stream()
                .filter(property -> checkAvailability(property.getId(), date, date))
                .sorted(Comparator.comparingDouble(Property::getPricePerNight))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves properties sorted by total reviews in descending order.
     *
     * @return a list of properties sorted by total reviews
     */
    public List<Property> getPropertiesByTotalReviews() {
        Map<Property, Double> propertyReviewAverage = new HashMap<>();

        for (Property property : propertyRepo.getAll()) {
            List<Review> reviews = reviewRepo.getAll().stream()
                    .filter(review -> review.getPropertyID() == property.getId())
                    .collect(Collectors.toList());

            double averageRating = reviews.stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0);

            propertyReviewAverage.put(property, averageRating);
        }

        return propertyReviewAverage.entrySet().stream()
                .sorted(Map.Entry.<Property, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public void addAmenity(Amenity amenity) {
        amenityRepo.create(amenity);
    }

    public void addLocation(Location location) {
        locationRepo.create(location);
    }

    public void addCancellationPolicy(CancellationPolicy cancellationPolicy) {
        cancellationPolicyRepo.create(cancellationPolicy);
    }
}