import org.code.Entities.*;
import org.code.Repository.DBRepository;
import org.code.Services.PropertyBookingService;
import org.code.Controller.PropertyBookingController;
import org.code.Mappers.*;

import java.util.Date;
import java.util.List;

public class AppTest {
    public static void main(String[] args) {
        // Initialize repositories
        IRepository<Host> hostRepo = new DBRepository<>(sessionFactory, Host.class);
        IRepository<Guest> guestRepo = new DBRepository<>(sessionFactory, Guest.class);
        IRepository<Property> propertyRepo = new DBRepository<>(sessionFactory, Property.class);
        IRepository<Booking> bookingRepo = new DBRepository<>(sessionFactory, Booking.class);
        IRepository<Review> reviewRepo = new DBRepository<>(sessionFactory, Review.class);
        IRepository<Amenity> amenityRepo = new DBRepository<>(sessionFactory, Amenity.class);
        IRepository<Location> locationRepo = new DBRepository<>(sessionFactory, Location.class);
        IRepository<CancellationPolicy> cancellationPolicyRepo = new DBRepository<>(sessionFactory, CancellationPolicy.class);
        IRepository<Payment> paymentRepo = new DBRepository<>(sessionFactory, Payment.class);

        // Initialize service
        PropertyBookingService bookingService = new PropertyBookingService(
                hostRepo, guestRepo, propertyRepo, bookingRepo, reviewRepo, amenityRepo, locationRepo, cancellationPolicyRepo, paymentRepo
        );

        // Initialize controller
        PropertyBookingController bookingController = new PropertyBookingController(bookingService);

        // Test adding a host
        Host host = new Host(1, "John Doe", "john@example.com", "1234567890", 4.5);
        bookingController.addHost(host);

        // Test adding a guest
        Guest guest = new Guest(1, "Jane Smith", "jane@example.com", "0987654321", 4.8);
        bookingController.addGuest(guest);

        // Test listing all hosts
        bookingController.listAllHosts();

        // Test listing all guests
        bookingController.listAllGuests();

        // Test adding a property
        Location location = new Location(1, "New York", "USA");
        CancellationPolicy policy = new CancellationPolicy(1, "Flexible");
        Property property = new Property(1, "123 Main St", 100.0, "A nice place", location, List.of(), policy, host.getId());
        bookingController.addProperty(property);

        // Test listing all properties
        bookingController.listAllProperties();

        // Test booking a property
        Date checkInDate = new Date();
        Date checkOutDate = new Date(checkInDate.getTime() + (1000 * 60 * 60 * 24 * 2)); // 2 days later
        bookingController.bookProperty(guest, property.getId(), checkInDate, checkOutDate);

        // Test viewing bookings for a guest
        bookingController.viewBookings(guest);
    }
}