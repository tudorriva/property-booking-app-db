package org.code;

import org.code.Controller.PropertyBookingController;
import org.code.Entities.*;
import org.code.Mappers.EntityMapper;
import org.code.Repository.DBRepository;
import org.code.Repository.FileRepository;
import org.code.Repository.IRepository;
import org.code.Services.PropertyBookingService;
import org.code.Repository.InMemoryRepo;
import org.code.Views.AdminView;
import org.code.Views.GuestView;
import org.code.Views.HostView;
import org.code.Mappers.*;
import org.code.Views.LoginView;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class PropertyBookingApp {
    private final PropertyBookingController controller;
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

        IRepository<Host> hostRepo = new DBRepository<>(sessionFactory, Host.class);
        IRepository<Guest> guestRepo = new DBRepository<>(sessionFactory, Guest.class);
        IRepository<Property> propertyRepo = new DBRepository<>(sessionFactory, Property.class);
        IRepository<Booking> bookingRepo = new DBRepository<>(sessionFactory, Booking.class);
        IRepository<Review> reviewRepo = new DBRepository<>(sessionFactory, Review.class);
        IRepository<Amenity> amenityRepo = new DBRepository<>(sessionFactory, Amenity.class);
        IRepository<Location> locationRepo = new DBRepository<>(sessionFactory, Location.class);
        IRepository<CancellationPolicy> cancellationPolicyRepo = new DBRepository<>(sessionFactory, CancellationPolicy.class);
        IRepository<Payment> paymentRepo = new DBRepository<>(sessionFactory, Payment.class);

        PropertyBookingService bookingService = new PropertyBookingService(
                hostRepo, guestRepo, propertyRepo, bookingRepo, reviewRepo, amenityRepo, locationRepo, cancellationPolicyRepo, paymentRepo, sessionFactory);

        PropertyBookingController controller = new PropertyBookingController(bookingService);

        PropertyBookingApp app = new PropertyBookingApp(controller);
        app.run();
    }

    private void run() {
//        boolean running = true;
//        while (running) {
//            showMainMenu();
//            int choice = Integer.parseInt(scanner.nextLine());
//
//            switch (choice) {
//                case 1 -> new AdminView(controller, scanner).run();
//                case 2 -> new GuestView(controller, scanner).run();
//                case 3 -> new HostView(controller, scanner).run();
//                case 0 -> running = false;
//                default -> System.out.println("Invalid choice. Please try again.");
//            }
//        }
        new LoginView(controller, scanner).run();
        System.out.println("Exiting the Property Booking System. Goodbye!");
    }

    private void showMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Admin View");
        System.out.println("2. Guest View");
        System.out.println("3. Host View");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }
}