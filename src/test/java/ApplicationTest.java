import org.code.Exceptions.BusinessLogicException;
import org.code.Exceptions.ValidationException;
import org.junit.jupiter.api.*;
import org.code.Entities.*;
import org.code.Repository.DBRepository;
import org.code.Repository.IRepository;
import org.code.Services.PropertyBookingService;
import org.code.Controller.PropertyBookingController;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApplicationTest {
    private SessionFactory sessionFactory;
    private PropertyBookingService bookingService;
    private PropertyBookingController bookingController;
    private Host testHost;
    private Guest testGuest;
    private Property testProperty;
    private Transaction transaction;

    @BeforeAll
    void setup() {
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

        bookingService = new PropertyBookingService(
                hostRepo, guestRepo, propertyRepo, bookingRepo, reviewRepo, amenityRepo, locationRepo, cancellationPolicyRepo, paymentRepo, sessionFactory
        );
        bookingController = new PropertyBookingController(bookingService);

        // Initialize test data
        testHost = new Host(1, "John Doe", "john@example.com", "1234567890", 4.5);
        bookingController.addHost(testHost);

        testGuest = new Guest(1, "Jane Smith", "jane@example.com", "0987654321", 4.8);
        bookingController.addGuest(testGuest);
    }

    @BeforeEach
    void startTransaction() {
        transaction = sessionFactory.getCurrentSession().beginTransaction();
    }

    @AfterEach
    void rollbackTransaction() {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }

    @Test
    void testCRUDOperations() {
        // Create
        Location location = new Location(1, "New York", "USA");
        CancellationPolicy policy = new CancellationPolicy(1, "Flexible");
        testProperty = new Property(1, "123 Main St", 100.0, "A nice place", location, List.of(), policy, testHost.getId());
        bookingController.addProperty(testProperty);

        // Read
        List<Host> hosts = bookingService.getAllHosts();
        List<Guest> guests = bookingService.getAllGuests();
        List<Property> properties = bookingService.getAllProperties();
        assertTrue(hosts.stream().anyMatch(host -> host.getId() == testHost.getId()));
        assertTrue(guests.stream().anyMatch(guest -> guest.getId() == testGuest.getId()));
        assertTrue(properties.stream().anyMatch(property -> property.getId() == testProperty.getId()));

        // Update
        testHost.setName("John Updated");
        bookingService.addHost(testHost);
        Host updatedHost = bookingService.getHostById(testHost.getId());
        assertEquals("John Updated", updatedHost.getName());

        // Delete
        bookingService.deleteProperty(testProperty.getId());
        assertThrows(BusinessLogicException.class, () -> bookingService.getPropertyById(testProperty.getId()));
    }

    @Test
    void testBookProperty() {
        // Create and add the property
        Location location = new Location(1, "New York", "USA");
        CancellationPolicy policy = new CancellationPolicy(1, "Flexible");
        testProperty = new Property(1, "123 Main St", 100.0, "A nice place", location, List.of(), policy, testHost.getId());
        bookingController.addProperty(testProperty);

        // Ensure the property exists before booking
        testProperty = bookingService.getPropertyById(testProperty.getId());

        Date checkInDate = new Date();
        Date checkOutDate = new Date(checkInDate.getTime() + (1000 * 60 * 60 * 24 * 2)); // 2 days later

        bookingController.bookProperty(testGuest, testProperty.getId(), checkInDate, checkOutDate);

        List<Booking> bookings = bookingService.getBookingsForGuest(testGuest.getId());
        assertTrue(bookings.stream().anyMatch(booking -> booking.getPropertyID() == testProperty.getId()));
    }

    @Test
    void testBookPropertyException() {
        // Attempt to book a property with invalid dates
        Date invalidCheckIn = new Date();
        Date invalidCheckOut = new Date(invalidCheckIn.getTime() - (1000 * 60 * 60 * 24)); // Past date

        Exception exception = assertThrows(ValidationException.class, () -> {
            bookingController.bookProperty(testGuest, testProperty.getId(), invalidCheckIn, invalidCheckOut);
        });

        assertEquals("Check-in date cannot be after check-out date.", exception.getMessage());
    }

    @Test
    void testFilterPropertiesByLocation() {
        // Create and add properties
        Location location1 = new Location(1, "New York", "USA");
        Location location2 = new Location(2, "Los Angeles", "USA");
        CancellationPolicy policy = new CancellationPolicy(1, "Flexible");
        Property property1 = new Property(1, "123 Main St", 100.0, "A nice place", location1, List.of(), policy, testHost.getId());
        Property property2 = new Property(2, "456 Elm St", 150.0, "Another nice place", location2, List.of(), policy, testHost.getId());
        bookingController.addProperty(property1);
        bookingController.addProperty(property2);

        // Filter properties by location
        List<Property> properties = bookingService.filterPropertiesByLocation(location1);
        assertTrue(properties.stream().allMatch(property -> property.getLocation().equals(location1)));
    }

    @Test
    void testFilterPropertiesByLocationException() {
        // Attempt to filter properties with an invalid location
        Location invalidLocation = new Location("", "");

        Exception exception = assertThrows(ValidationException.class, () -> {
            bookingController.filterPropertiesByLocation(invalidLocation);
        });

        assertEquals("Location details are invalid.", exception.getMessage());
    }

    @Test
    void testViewPropertiesByDate() {
        // Setup test data
        Location location = new Location(1, "New York", "USA");
        CancellationPolicy policy = new CancellationPolicy(1, "Flexible");
        Property property1 = new Property(1, "123 Main St", 100.0, "A nice place", location, List.of(), policy, testHost.getId());
        Property property2 = new Property(2, "456 Elm St", 150.0, "Another nice place", location, List.of(), policy, testHost.getId());
        bookingController.addProperty(property1);
        bookingController.addProperty(property2);

        // Book property1
        Date checkInDate = new Date();
        Date checkOutDate = new Date(checkInDate.getTime() + (1000 * 60 * 60 * 24 * 2)); // 2 days later
        bookingController.bookProperty(testGuest, property1.getId(), checkInDate, checkOutDate);

        // View properties by date
        List<Property> properties = bookingService.getAllProperties().stream()
                .filter(property -> bookingService.checkAvailability(property.getId(), checkInDate, checkInDate))
                .collect(Collectors.toList());

        // Verify that property2 is available
        assertTrue(properties.stream().anyMatch(p -> p.getId() == property2.getId()));
    }

    @Test
    void testViewPropertiesByDateException() {
        // Attempt to view properties with an invalid date format
        Exception exception = assertThrows(ValidationException.class, () -> {
            bookingController.viewAllPropertiesByDate(null);
        });

        assertEquals("Date cannot be null.", exception.getMessage());
    }

    @Test
    void testViewAvailablePropertiesByDateSortedByPriceException() {
        // Attempt to view available properties with an invalid date
        Exception exception = assertThrows(ValidationException.class, () -> {
            bookingController.listAvailablePropertiesByDateSortedByPrice(null);
        });

        assertEquals("Date cannot be null.", exception.getMessage());
    }

    @AfterAll
    void cleanup() {
        sessionFactory.close();
    }
}