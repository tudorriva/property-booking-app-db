package org.code.Views;

import org.code.Controller.PropertyBookingController;
import org.code.Entities.Guest;
import org.code.Entities.Host;
import org.code.Exceptions.ValidationException;

import java.util.Scanner;

public class LoginView {
    private final PropertyBookingController controller;
    private final Scanner scanner;

    public LoginView(PropertyBookingController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    public void run() {
        System.out.print("Are you logging in as a Guest or Host? (G/H): ");
        String userType = scanner.nextLine().toUpperCase();

        System.out.println("Login Page:");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        if (email.equals("admin")) {
            new AdminView(controller, scanner).run();
            return;
        }

        try {
            if (userType.equals("G")) {
                Guest guest = controller.getAllGuests().stream()
                        .filter(g -> g.getEmail().equals(email))
                        .findFirst()
                        .orElseThrow(() -> new ValidationException("Invalid email."));
                new GuestView(controller, scanner, guest).run();
            } else if (userType.equals("H")) {
                Host host = controller.getAllHosts().stream()
                        .filter(h -> h.getEmail().equals(email))
                        .findFirst()
                        .orElseThrow(() -> new ValidationException("Invalid email."));
                new HostView(controller, scanner, host).run();
            } else {
                System.out.println("Invalid user type. Please enter 'G' for Guest or 'H' for Host.");
            }
        } catch (ValidationException e) {
            System.out.println("Login Error: " + e.getMessage());
        }
    }
}