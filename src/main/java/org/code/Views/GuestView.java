package org.code.Views;

import org.code.Controller.PropertyBookingController;
import org.code.Entities.Booking;
import org.code.Entities.Guest;
import org.code.Exceptions.ValidationException;
import org.code.Helpers.HelperFunctions;
import org.code.Entities.Location;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class GuestView {
    private final PropertyBookingController controller;
    private final Scanner scanner;
    private final Guest guest;

    public GuestView(PropertyBookingController controller, Scanner scanner, Guest guest) {
        this.controller = controller;
        this.scanner = scanner;
        this.guest = guest;
    }

    public void run() {
        boolean running = true;
        while (running) {
            showGuestMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> bookProperty(guest);
                case 2 -> controller.viewBookings(guest);
                case 3 -> leaveReview(guest);
                case 4 -> payForBooking(guest);
                case 5 -> filterPropertiesByLocation();
                case 6 -> viewPropertiesByDate();
                case 7 -> viewAvailablePropertiesByDateSortedByPrice();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        new LoginView(controller, scanner).run();
    }

    private void showGuestMenu() {
        System.out.println("\nGuest Menu:");
        System.out.println("1. Book a Property");
        System.out.println("2. View My Bookings");
        System.out.println("3. Leave a Review");
        System.out.println("4. Pay for a Booking");
        System.out.println("5. Filter Properties by Location");
        System.out.println("6. View Properties by Date");
        System.out.println("7. View Available Properties by Date Sorted by Price");
        System.out.println("0. Go back");
        System.out.print("Choose an option: ");
    }

    private void bookProperty(Guest guest) {
        try {
            System.out.print("Enter property ID to book: ");
            int propertyId = Integer.parseInt(scanner.nextLine());
            if (propertyId <= 0) throw new ValidationException("Property ID must be positive.");

            System.out.print("Enter check-in date (YYYY-MM-DD): ");
            Date checkInDate = new SimpleDateFormat("yyyy-MM-dd").parse(scanner.nextLine());

            System.out.print("Enter check-out date (YYYY-MM-DD): ");
            Date checkOutDate = new SimpleDateFormat("yyyy-MM-dd").parse(scanner.nextLine());

            controller.bookProperty(guest, propertyId, checkInDate, checkOutDate);
        } catch (ValidationException e) {
            System.out.println("Validation Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid date format. Please enter in YYYY-MM-DD format.");
        }
    }

    private void leaveReview(Guest guest) {
        try {
            System.out.print("Enter property ID to review: ");
            int propertyId = Integer.parseInt(scanner.nextLine());
            if (propertyId <= 0) throw new ValidationException("Property ID must be positive.");

            System.out.print("Enter rating (0.0 - 5.0): ");
            double rating = Double.parseDouble(scanner.nextLine());
            if (rating < 0.0 || rating > 5.0) throw new ValidationException("Rating must be between 0.0 and 5.0.");

            System.out.print("Enter comment: ");
            String comment = scanner.nextLine();
            if (comment.isEmpty()) throw new ValidationException("Comment cannot be empty.");

            controller.leaveReview(guest, propertyId, rating, comment);
        } catch (ValidationException e) {
            System.out.println("Validation Error: " + e.getMessage());
        }
    }

    private void filterPropertiesByLocation() {
        try {
            System.out.print("Enter city: ");
            String city = scanner.nextLine();
            if (city.isEmpty()) throw new ValidationException("City cannot be empty.");

            System.out.print("Enter country: ");
            String country = scanner.nextLine();
            if (country.isEmpty()) throw new ValidationException("Country cannot be empty.");

            Location location = new Location(city, country);
            controller.filterPropertiesByLocation(location);
        } catch (ValidationException e) {
            System.out.println("Validation Error: " + e.getMessage());
        }
    }

    private void viewPropertiesByDate() {
        try {
            System.out.print("Enter date (YYYY-MM-DD): ");
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(scanner.nextLine());
            controller.viewAllPropertiesByDate(date);
        } catch (Exception e) {
            System.out.println("Invalid date format. Please enter in YYYY-MM-DD format.");
        }
    }

    private void payForBooking(Guest guest) {
        try {
            List<Booking> unpaidBookings = controller.getUnpaidBookingsForGuest(guest.getId());

            if (unpaidBookings.isEmpty()) {
                System.out.println("No unpaid bookings found.");
                return;
            }

            System.out.println("Select a booking to pay for:");
            for (int i = 0; i < unpaidBookings.size(); i++) {
                Booking booking = unpaidBookings.get(i);
                System.out.println((i + 1) + ". Booking ID: " + booking.getId() + ", Property ID: " + booking.getPropertyID() + ", Total Price: " + booking.getTotalPrice());
            }

            System.out.print("Enter booking number: ");
            int bookingIndex = Integer.parseInt(scanner.nextLine()) - 1;

            if (bookingIndex < 0 || bookingIndex >= unpaidBookings.size()) {
                throw new ValidationException("Invalid booking number.");
            }

            Booking selectedBooking = unpaidBookings.get(bookingIndex);
            controller.processPaymentForBooking(selectedBooking.getId());
            System.out.println("Payment processed successfully for Booking ID: " + selectedBooking.getId());
        } catch (ValidationException e) {
            System.out.println("Validation Error: " + e.getMessage());
        }
    }

    private void viewAvailablePropertiesByDateSortedByPrice() {
        try {
            System.out.print("Enter date (YYYY-MM-DD): ");
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(scanner.nextLine());
            controller.listAvailablePropertiesByDateSortedByPrice(date);
        } catch (Exception e) {
            System.out.println("Invalid date format. Please enter in YYYY-MM-DD format.");
        }
    }
}