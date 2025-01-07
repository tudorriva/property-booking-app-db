package org.code.Services;

import org.code.Entities.*;
import org.code.Exceptions.BusinessLogicException;
import org.code.Exceptions.EntityNotFoundException;
import org.code.Repository.IRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.*;
import java.util.stream.Collectors;

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
    private final SessionFactory sessionFactory;

    public PropertyBookingService(
            IRepository<Host> hostRepo,
            IRepository<Guest> guestRepo,
            IRepository<Property> propertyRepo,
            IRepository<Booking> bookingRepo,
            IRepository<Review> reviewRepo,
            IRepository<Amenity> amenityRepo,
            IRepository<Location> locationRepo,
            IRepository<CancellationPolicy> cancellationPolicyRepo,
            IRepository<Payment> paymentRepo,
            SessionFactory sessionFactory) {
        this.hostRepo = hostRepo;
        this.guestRepo = guestRepo;
        this.propertyRepo = propertyRepo;
        this.bookingRepo = bookingRepo;
        this.reviewRepo = reviewRepo;
        this.amenityRepo = amenityRepo;
        this.locationRepo = locationRepo;
        this.cancellationPolicyRepo = cancellationPolicyRepo;
        this.paymentRepo = paymentRepo;
        this.sessionFactory = sessionFactory;
    }

    public void addHost(Host host) {
        try {
            if (host == null) {
                throw new BusinessLogicException("Host cannot be null.");
            }
            hostRepo.create(host);
        } catch (Exception e) {
            throw new BusinessLogicException("Error adding host: " + e.getMessage(), e);
        }
    }

    public void addGuest(Guest guest) {
        try {
            if (guest == null) {
                throw new BusinessLogicException("Guest cannot be null.");
            }
            guestRepo.create(guest);
        } catch (Exception e) {
            throw new BusinessLogicException("Error adding guest: " + e.getMessage(), e);
        }
    }

    public List<Host> getAllHosts() {
        try {
            return hostRepo.getAll();
        } catch (Exception e) {
            throw new BusinessLogicException("Error retrieving hosts: " + e.getMessage(), e);
        }
    }

    public List<Guest> getAllGuests() {
        try {
            return guestRepo.getAll();
        } catch (Exception e) {
            throw new BusinessLogicException("Error retrieving guests: " + e.getMessage(), e);
        }
    }

    public Host getHostById(int id) {
        try {
            Host host = hostRepo.read(id);
            if (host == null) {
                throw new EntityNotFoundException("Host with ID " + id + " not found.");
            }
            return host;
        } catch (Exception e) {
            throw new BusinessLogicException("Error retrieving host by ID: " + e.getMessage(), e);
        }
    }

    public Guest getGuestById(int id) {
        try {
            Guest guest = guestRepo.read(id);
            if (guest == null) {
                throw new EntityNotFoundException("Guest with ID " + id + " not found.");
            }
            return guest;
        } catch (Exception e) {
            throw new BusinessLogicException("Error retrieving guest by ID: " + e.getMessage(), e);
        }
    }

    public List<Booking> getBookingsForGuest(int guestId) {
        try {
            return bookingRepo.getAll().stream()
                    .filter(booking -> booking.getGuestID() == guestId)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BusinessLogicException("Error retrieving bookings for guest: " + e.getMessage(), e);
        }
    }

    public List<Property> getPropertiesForHost(int hostId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Property> properties = propertyRepo.getAll().stream()
                    .filter(property -> property.getHostID() == hostId)
                    .collect(Collectors.toList());
            transaction.commit();
            return properties;
        } catch (Exception e) {
            transaction.rollback();
            throw new BusinessLogicException("Error fetching properties for host: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    public void addProperty(Property property) {
        try {
            if (property == null) {
                throw new BusinessLogicException("Property cannot be null.");
            }
            // Ensure the cancellation policy exists
            CancellationPolicy policy = property.getCancellationPolicy();
            if (policy != null) {
                CancellationPolicy existingPolicy = getCancellationPolicyByDescription(policy.getDescription());
                if (existingPolicy == null) {
                    cancellationPolicyRepo.create(policy);
                } else {
                    property.setCancellationPolicy(existingPolicy);
                }
            }

            // Ensure the location exists
            Location location = property.getLocation();
            if (location != null) {
                Location existingLocation = getLocationByCityAndCountry(location.getCity(), location.getCountry());
                if (existingLocation == null) {
                    locationRepo.create(location);
                } else {
                    property.setLocation(existingLocation);
                }
            }

            // Create the property
            propertyRepo.create(property);
        } catch (Exception e) {
            throw new BusinessLogicException("Error adding property: " + e.getMessage(), e);
        }
    }

    public Location getLocationByCityAndCountry(String city, String country) {
        try {
            return locationRepo.getAll().stream()
                    .filter(loc -> loc.getCity().equals(city) && loc.getCountry().equals(country))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            throw new BusinessLogicException("Error retrieving location: " + e.getMessage(), e);
        }
    }

    public List<Property> getAllProperties() {
        try {
            return propertyRepo.getAll();
        } catch (Exception e) {
            throw new BusinessLogicException("Error retrieving properties: " + e.getMessage(), e);
        }
    }

    public Property getPropertyById(int id) {
        try {
            Property property = propertyRepo.read(id);
            if (property == null) {
                throw new EntityNotFoundException("Property with ID " + id + " not found.");
            }
            return property;
        } catch (Exception e) {
            throw new BusinessLogicException("Error retrieving property by ID: " + e.getMessage(), e);
        }
    }

    public List<Property> getPropertiesByLocation(Location location) {
        try {
            return propertyRepo.getAll().stream()
                    .filter(p -> p.getLocation().equals(location))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BusinessLogicException("Error retrieving properties by location: " + e.getMessage(), e);
        }
    }

    public void addAmenityToProperty(Property property, Amenity amenity) {
        try {
            if (property == null || amenity == null) {
                throw new BusinessLogicException("Property or Amenity cannot be null.");
            }
            amenityRepo.create(amenity);
            property.getAmenityIDs().add(amenity.getAmenityID());
            propertyRepo.update(property);
        } catch (Exception e) {
            throw new BusinessLogicException("Error adding amenity to property: " + e.getMessage(), e);
        }
    }

    public CancellationPolicy getCancellationPolicyByDescription(String description) {
        try {
            return cancellationPolicyRepo.getAll().stream()
                    .filter(policy -> policy.getDescription().equals(description))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            throw new BusinessLogicException("Error retrieving cancellation policy: " + e.getMessage(), e);
        }
    }

    public List<Amenity> getAmenitiesForProperty(Property property) {
        try {
            return property.getAmenityIDs().stream()
                    .map(amenityRepo::read)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BusinessLogicException("Error retrieving amenities for property: " + e.getMessage(), e);
        }
    }

    public Property getPropertyForBooking(Booking booking) {
        try {
            return propertyRepo.read(booking.getPropertyID());
        } catch (Exception e) {
            throw new BusinessLogicException("Error retrieving property for booking: " + e.getMessage(), e);
        }
    }

    public List<Property> filterPropertiesByLocation(Location location) {
        try {
            return propertyRepo.getAll().stream()
                    .filter(property -> property.getLocation().equals(location))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BusinessLogicException("Error filtering properties by location: " + e.getMessage(), e);
        }
    }

    public void deleteProperty(int propertyId) {
        try {
            Property property = propertyRepo.read(propertyId);
            if (property == null) {
                throw new EntityNotFoundException("Property with ID " + propertyId + " not found.");
            }
            propertyRepo.delete(propertyId);
        } catch (Exception e) {
            throw new BusinessLogicException("Error deleting property: " + e.getMessage(), e);
        }
    }

    public boolean bookProperty(Guest guest, Property property, Date checkInDate, Date checkOutDate) {
        try {
            if (!checkAvailability(property.getId(), checkInDate, checkOutDate)) {
                throw new BusinessLogicException("Property is not available for the selected dates.");
            }
            double totalPrice = property.getPricePerNight() * getDaysBetween(checkInDate, checkOutDate);

            int paymentId = generateUniqueId();
            Payment payment = new Payment(paymentId, totalPrice, new Date());
            paymentRepo.create(payment);

            int bookingId = generateUniqueId();
            Booking booking = new Booking(bookingId, checkOutDate, checkInDate, totalPrice, guest.getId(), property.getId(), payment);

            bookingRepo.create(booking);

            return true;
        } catch (Exception e) {
            throw new BusinessLogicException("Error booking property: " + e.getMessage(), e);
        }
    }

    public List<Booking> getBookingsForProperty(int propertyId) {
        try {
            return bookingRepo.getAll().stream()
                    .filter(booking -> booking.getPropertyID() == propertyId)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BusinessLogicException("Error retrieving bookings for property: " + e.getMessage(), e);
        }
    }

    public Booking getBookingById(int bookingId) {
        try {
            Booking booking = bookingRepo.read(bookingId);
            if (booking == null) {
                throw new EntityNotFoundException("Booking with ID " + bookingId + " not found.");
            }
            return booking;
        } catch (Exception e) {
            throw new BusinessLogicException("Error retrieving booking by ID: " + e.getMessage(), e);
        }
    }

    public List<Guest> filterGuestsByBookingCount(int minBookings) {
        try {
            return guestRepo.getAll().stream()
                    .filter(guest -> getBookingsForGuest(guest.getId()).size() >= minBookings)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BusinessLogicException("Error filtering guests by booking count: " + e.getMessage(), e);
        }
    }

    public Amenity getAmenityById(int id) {
        try {
            Amenity amenity = amenityRepo.read(id);
            if (amenity == null) {
                throw new EntityNotFoundException("Amenity with ID " + id + " not found.");
            }
            return amenity;
        } catch (Exception e) {
            throw new BusinessLogicException("Error retrieving amenity by ID: " + e.getMessage(), e);
        }
    }

    public List<Amenity> getAllAmenities() {
        try {
            return amenityRepo.getAll();
        } catch (Exception e) {
            throw new BusinessLogicException("Error retrieving all amenities: " + e.getMessage(), e);
        }
    }

    public void addReview(Guest guest, Property property, double rating, String comment) {
        try {
            int reviewId = generateUniqueId();
            Review review = new Review(reviewId, guest.getId(), property.getId(), rating, comment, new Date());
            reviewRepo.create(review);
        } catch (Exception e) {
            throw new BusinessLogicException("Error adding review: " + e.getMessage(), e);
        }
    }

    public List<Review> getReviewsForProperty(int propertyId, boolean sortByRating, boolean descending) {
        try {
            List<Review> reviews = reviewRepo.getAll().stream()
                    .filter(review -> review.getPropertyID() == propertyId)
                    .collect(Collectors.toList());

            if (sortByRating) {
                reviews.sort((r1, r2) -> {
                    int comparison = Double.compare(r1.getRating(), r2.getRating());
                    return descending ? -comparison : comparison;
                });
            }

            return reviews;
        } catch (Exception e) {
            throw new BusinessLogicException("Error retrieving reviews for property: " + e.getMessage(), e);
        }
    }

    private long getDaysBetween(Date start, Date end) {
        return (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24);
    }

    private int generateUniqueId() {
        return (int) (Math.random() * 10000);
    }

    public void processPaymentForBooking(Booking booking) {
        try {
            Payment payment = booking.getPayment();
            if (!payment.isProcessed()) {
                payment.processPayment();
                paymentRepo.update(payment);
            }
        } catch (Exception e) {
            throw new BusinessLogicException("Error processing payment for booking: " + e.getMessage(), e);
        }
    }

    public List<Payment> getPaymentsForHost(int hostId) {
        try {
            return bookingRepo.getAll().stream()
                    .filter(booking -> getPropertyById(booking.getPropertyID()).getHostID() == hostId)
                    .map(Booking::getPayment)
                    .filter(Payment::isProcessed)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BusinessLogicException("Error retrieving payments for host: " + e.getMessage(), e);
        }
    }

    public List<Payment> getTransactionHistoryForHost(int hostId) {
        try {
            return bookingRepo.getAll().stream()
                    .filter(booking -> getPropertyById(booking.getPropertyID()).getHostID() == hostId)
                    .map(Booking::getPayment)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BusinessLogicException("Error retrieving transaction history for host: " + e.getMessage(), e);
        }
    }

    public boolean checkAvailability(int propertyId, Date checkInDate, Date checkOutDate) {
        try {
            List<Booking> bookings = bookingRepo.getAll().stream()
                    .filter(booking -> booking.getPropertyID() == propertyId)
                    .collect(Collectors.toList());

            for (Booking booking : bookings) {
                if (booking.getCheckInDate().before(checkOutDate) && booking.getCheckOutDate().after(checkInDate)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            throw new BusinessLogicException("Error checking property availability: " + e.getMessage(), e);
        }
    }

    public List<Property> getAvailablePropertiesByDateSortedByPrice(Date date) {
        try {
            return propertyRepo.getAll().stream()
                    .filter(property -> checkAvailability(property.getId(), date, date))
                    .sorted(Comparator.comparingDouble(Property::getPricePerNight))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BusinessLogicException("Error retrieving available properties by date: " + e.getMessage(), e);
        }
    }

    public List<Property> getPropertiesByTotalReviews() {
        try {
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
        } catch (Exception e) {
            throw new BusinessLogicException("Error retrieving properties by total reviews: " + e.getMessage(), e);
        }
    }

    public void addAmenity(Amenity amenity) {
        try {
            amenityRepo.create(amenity);
        } catch (Exception e) {
            throw new BusinessLogicException("Error adding amenity: " + e.getMessage(), e);
        }
    }

    public void addLocation(Location location) {
        try {
            locationRepo.create(location);
        } catch (Exception e) {
            throw new BusinessLogicException("Error adding location: " + e.getMessage(), e);
        }
    }

    public void addCancellationPolicy(CancellationPolicy cancellationPolicy) {
        try {
            cancellationPolicyRepo.create(cancellationPolicy);
        } catch (Exception e) {
            throw new BusinessLogicException("Error adding cancellation policy: " + e.getMessage(), e);
        }
    }

    public IRepository<Host> getHostRepo() {
        return hostRepo;
    }

    public IRepository<Guest> getGuestRepo() {
        return guestRepo;
    }

    public IRepository<Property> getPropertyRepo() {
        return propertyRepo;
    }

    public IRepository<Booking> getBookingRepo() {
        return bookingRepo;
    }

    public IRepository<Review> getReviewRepo() {
        return reviewRepo;
    }

    public void createPayment(Payment payment) {
        try {
            paymentRepo.create(payment);
        } catch (Exception e) {
            throw new BusinessLogicException("Error creating payment: " + e.getMessage(), e);
        }
    }

    public void processPayment(int paymentId) {
        try {
            Payment payment = paymentRepo.read(paymentId);
            if (payment == null) {
                throw new EntityNotFoundException("Payment with ID " + paymentId + " not found.");
            }
            payment.processPayment();
            paymentRepo.update(payment);
        } catch (Exception e) {
            throw new BusinessLogicException("Error processing payment: " + e.getMessage(), e);
        }
    }
}