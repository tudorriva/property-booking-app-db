// PropertyBookingController.java
package org.code.Controller;

import org.code.Entities.*;
import org.code.Services.*;
import org.code.Helpers.HelperFunctions;

import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Controller class for managing property bookings.
 */
public class PropertyBookingController {
    private final PropertyBookingService bookingService;

    /**
     * Constructs a PropertyBookingController with the specified booking service.
     *
     * @param bookingService the booking service to be used by this controller
     */
    public PropertyBookingController(PropertyBookingService bookingService) {
        this.bookingService = bookingService;
    }

    // -------------------- Host Operations --------------------

    /**
     * Retrieves reviews for a specific property.
     *
     * @param property The property whose reviews are to be fetched.
     * @return A list of reviews for the property.
     */
    public List<Review> getReviewsForProperty(Property property) {
        // Call with sorting by rating in descending order
        return bookingService.getReviewsForProperty(property.getId(), true, true); // Sort by rating, descending
    }


    /**
     * Adds a new host.
     *
     * @param host the host to be added
     */
    public void addHost(Host host) {
        bookingService.addHost(host);
        System.out.println("Host added successfully");
    }

    /**
     * Lists all hosts.
     */
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

    /**
     * Retrieves a host by their ID.
     *
     * @param id the ID of the host
     * @return the host with the specified ID
     */
    public Host getHostById(int id) {
        return bookingService.getHostById(id);
    }

    /**
     * Retrieves all hosts.
     *
     * @return a list of all hosts
     */
    public List<Host> getAllHosts() {
        return bookingService.getAllHosts();
    }

    /**
     * Retrieves properties managed by a specific host.
     *
     * @param hostId the ID of the host
     * @return a list of properties managed by the host
     */
    public List<Property> getPropertiesForHost(int hostId) {
        return bookingService.getPropertiesForHost(hostId);
    }

    /**
     * Lists properties managed by a specific host.
     *
     * @param host the host whose properties are to be listed
     */
    public void listPropertiesForHost(Host host) {
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

    /**
     * Shows properties managed by a specific host.
     *
     * @param host the host whose properties are to be shown
     */
    public void showPropertiesForHost(Host host) {
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

    /**
     * Lists a new property for a host.
     *
     * @param host               the host listing the property
     * @param address            the address of the property
     * @param pricePerNight      the price per night for the property
     * @param description        the description of the property
     * @param location           the location of the property
     * @param amenityIDs         the list of amenity IDs for the property
     * @param cancellationPolicy the cancellation policy of the property
     */
    public void listProperty(int id, Host host, String address, double pricePerNight, String description, Location location, List<Integer> amenityIDs, CancellationPolicy cancellationPolicy) {
        Property property = new Property(id, address, pricePerNight, description, location, amenityIDs, cancellationPolicy, host.getId());
        bookingService.addProperty(property);
        System.out.println("Property listed successfully.");
    }

    public CancellationPolicy getCancellationPolicyByDescription(String description) {
        return bookingService.getCancellationPolicyByDescription(description);
    }

    /**
     * Adds an amenity to a property managed by a host.
     *
     * @param host          the host managing the property
     * @param propertyIndex the index of the property in the host's property list
     * @param name          the name of the amenity
     * @param description   the description of the amenity
     */
    public void addAmenityToProperty(Host host, int propertyIndex, String name, String description) {
        List<Property> properties = getPropertiesForHost(host.getId());
        if (propertyIndex < 0 || propertyIndex >= properties.size()) {
            System.out.println("Invalid property number.");
            return;
        }

        Property property = properties.get(propertyIndex);
        int id = HelperFunctions.randomId();
        Amenity amenity = new Amenity(id, name, description);
        bookingService.addAmenityToProperty(property, amenity);
        System.out.println("Amenity added successfully.");
    }

    // -------------------- Guest Operations --------------------

    /**
     * Adds a new guest.
     *
     * @param guest the guest to be added
     */
    public void addGuest(Guest guest) {
        bookingService.addGuest(guest);
        System.out.println("Guest added successfully");
    }

    /**
     * Lists all guests.
     */
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

    /**
     * Retrieves a guest by their ID.
     *
     * @param id the ID of the guest
     * @return the guest with the specified ID
     */
    public Guest getGuestById(int id) {
        return bookingService.getGuestById(id);
    }

    /**
     * Retrieves all guests.
     *
     * @return a list of all guests
     */
    public List<Guest> getAllGuests() {
        return bookingService.getAllGuests();
    }

    /**
     * Retrieves bookings for a specific guest.
     *
     * @param guestId the ID of the guest
     * @return a list of bookings for the guest
     */
    public List<Booking> getBookingsForGuest(int guestId) {
        return bookingService.getBookingsForGuest(guestId);
    }

    /**
     * Books a property for a guest.
     *
     * @param guest        the guest booking the property
     * @param propertyId   the ID of the property to be booked
     * @param checkInDate  the check-in date
     * @param checkOutDate the check-out date
     */
    public void bookProperty(Guest guest, int propertyId, Date checkInDate, Date checkOutDate) {
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

    /**
     * Views bookings for a guest.
     *
     * @param guest the guest whose bookings are to be viewed
     */
    public void viewBookings(Guest guest) {
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

    /**
     * Leaves a review for a property.
     *
     * @param guest      the guest leaving the review
     * @param propertyId the ID of the property being reviewed
     * @param rating     the rating given by the guest
     * @param comment    the comment given by the guest
     */
    public void leaveReview(Guest guest, int propertyId, double rating, String comment) {
        Property property = bookingService.getPropertyById(propertyId);
        if (property != null) {
            bookingService.addReview(guest, property, rating, comment);
            System.out.println("Review added for property: " + property.getAddress());
        } else {
            System.out.println("Invalid property ID.");
        }
    }

    /**
     * Views all properties in a specific location.
     *
     * @param location the location to search for properties
     */
    public void viewAllPropertiesByLocation(Location location) {
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

    /**
     * Views all properties available on a specific date.
     *
     * @param date the date to search for available properties
     */
    public void viewAllPropertiesByDate(Date date) {
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
        return bookingService.getPropertyById(id);
    }

    /**
     * Lists properties in a specific location.
     *
     * @param location the location to search for properties
     */
    public void listPropertiesByLocation(Location location) {
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
        return bookingService.getAmenityById(id);
    }

    // -------------------- Payment Operations --------------------

    /**
     * Processes a payment for a booking.
     *
     * @param booking the booking for which the payment is to be processed
     */
    public void processPaymentForBooking(Booking booking) {
        bookingService.processPaymentForBooking(booking);
        System.out.println("Payment processed successfully.");
    }

    /**
     * Views payments received by a host.
     *
     * @param host the host whose payments are to be viewed
     */
    public void viewPaymentsForHost(Host host) {
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

    /**
     * Retrieves properties sorted by total reviews in descending order.
     */
//    public void listPropertiesByTotalReviews() {
//        List<Property> properties = bookingService.getPropertiesByTotalReviews();
//
//        if (properties.isEmpty()) {
//            System.out.println("No properties found.");
//        } else {
//            properties.forEach(property -> System.out.println(property.getId() + ": " + property.getAddress() + " - Total Reviews: " + bookingService.getReviewsForProperty(property.getId()).size()));
//        }
//    }
}