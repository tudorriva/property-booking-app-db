package org.code.Views;

import org.code.Controller.PropertyBookingController;
import org.code.Entities.*;
import org.code.Exceptions.ValidationException;
import org.code.Helpers.HelperFunctions;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class HostView {
    private final PropertyBookingController controller;
    private final Scanner scanner;
    private final Host host;

    public HostView(PropertyBookingController controller, Scanner scanner, Host host) {
        this.controller = controller;
        this.scanner = scanner;
        this.host = host;
    }

    public void run() {
        boolean running = true;
        while (running) {
            showHostMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> listProperty(host);
                case 2 -> addAmenity(host);
                case 3 -> viewPaymentsForHost();
                case 4 -> controller.showPropertiesForHost(host);
                case 5 -> deleteProperty(host);
                case 6 -> sortReviewsForHostProperties(host);
                case 0 -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        new LoginView(controller, scanner).run();
    }

    private void showHostMenu() {
        System.out.println("\nHost Menu:");
        System.out.println("1. List a Property");
        System.out.println("2. Add an Amenity to Property");
        System.out.println("3. View Payments for properties");
        System.out.println("4. Show Properties Managed by Host");
        System.out.println("5. Delete a Property");
        System.out.println("6. Sort and View Reviews for Properties");
        System.out.println("0. Go back");
        System.out.print("Choose an option: ");
    }

    private void listProperty(Host host) {
        try {
            int prop_id = HelperFunctions.randomId();
            System.out.print("Enter property address: ");
            String address = scanner.nextLine();
            if (address.isEmpty()) throw new ValidationException("Property address cannot be empty.");

            System.out.print("Enter price per night: ");
            double pricePerNight = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter property description: ");
            String description = scanner.nextLine();
            if (description.isEmpty()) throw new ValidationException("Property description cannot be empty.");

            System.out.print("Enter location city: ");
            String city = scanner.nextLine();
            if (city.isEmpty()) throw new ValidationException("City cannot be empty.");

            System.out.print("Enter location country: ");
            String country = scanner.nextLine();
            if (country.isEmpty()) throw new ValidationException("Country cannot be empty.");

            int locationId = HelperFunctions.randomId();
            Location location = new Location(locationId, city, country);

            // List available amenities
            List<Amenity> allAmenities = controller.getAllAmenities();
            System.out.println("Available amenities:");
            allAmenities.forEach(amenity -> System.out.println(amenity.getAmenityID() + ". " + amenity.getName() + ": " + amenity.getDescription()));

            System.out.print("Enter amenity ID to add or 0 to create a new one: ");
            int amenityId = Integer.parseInt(scanner.nextLine());

            Amenity amenity;
            if (amenityId == 0) {
                System.out.print("Enter new amenity name: ");
                String amenityName = scanner.nextLine();
                if (amenityName.isEmpty()) throw new ValidationException("Amenity name cannot be empty.");

                System.out.print("Enter new amenity description: ");
                String amenityDescription = scanner.nextLine();
                if (amenityDescription.isEmpty()) throw new ValidationException("Amenity description cannot be empty.");

                int newAmenityId = HelperFunctions.randomId();
                amenity = new Amenity(newAmenityId, amenityName, amenityDescription);
                controller.addAmenity(amenity);
            } else {
                amenity = controller.getAmenityById(amenityId);
                if (amenity == null) throw new ValidationException("Invalid amenity ID.");
            }

            // List available cancellation policies
            List<CancellationPolicy> allPolicies = controller.getAllCancellationPolicies();
            System.out.println("Available cancellation policies:");
            allPolicies.forEach(policy -> System.out.println(policy.getId() + ". " + policy.getDescription()));

            System.out.print("Enter cancellation policy ID to add or 0 to create a new one: ");
            int policyId = Integer.parseInt(scanner.nextLine());

            CancellationPolicy cancellationPolicy;
            if (policyId == 0) {
                System.out.print("Enter new cancellation policy description: ");
                String policyDescription = scanner.nextLine();
                if (policyDescription.isEmpty()) throw new ValidationException("Cancellation policy description cannot be empty.");

                int newPolicyId = HelperFunctions.randomId();
                cancellationPolicy = new CancellationPolicy(newPolicyId, policyDescription);
                controller.addCancellationPolicy(cancellationPolicy);
            } else {
                cancellationPolicy = controller.getCancellationPolicyById(policyId);
                if (cancellationPolicy == null) throw new ValidationException("Invalid cancellation policy ID.");
            }

            List<Integer> amenityIDs = List.of(amenity.getAmenityID());
            controller.listProperty(prop_id, host, address, pricePerNight, description, location, amenityIDs, cancellationPolicy);
        } catch (ValidationException e) {
            System.out.println("Validation Error: " + e.getMessage());
        }
    }

    private void addAmenity(Host host) {
        try {
            // List properties managed by the host
            List<Property> properties = controller.getPropertiesForHost(host.getId());
            if (properties.isEmpty()) {
                System.out.println("No properties found for this host.");
                return;
            }

            System.out.println("Select a property to add an amenity:");
            for (int i = 0; i < properties.size(); i++) {
                Property property = properties.get(i);
                System.out.println((i + 1) + ". " + property.getAddress());
            }

            int propertyIndex = Integer.parseInt(scanner.nextLine()) - 1;
            if (propertyIndex < 0 || propertyIndex >= properties.size()) {
                throw new ValidationException("Invalid property number.");
            }

            Property property = properties.get(propertyIndex);

            // List available amenities
            List<Amenity> allAmenities = controller.getAllAmenities();
            System.out.println("Available amenities:");
            allAmenities.forEach(amenity -> System.out.println(amenity.getAmenityID() + ". " + amenity.getName() + ": " + amenity.getDescription()));

            System.out.print("Enter amenity ID to add or 0 to create a new one: ");
            int amenityId = Integer.parseInt(scanner.nextLine());

            Amenity amenity;
            if (amenityId == 0) {
                System.out.print("Enter new amenity name: ");
                String amenityName = scanner.nextLine();
                if (amenityName.isEmpty()) throw new ValidationException("Amenity name cannot be empty.");

                System.out.print("Enter new amenity description: ");
                String amenityDescription = scanner.nextLine();
                if (amenityDescription.isEmpty()) throw new ValidationException("Amenity description cannot be empty.");

                int newAmenityId = HelperFunctions.randomId();
                amenity = new Amenity(newAmenityId, amenityName, amenityDescription);
                controller.addAmenity(amenity);
            } else {
                amenity = controller.getAmenityById(amenityId);
                if (amenity == null) throw new ValidationException("Invalid amenity ID.");
            }

            controller.addAmenityToProperty(property, amenity);
            System.out.println("Amenity added successfully.");
        } catch (ValidationException e) {
            System.out.println("Validation Error: " + e.getMessage());
        }
    }

    private void deleteProperty(Host host) {
        try {
            List<Property> properties = controller.getPropertiesForHost(host.getId());
            if (properties.isEmpty()) throw new ValidationException("No properties found for this host.");

            System.out.println("Select a property to delete:");
            for (int i = 0; i < properties.size(); i++) {
                System.out.println((i + 1) + ". " + properties.get(i).getAddress());
            }
            System.out.print("Enter property number: ");
            int propertyIndex = Integer.parseInt(scanner.nextLine()) - 1;

            if (propertyIndex < 0 || propertyIndex >= properties.size()) throw new ValidationException("Invalid property number.");

            Property property = properties.get(propertyIndex);
            controller.deleteProperty(property.getId());
        } catch (ValidationException e) {
            System.out.println("Validation Error: " + e.getMessage());
        }
    }

    private void sortReviewsForHostProperties(Host host) {
        try {
            List<Property> properties = controller.getPropertiesForHost(host.getId());
            if (properties.isEmpty()) throw new ValidationException("No properties found for this host.");

            System.out.println("Select a property to view and sort reviews:");
            for (int i = 0; i < properties.size(); i++) {
                System.out.println((i + 1) + ". " + properties.get(i).getAddress());
            }
            System.out.print("Enter property number: ");
            int propertyIndex = Integer.parseInt(scanner.nextLine()) - 1;

            if (propertyIndex < 0 || propertyIndex >= properties.size()) throw new ValidationException("Invalid property number.");

            Property property = properties.get(propertyIndex);
            List<Review> reviews = controller.getReviewsForProperty(property);

            if (reviews.isEmpty()) throw new ValidationException("No reviews found for this property.");

            reviews.sort(Comparator.comparingDouble(Review::getRating).reversed());
            System.out.println("\nReviews sorted by rating (highest to lowest):");
            reviews.forEach(review -> System.out.println(
                    "Rating: " + review.getRating() +
                            ", Comment: " + review.getComment() +
                            ", Date: " + review.getDate()));
        } catch (ValidationException e) {
            System.out.println("Validation Error: " + e.getMessage());
        }
    }
    private void viewPaymentsForHost() {
        try {
            List<Payment> allPayments = controller.getAllPaymentsForHost(host.getId());

            if (allPayments.isEmpty()) {
                System.out.println("No payments found.");
                return;
            }

            System.out.println("Payments for host:");
            for (Payment payment : allPayments) {
                System.out.println("Payment ID: " + payment.getPaymentID() + ", Amount: " + payment.getAmount() + ", Date: " + payment.getDate() + ", Processed: " + payment.isProcessed());
            }
        } catch (ValidationException e) {
            System.out.println("Validation Error: " + e.getMessage());
        }
    }

}