import org.junit.jupiter.api.*;
import org.code.Entities.*;
import org.code.Repository.DBRepository;
import org.code.Repository.IRepository;
import org.code.Services.PropertyBookingService;
import org.code.Controller.PropertyBookingController;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApplicationTest {
    private SessionFactory sessionFactory;
    private PropertyBookingService bookingService;
    private PropertyBookingController bookingController;
    private Host testHost;
    private Guest testGuest;
    private Property testProperty;

    @BeforeAll
    void setup() {
        // Initialize Hibernate
        sessionFactory = new Configuration().configure().buildSessionFactory();

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

        // Initialize service and controller
//        bookingService = new PropertyBookingService(
//                hostRepo, guestRepo, propertyRepo, bookingRepo, reviewRepo, amenityRepo, locationRepo, cancellationPolicyRepo, paymentRepo
//        );
//
//    we are having some problems but we will made it through
        bookingController = new PropertyBookingController(bookingService);
    }

    @Test
    void testCRUDOperations() {
        // Create
        testHost = new Host(1, "John Doe", "john@example.com", "1234567890", 4.5);
        bookingController.addHost(testHost);

        testGuest = new Guest(1, "Jane Smith", "jane@example.com", "0987654321", 4.8);
        bookingController.addGuest(testGuest);

        Location location = new Location(1, "New York", "USA");
        CancellationPolicy policy = new CancellationPolicy(1, "Flexible");
        testProperty = new Property(1, "123 Main St", 100.0, "A nice place", location, List.of(), policy, testHost.getId());
        bookingController.addProperty(testProperty);

        // Read
        List<Host> hosts = bookingService.getAllHosts();
        List<Guest> guests = bookingService.getAllGuests();
        List<Property> properties = bookingService.getAllProperties();
        assertTrue(hosts.contains(testHost));
        assertTrue(guests.contains(testGuest));
        assertTrue(properties.contains(testProperty));

        // Update
        testHost.setName("John Updated");
        bookingService.addHost(testHost);
        Host updatedHost = bookingService.getHostById(testHost.getId());
        assertEquals("John Updated", updatedHost.getName());

        // Delete
        bookingService.deleteProperty(testProperty.getId());
        Property deletedProperty = bookingService.getPropertyById(testProperty.getId());
        assertNull(deletedProperty);
    }

    @Test
    void testBookProperty() {
        Date checkInDate = new Date();
        Date checkOutDate = new Date(checkInDate.getTime() + (1000 * 60 * 60 * 24 * 2)); // 2 days later

        //boolean bookingSuccess = bookingController.bookProperty(testGuest, testProperty.getId(), checkInDate, checkOutDate);
        //assertTrue(bookingSuccess);

        List<Booking> bookings = bookingService.getBookingsForGuest(testGuest.getId());
        assertFalse(bookings.isEmpty());
    }

    @Test
    void testExceptionHandling() {
        // Attempt to book a property with invalid dates
        Date invalidCheckIn = new Date();
        Date invalidCheckOut = new Date(invalidCheckIn.getTime() - (1000 * 60 * 60 * 24)); // Past date

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingController.bookProperty(testGuest, testProperty.getId(), invalidCheckIn, invalidCheckOut);
        });

        assertEquals("Check-out date must be after check-in date", exception.getMessage());
    }

    @AfterAll
    void cleanup() {
        sessionFactory.close();
    }
}
