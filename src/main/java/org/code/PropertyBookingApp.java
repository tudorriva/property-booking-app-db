package org.code;

import org.code.Controller.PropertyBookingController;
import org.code.Entities.*;
import org.code.Repository.DBRepository;
import org.code.Repository.FileRepository;
import org.code.Repository.IRepository;
import org.code.Repository.InMemoryRepo;
import org.code.Services.PropertyBookingService;
import org.code.Views.LoginView;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Scanner;
import java.util.List;
import java.util.Date;

public class PropertyBookingApp {
    private PropertyBookingController controller;
    private final Scanner scanner;
    private static SessionFactory sessionFactory;

    public PropertyBookingApp(PropertyBookingController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        try {
            // Initialize Hibernate
            sessionFactory = new Configuration().configure().buildSessionFactory();
            System.out.println("Hibernate configuration loaded successfully.");

            // Open a session to test the connection
            Session session = sessionFactory.openSession();
            System.out.println("Connected to the database successfully.");

            // Close the session
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        PropertyBookingApp app = new PropertyBookingApp(null);

        app.run(scanner);
    }

    private void run(Scanner scanner) {
        PropertyBookingService bookingService = initializeRepositories(scanner);

        // Populate in-memory data if using in-memory repository
        if (bookingService.getHostRepo() instanceof InMemoryRepo) {
            populateInMemoryData(bookingService);
        }

        PropertyBookingController controller = new PropertyBookingController(bookingService);
        this.controller = controller;

        new LoginView(controller, scanner).run();
        System.out.println("Exiting the Property Booking System. Goodbye!");
    }

    private PropertyBookingService initializeRepositories(Scanner scanner) {
        System.out.println("Select storage type:");
        System.out.println("1: In-Memory Storage");
        System.out.println("2: File-based Storage");
        System.out.println("3: Database Storage");
        System.out.print("Enter choice: ");
        int choice = Integer.parseInt(scanner.nextLine());

        IRepository<Host> hostRepo;
        IRepository<Guest> guestRepo;
        IRepository<Property> propertyRepo;
        IRepository<Booking> bookingRepo;
        IRepository<Review> reviewRepo;
        IRepository<Amenity> amenityRepo;
        IRepository<Location> locationRepo;
        IRepository<CancellationPolicy> cancellationPolicyRepo;
        IRepository<Payment> paymentRepo;

        String basePath = "C:\\Users\\tudor\\Desktop\\proiect retele\\server\\property-booking-app\\src\\main\\java\\org\\code\\Files\\";

        switch (choice) {
            case 1:
                hostRepo = new InMemoryRepo<>();
                guestRepo = new InMemoryRepo<>();
                propertyRepo = new InMemoryRepo<>();
                bookingRepo = new InMemoryRepo<>();
                reviewRepo = new InMemoryRepo<>();
                amenityRepo = new InMemoryRepo<>();
                locationRepo = new InMemoryRepo<>();
                cancellationPolicyRepo = new InMemoryRepo<>();
                paymentRepo = new InMemoryRepo<>();
                break;
            case 2:
                hostRepo = new FileRepository<>(basePath + "hosts.txt");
                guestRepo = new FileRepository<>(basePath + "guests.txt");
                propertyRepo = new FileRepository<>(basePath + "properties.txt");
                bookingRepo = new FileRepository<>(basePath + "bookings.txt");
                reviewRepo = new FileRepository<>(basePath + "reviews.txt");
                amenityRepo = new FileRepository<>(basePath + "amenities.txt");
                locationRepo = new FileRepository<>(basePath + "locations.txt");
                cancellationPolicyRepo = new FileRepository<>(basePath + "cancellationPolicies.txt");
                paymentRepo = new FileRepository<>(basePath + "payments.txt");
                break;
            case 3:
                hostRepo = new DBRepository<>(sessionFactory, Host.class);
                guestRepo = new DBRepository<>(sessionFactory, Guest.class);
                propertyRepo = new DBRepository<>(sessionFactory, Property.class);
                bookingRepo = new DBRepository<>(sessionFactory, Booking.class);
                reviewRepo = new DBRepository<>(sessionFactory, Review.class);
                amenityRepo = new DBRepository<>(sessionFactory, Amenity.class);
                locationRepo = new DBRepository<>(sessionFactory, Location.class);
                cancellationPolicyRepo = new DBRepository<>(sessionFactory, CancellationPolicy.class);
                paymentRepo = new DBRepository<>(sessionFactory, Payment.class);
                break;
            default:
                throw new IllegalArgumentException("Invalid choice. Please restart the application and select a valid option.");
        }

        return new PropertyBookingService(
                hostRepo, guestRepo, propertyRepo, bookingRepo, reviewRepo, amenityRepo, locationRepo, cancellationPolicyRepo, paymentRepo, sessionFactory);
    }

    private void populateInMemoryData(PropertyBookingService bookingService) {
        // Sample Hosts
        Host host1 = new Host(1, "Ion Popescu", "ion.popescu@example.com", "0712345678", 4.5);
        Host host2 = new Host(2, "Maria Ionescu", "maria.ionescu@example.com", "0723456789", 4.7);
        bookingService.getHostRepo().create(host1);
        bookingService.getHostRepo().create(host2);

        // Sample Guests
        Guest guest1 = new Guest(1, "Andrei Georgescu", "andrei.georgescu@example.com", "0734567890", 4.2);
        Guest guest2 = new Guest(2, "Elena Dumitrescu", "elena.dumitrescu@example.com", "0745678901", 4.8);
        bookingService.getGuestRepo().create(guest1);
        bookingService.getGuestRepo().create(guest2);

        // Sample Properties
        Location location1 = new Location(1, "Bucharest", "Romania");
        Location location2 = new Location(2, "Cluj-Napoca", "Romania");
        Amenity amenity1 = new Amenity(1, "WiFi", "High-speed internet");
        Amenity amenity2 = new Amenity(2, "Parking", "Free parking space");
        CancellationPolicy policy1 = new CancellationPolicy(1, "Flexible");
        CancellationPolicy policy2 = new CancellationPolicy(2, "Strict");

        Property property1 = new Property(1, "Strada Unirii 10", 100.0, "Cozy apartment in Bucharest", location1, List.of(amenity1.getAmenityID()), policy1, host1.getId());
        Property property2 = new Property(2, "Strada Libertatii 5", 150.0, "Modern apartment in Cluj-Napoca", location2, List.of(amenity2.getAmenityID()), policy2, host2.getId());

        // Sample Bookings
        Booking booking1 = new Booking(1, new Date(), new Date(), 100.0, guest1.getId(), property1.getId(), null);
        Booking booking2 = new Booking(2, new Date(), new Date(), 150.0, guest2.getId(), property2.getId(), null);bookingService.getBookingRepo().create(booking1);
        bookingService.getBookingRepo().create(booking2);

        // Sample Reviews
        Review review1 = new Review(1, guest1.getId(), property1.getId(), 4.0, "Nice place to stay.", new Date());
        Review review2 = new Review(2, guest2.getId(), property2.getId(), 5.0, "Excellent experience.", new Date());
        bookingService.getReviewRepo().create(review1);
        bookingService.getReviewRepo().create(review2);
    }
}