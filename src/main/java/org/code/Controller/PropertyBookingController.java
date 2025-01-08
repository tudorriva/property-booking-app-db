package org.code.Controller;

import org.code.Entities.*;
import org.code.Exceptions.ValidationException;
import org.code.Services.PropertyBookingService;
import org.code.Helpers.HelperFunctions;

import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;

public class PropertyBookingController {
    private final PropertyBookingService bookingService;

    public PropertyBookingController(PropertyBookingService bookingService) {
        this.bookingService = bookingService;
    }

    public List<Review> getReviewsForProperty(Property property) {
        if (property == null) {
            throw new ValidationException("Property cannot be null.");
        }
        return bookingService.getReviewsForProperty(property.getId(), true, true);
    }

    public void addHost(Host host) {
        if (host == null) {
            throw new ValidationException("Host cannot be null.");
        }
        bookingService.addHost(host);
        System.out.println("Host added successfully");
    }

    public void listAllHosts() {
        List<Host> hosts = bookingService.getAllHosts();
        if (hosts.isEmpty()) {
            System.out.println("No hosts found.");
        } else {
            hosts.forEach(host -> {
                System.out.println("Host ID: " + host.getId());
                System.out.println("Name: " + host.getName());
                System.out.println("Email: " + host.getEmail());
                System.out.println("Phone: " + host.getPhone());
                System.out.println("Host Rating: " + host.getHostRating());
                System.out.println();
            });
        }
    }

    public Host getHostById(int id) {
        if (id <= 0) {
            throw new ValidationException("Host ID must be positive.");
        }
        return bookingService.getHostById(id);
    }

    public List<Host> getAllHosts() {
        return bookingService.getAllHosts();
    }

    public List<Property> getPropertiesForHost(int hostId) {
        if (hostId <= 0) {
            throw new ValidationException("Host ID must be positive.");
        }
        return bookingService.getPropertiesForHost(hostId);
    }

    public void listPropertiesForHost(Host host) {
        if (host == null) {
            throw new ValidationException("Host cannot be null.");
        }
        List<Property> properties = bookingService.getPropertiesForHost(host.getId());
        if (properties.isEmpty()) {
            System.out.println("No properties found for this host.");
        } else {
            properties.forEach(property -> {
                System.out.println("Property ID: " + property.getId());
                System.out.println("Address: " + property.getAddress());
                System.out.println("Price per Night: " + property.getPricePerNight());
                System.out.println("Description: " + property.getDescription());
                System.out.println("Location: " + property.getLocation().getCity() + ", " + property.getLocation().getCountry());
                System.out.println("Amenities: " + property.getAmenityIDs().stream()
                        .map(id -> bookingService.getAmenityById(id).getName())
                        .collect(Collectors.joining(", ")));
                System.out.println("Cancellation Policy: " + property.getCancellationPolicy().getDescription());
                System.out.println();
            });
        }
    }

    public void showPropertiesForHost(Host host) {
        if (host == null) {
            throw new ValidationException("Host cannot be null.");
        }
        List<Property> properties = bookingService.getPropertiesForHost(host.getId());
        if (properties.isEmpty()) {
            System.out.println("No properties found for this host.");
            return;
        }

        System.out.println("Properties managed by host " + host.getName() + ":");
        for (Property property : properties) {
            System.out.println("Property ID: " + property.getId());
            System.out.println("Address: " + property.getAddress());
            System.out.println("Price per Night: " + property.getPricePerNight());
            System.out.println("Description: " + property.getDescription());
            System.out.println("Location: " + property.getLocation().getCity() + ", " + property.getLocation().getCountry());
            System.out.println("Amenities: " + property.getAmenityIDs().stream()
                    .map(id -> bookingService.getAmenityById(id).getName())
                    .collect(Collectors.joining(", ")));
            System.out.println("Cancellation Policy: " + property.getCancellationPolicy().getDescription());
            System.out.println();
        }
    }

    public void listProperty(int id, Host host, String address, double pricePerNight, String description, Location location, List<Integer> amenityIDs, CancellationPolicy cancellationPolicy) {
        if (host == null) {
            throw new ValidationException("Host cannot be null.");
        }
        if (address == null || address.isEmpty()) {
            throw new ValidationException("Address cannot be null or empty.");
        }
        if (pricePerNight <= 0) {
            throw new ValidationException("Price per night must be positive.");
        }
        if (description == null || description.isEmpty()) {
            throw new ValidationException("Description cannot be null or empty.");
        }
        if (location == null) {
            throw new ValidationException("Location cannot be null.");
        }
        if (amenityIDs == null || amenityIDs.isEmpty()) {
            throw new ValidationException("Amenity IDs cannot be null or empty.");
        }
        if (cancellationPolicy == null) {
            throw new ValidationException("Cancellation policy cannot be null.");
        }

        Property property = new Property(id, address, pricePerNight, description, location, amenityIDs, cancellationPolicy, host.getId());
        bookingService.addProperty(property);
        System.out.println("Property listed successfully.");
    }

    public CancellationPolicy getCancellationPolicyByDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new ValidationException("Description cannot be null or empty.");
        }
        return bookingService.getCancellationPolicyByDescription(description);
    }

    public void addAmenityToProperty(Host host, int propertyIndex, String name, String description) {
        if (host == null) {
            throw new ValidationException("Host cannot be null.");
        }
        if (name == null || name.isEmpty()) {
            throw new ValidationException("Amenity name cannot be null or empty.");
        }
        if (description == null || description.isEmpty()) {
            throw new ValidationException("Amenity description cannot be null or empty.");
        }

        List<Property> properties = getPropertiesForHost(host.getId());
        if (propertyIndex < 0 || propertyIndex >= properties.size()) {
            throw new ValidationException("Invalid property number.");
        }

        Property property = properties.get(propertyIndex);
        int id = HelperFunctions.randomId();
        Amenity amenity = new Amenity(id, name, description);
        bookingService.addAmenityToProperty(property, amenity);
        System.out.println("Amenity added successfully.");
    }

    public void addGuest(Guest guest) {
        if (guest == null) {
            throw new ValidationException("Guest cannot be null.");
        }
        bookingService.addGuest(guest);
        System.out.println("Guest added successfully");
    }

    public void listAllGuests() {
        List<Guest> guests = bookingService.getAllGuests();
        if (guests.isEmpty()) {
            System.out.println("No guests found.");
        } else {
            guests.forEach(guest -> {
                System.out.println("Guest ID: " + guest.getId());
                System.out.println("Name: " + guest.getName());
                System.out.println("Email: " + guest.getEmail());
                System.out.println("Phone: " + guest.getPhone());
                System.out.println("Guest Rating: " + guest.getGuestRating());
                System.out.println();
            });
        }
    }

    public Guest getGuestById(int id) {
        if (id <= 0) {
            throw new ValidationException("Guest ID must be positive.");
        }
        return bookingService.getGuestById(id);
    }

    public List<Guest> getAllGuests() {
        return bookingService.getAllGuests();
    }

    public List<Booking> getBookingsForGuest(int guestId) {
        if (guestId <= 0) {
            throw new ValidationException("Guest ID must be positive.");
        }
        return bookingService.getBookingsForGuest(guestId);
    }

    public void bookProperty(Guest guest, int propertyId, Date checkInDate, Date checkOutDate) {
        if (guest == null) {
            throw new ValidationException("Guest cannot be null.");
        }
        if (propertyId <= 0) {
            throw new ValidationException("Property ID must be positive.");
        }
        if (checkInDate == null || checkOutDate == null) {
            throw new ValidationException("Check-in and check-out dates cannot be null.");
        }
        if (checkInDate.after(checkOutDate)) {
            throw new ValidationException("Check-in date cannot be after check-out date.");
        }

        Property property = bookingService.getPropertyById(propertyId);
        if (property != null) {
            boolean success = bookingService.bookProperty(guest, property, checkInDate, checkOutDate);
            if (success) {
                System.out.println("Property booked successfully.");
            } else {
                System.out.println("Property is not available for the selected dates.");
            }
        } else {
            System.out.println("Invalid property ID.");
        }
    }

    public void viewBookings(Guest guest) {
        if (guest == null) {
            throw new ValidationException("Guest cannot be null.");
        }
        List<Booking> bookings = bookingService.getBookingsForGuest(guest.getId());
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
        } else {
            bookings.forEach(booking -> {
                System.out.println("Booking ID: " + booking.getId());
                System.out.println("Property ID: " + booking.getPropertyID());
                System.out.println("Check-in Date: " + booking.getCheckInDate());
                System.out.println("Check-out Date: " + booking.getCheckOutDate());
                System.out.println("Total Price: " + booking.getTotalPrice());
                System.out.println();
            });
        }
    }

    public void leaveReview(Guest guest, int propertyId, double rating, String comment) {
        if (guest == null) {
            throw new ValidationException("Guest cannot be null.");
        }
        if (propertyId <= 0) {
            throw new ValidationException("Property ID must be positive.");
        }
        if (rating < 0.0 || rating > 5.0) {
            throw new ValidationException("Rating must be between 0.0 and 5.0.");
        }
        if (comment == null || comment.isEmpty()) {
            throw new ValidationException("Comment cannot be null or empty.");
        }

        Property property = bookingService.getPropertyById(propertyId);
        if (property != null) {
            bookingService.addReview(guest, property, rating, comment);
            System.out.println("Review added for property: " + property.getAddress());
        } else {
            System.out.println("Invalid property ID.");
        }
    }

    public void viewAllPropertiesByLocation(Location location) {
        if (location == null || location.getCity().isEmpty() || location.getCountry().isEmpty()) {
            throw new ValidationException("Location details are invalid.");
        }
        List<Property> properties = bookingService.getPropertiesByLocation(location);
        if (properties.isEmpty()) {
            System.out.println("No properties found for location: " + location);
        } else {
            properties.forEach(property -> {
                System.out.println("Property ID: " + property.getId());
                System.out.println("Address: " + property.getAddress());
                System.out.println("Price per Night: " + property.getPricePerNight());
                System.out.println("Description: " + property.getDescription());
                System.out.println("Location: " + property.getLocation().getCity() + ", " + property.getLocation().getCountry());
                System.out.println("Amenities: " + property.getAmenityIDs().stream()
                        .map(id -> bookingService.getAmenityById(id).getName())
                        .collect(Collectors.joining(", ")));
                System.out.println("Cancellation Policy: " + property.getCancellationPolicy().getDescription());
                System.out.println();
            });
        }
    }

    public void viewAllPropertiesByDate(Date date) {
        if (date == null) {
            throw new ValidationException("Date cannot be null.");
        }
        List<Property> properties = bookingService.getAllProperties().stream()
                .filter(property -> property.checkAvailability(date, date))
                .collect(Collectors.toList());
        if (properties.isEmpty()) {
            System.out.println("No properties available on: " + date);
        } else {
            properties.forEach(property -> {
                System.out.println("Property ID: " + property.getId());
                System.out.println("Address: " + property.getAddress());
                System.out.println("Price per Night: " + property.getPricePerNight());
                System.out.println("Description: " + property.getDescription());
                System.out.println("Location: " + property.getLocation().getCity() + ", " + property.getLocation().getCountry());
                System.out.println("Amenities: " + property.getAmenityIDs().stream()
                        .map(id -> bookingService.getAmenityById(id).getName())
                        .collect(Collectors.joining(", ")));
                System.out.println("Cancellation Policy: " + property.getCancellationPolicy().getDescription());
                System.out.println();
            });
        }
    }


    public void filterGuestsByBookingCount(int minBookings) {
        if (minBookings < 0) {
            throw new ValidationException("Minimum bookings cannot be negative.");
        }
        List<Guest> guests = bookingService.filterGuestsByBookingCount(minBookings);
        if (guests.isEmpty()) {
            System.out.println("No guests found with more than " + minBookings + " bookings.");
        } else {
            guests.forEach(guest -> System.out.println(guest.getId() + ": " + guest.getName()));
        }
    }

// -------------------- Property Operations --------------------

    /**
     * Adds a new property.
     *
     * @param property the property to be added
     */
    public void addProperty(Property property) {
        if (property == null) {
            throw new ValidationException("Property cannot be null.");
        }
        bookingService.addProperty(property);
        System.out.println("Property added successfully");
    }

    /**
     * Lists all properties.
     */
    public void listAllProperties() {
        List<Property> properties = bookingService.getAllProperties();

        if (properties.isEmpty()) {
            System.out.println("No properties found.");
        } else {
            properties.forEach(property -> {
                System.out.println("Property ID: " + property.getId());
                System.out.println("Address: " + property.getAddress());
                System.out.println("Price per Night: " + property.getPricePerNight());
                System.out.println("Description: " + property.getDescription());
                System.out.println("Location: " + property.getLocation().getCity() + ", " + property.getLocation().getCountry());
                System.out.println("Amenities: " + property.getAmenityIDs().stream()
                        .map(id -> bookingService.getAmenityById(id).getName())
                        .collect(Collectors.joining(", ")));
                System.out.println("Cancellation Policy: " + property.getCancellationPolicy().getDescription());
                System.out.println();
            });
        }
    }

    /**
     * Retrieves a property by its ID.
     *
     * @param id the ID of the property
     * @return the property with the specified ID
     */
    public Property getPropertyById(int id) {
        if (id <= 0) {
            throw new ValidationException("Property ID must be positive.");
        }
        return bookingService.getPropertyById(id);
    }

    /**
     * Lists properties in a specific location.
     *
     * @param location the location to search for properties
     */
    public void listPropertiesByLocation(Location location) {
        if (location == null || location.getCity().isEmpty() || location.getCountry().isEmpty()) {
            throw new ValidationException("Location details are invalid.");
        }
        List<Property> properties = bookingService.getPropertiesByLocation(location);

        if (properties.isEmpty()) {
            System.out.println("No properties found for location: " + location);
        } else {
            properties.forEach(property -> {
                System.out.println("Property ID: " + property.getId());
                System.out.println("Address: " + property.getAddress());
                System.out.println("Price per Night: " + property.getPricePerNight());
                System.out.println("Description: " + property.getDescription());
                System.out.println("Location: " + property.getLocation().getCity() + ", " + property.getLocation().getCountry());
                System.out.println("Amenities: " + property.getAmenityIDs().stream()
                        .map(id -> bookingService.getAmenityById(id).getName())
                        .collect(Collectors.joining(", ")));
                System.out.println("Cancellation Policy: " + property.getCancellationPolicy().getDescription());
                System.out.println();
            });
        }
    }

    public void filterPropertiesByLocation(Location location) {
        if (location == null || location.getCity().isEmpty() || location.getCountry().isEmpty()) {
            throw new ValidationException("Location details are invalid.");
        }
        List<Property> properties = bookingService.filterPropertiesByLocation(location);
        if (properties.isEmpty()) {
            System.out.println("No properties found in the specified location: " + location.getCity() + ", " + location.getCountry());
        } else {
            properties.forEach(property -> {
                System.out.println("Property ID: " + property.getId());
                System.out.println("Address: " + property.getAddress());
                System.out.println("Price per Night: " + property.getPricePerNight());
                System.out.println("Description: " + property.getDescription());
                System.out.println("Location: " + property.getLocation().getCity() + ", " + property.getLocation().getCountry());
                System.out.println("Amenities: " + property.getAmenityIDs().stream()
                        .map(id -> bookingService.getAmenityById(id).getName())
                        .collect(Collectors.joining(", ")));
                System.out.println("Cancellation Policy: " + property.getCancellationPolicy().getDescription());
                System.out.println();
            });
        }
    }

    public void deleteProperty(int propertyId) {
        if (propertyId <= 0) {
            throw new ValidationException("Property ID must be positive.");
        }
        bookingService.deleteProperty(propertyId);
        System.out.println("Property deleted successfully.");
    }

// -------------------- Amenity Operations --------------------

    /**
     * Adds an amenity to a property.
     *
     * @param property the property to which the amenity is to be added
     * @param amenity  the amenity to be added
     */
    public void addAmenityToProperty(Property property, Amenity amenity) {
        if (property == null) {
            throw new ValidationException("Property cannot be null.");
        }
        if (amenity == null) {
            throw new ValidationException("Amenity cannot be null.");
        }
        bookingService.addAmenityToProperty(property, amenity);
        System.out.println("Amenity added successfully.");
    }

    /**
     * Retrieves amenities for a property.
     *
     * @param property the property whose amenities are to be retrieved
     * @return a list of amenities for the property
     */
    public List<Amenity> getAmenitiesForProperty(Property property) {
        if (property == null) {
            throw new ValidationException("Property cannot be null.");
        }
        return bookingService.getAmenitiesForProperty(property);
    }

    /**
     * Retrieves all amenities.
     *
     * @return a list of all amenities
     */
    public List<Amenity> getAllAmenities() {
        return bookingService.getAllAmenities();
    }

    /**
     * Retrieves an amenity by its ID.
     *
     * @param id the ID of the amenity
     * @return the amenity with the specified ID
     */
    public Amenity getAmenityById(int id) {
        if (id <= 0) {
            throw new ValidationException("Amenity ID must be positive.");
        }
        return bookingService.getAmenityById(id);
    }

// -------------------- Payment Operations --------------------

    /**
     * Processes a payment for a booking.
     *
     * @param booking the booking for which the payment is to be processed
     */
    public void processPaymentForBooking(Booking booking) {
        if (booking == null) {
            throw new ValidationException("Booking cannot be null.");
        }
        bookingService.processPaymentForBooking(booking);
        System.out.println("Payment processed successfully.");
    }

    /**
     * Views payments received by a host.
     *
     * @param host the host whose payments are to be viewed
     */
    public void viewPaymentsForHost(Host host) {
        if (host == null) {
            throw new ValidationException("Host cannot be null.");
        }
        List<Payment> payments = bookingService.getPaymentsForHost(host.getId());
        if (payments.isEmpty()) {
            System.out.println("No payments received by this host.");
        } else {
            for (Payment payment : payments) {
                System.out.println(payment);
            }
        }
    }

    /**
     * Views transaction history for a host.
     *
     * @param host the host whose transaction history is to be viewed
     */
    public void viewTransactionHistoryForHost(Host host) {
        if (host == null) {
            throw new ValidationException("Host cannot be null.");
        }
        List<Payment> transactionHistory = bookingService.getTransactionHistoryForHost(host.getId());
        if (transactionHistory.isEmpty()) {
            System.out.println("No transaction history for this host.");
        } else {
            System.out.println("Transaction history for " + host.getName() + ":");
            for (Payment payment : transactionHistory) {
                System.out.println(payment);
            }
        }
    }

    public void listAvailablePropertiesByDateSortedByPrice(Date date) {
        if (date == null) {
            throw new ValidationException("Date cannot be null.");
        }
        List<Property> properties = bookingService.getAvailablePropertiesByDateSortedByPrice(date);

        if (properties.isEmpty()) {
            System.out.println("No properties available on: " + date);
        } else {
            properties.forEach(property -> {
                System.out.println("Property ID: " + property.getId());
                System.out.println("Address: " + property.getAddress());
                System.out.println("Price per Night: " + property.getPricePerNight());
                System.out.println("Description: " + property.getDescription());
                System.out.println("Location: " + property.getLocation().getCity() + ", " + property.getLocation().getCountry());
                System.out.println("Amenities: " + property.getAmenityIDs().stream()
                        .map(id -> bookingService.getAmenityById(id).getName())
                        .collect(Collectors.joining(", ")));
                System.out.println("Cancellation Policy: " + property.getCancellationPolicy().getDescription());
                System.out.println();
            });
        }
    }

    public void processPayment(int paymentId) {
        bookingService.processPayment(paymentId);
        System.out.println("Payment processed successfully.");
    }

    public List<Payment> getAllPaymentsForHost(int hostId) {
        return bookingService.getPaymentsForHost(hostId);
    }

    public List<Booking> getUnpaidBookingsForGuest(int guestId) {
        return bookingService.getBookingsForGuest(guestId).stream()
                .filter(booking -> !booking.getPayment().isProcessed())
                .collect(Collectors.toList());
    }

    public void processPaymentForBooking(int bookingId) {
        Booking booking = bookingService.getBookingById(bookingId);
        if (booking == null) {
            throw new ValidationException("Invalid booking ID.");
        }
        bookingService.processPayment(booking.getPayment().getPaymentID());
    }

    public List<Payment> getUnpaidPaymentsForHost(int hostId) {
        return bookingService.getPaymentsForHost(hostId).stream()
                .filter(payment -> !payment.isProcessed())
                .collect(Collectors.toList());
    }

    public List<CancellationPolicy> getAllCancellationPolicies() {
        return bookingService.getAllCancellationPolicies();
    }

    public void addCancellationPolicy(CancellationPolicy policy) {
        if (policy == null) {
            throw new ValidationException("Cancellation policy cannot be null.");
        }
        bookingService.addCancellationPolicy(policy);
        System.out.println("Cancellation policy added successfully.");
    }

    public CancellationPolicy getCancellationPolicyById(int id) {
        if (id <= 0) {
            throw new ValidationException("Cancellation policy ID must be positive.");
        }
        return bookingService.getCancellationPolicyById(id);
    }

    public void addAmenity(Amenity amenity) {
        if (amenity == null) {
            throw new ValidationException("Amenity cannot be null.");
        }
        bookingService.addAmenity(amenity);
        System.out.println("Amenity added successfully.");
    }
}