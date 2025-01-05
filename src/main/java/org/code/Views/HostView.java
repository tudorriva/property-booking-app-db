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
                case 3 -> controller.viewPaymentsForHost(host);
                case 4 -> controller.viewTransactionHistoryForHost(host);
                case 5 -> controller.showPropertiesForHost(host);
                case 6 -> deleteProperty(host);
                case 7 -> sortReviewsForHostProperties(host);
                case 0 -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void showHostMenu() {
        System.out.println("\nHost Menu:");
        System.out.println("1. List a Property");
        System.out.println("2. Add an Amenity to Property");
        System.out.println("3. View Payments Received");
        System.out.println("4. View Transaction History");
        System.out.println("5. Show Properties Managed by Host");
        System.out.println("6. Delete a Property");
        System.out.println("7. Sort and View Reviews for Properties");
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

            int id = HelperFunctions.randomId();
            Location location = new Location(id, city, country);

            System.out.print("Enter amenity name: ");
            String amenityName = scanner.nextLine();
            if (amenityName.isEmpty()) throw new ValidationException("Amenity name cannot be empty.");

            System.out.print("Enter amenity description: ");
            String amenityDescription = scanner.nextLine();
            if (amenityDescription.isEmpty()) throw new ValidationException("Amenity description cannot be empty.");

            int id2 = HelperFunctions.randomId();
            Amenity amenity = new Amenity(id2, amenityName, amenityDescription);

            System.out.print("Enter cancellation policy description: ");
            String cancellationPolicyDescription = scanner.nextLine();
            if (cancellationPolicyDescription.isEmpty()) throw new ValidationException("Cancellation policy description cannot be empty.");

            CancellationPolicy cancellationPolicy = controller.getCancellationPolicyByDescription(cancellationPolicyDescription);
            if (cancellationPolicy == null) {
                int id3 = HelperFunctions.randomId();
                cancellationPolicy = new CancellationPolicy(id3, cancellationPolicyDescription);
            }

            List<Integer> amenityIDs = List.of(amenity.getAmenityID());
            controller.listProperty(prop_id, host, address, pricePerNight, description, location, amenityIDs, cancellationPolicy);
        } catch (ValidationException e) {
            System.out.println("Validation Error: " + e.getMessage());
        }
    }

    private void addAmenity(Host host) {
        try {
            List<Property> properties = controller.getPropertiesForHost(host.getId());
            if (properties.isEmpty()) throw new ValidationException("No properties found for this host.");

            System.out.println("Select a property to add an amenity:");
            for (int i = 0; i < properties.size(); i++) {
                System.out.println((i + 1) + ". " + properties.get(i).getAddress());
            }
            System.out.print("Enter property number: ");
            int propertyIndex = Integer.parseInt(scanner.nextLine()) - 1;

            if (propertyIndex < 0 || propertyIndex >= properties.size()) throw new ValidationException("Invalid property number.");

            Property property = properties.get(propertyIndex);
            List<Amenity> existingAmenities = controller.getAmenitiesForProperty(property);
            List<Amenity> allAmenities = controller.getAllAmenities();

            System.out.println("Existing amenities for this property:");
            existingAmenities.forEach(amenity -> System.out.println(amenity.getName() + ": " + amenity.getDescription()));

            System.out.println("\nAmenities that can be added:");
            allAmenities.stream()
                    .filter(amenity -> !existingAmenities.contains(amenity))
                    .forEach(amenity -> System.out.println(amenity.getId() + ". " + amenity.getName() + ": " + amenity.getDescription()));

            System.out.print("\nEnter amenity ID to add or 0 to create a new one: ");
            int amenityId = Integer.parseInt(scanner.nextLine());

            if (amenityId == 0) {
                System.out.print("Enter new amenity name: ");
                String name = scanner.nextLine();
                if (name.isEmpty()) throw new ValidationException("Amenity name cannot be empty.");

                System.out.print("Enter new amenity description: ");
                String description = scanner.nextLine();
                if (description.isEmpty()) throw new ValidationException("Amenity description cannot be empty.");

                int newAmenityId = HelperFunctions.randomId();
                Amenity newAmenity = new Amenity(newAmenityId, name, description);
                controller.addAmenityToProperty(property, newAmenity);
            } else {
                Amenity amenity = controller.getAmenityById(amenityId);
                if (amenity != null && !existingAmenities.contains(amenity)) {
                    controller.addAmenityToProperty(property, amenity);
                } else {
                    throw new ValidationException("Invalid amenity ID or amenity already exists on this property.");
                }
            }
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
}