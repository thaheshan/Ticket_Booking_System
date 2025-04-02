package com.thaheshan.Booking;

import com.thaheshan.Booking.Configuration.Config;
import com.thaheshan.Booking.Models.Customer;
import com.thaheshan.Booking.Models.Vendor;
import com.thaheshan.Booking.Models.VIPCustomer;
import jakarta.persistence.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

@EnableJpaRepositories(basePackages = "com.thaheshan.Booking.Repository")
@EntityScan(basePackages = "com.thaheshan.Booking")
@Table(name = "booking")
public class Booking {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id

    public void runMenu() throws JSONException {
        boolean running = true;
        while (running) {
            System.out.println("********** Ticket System Menu **********");
            System.out.println("1. Start System");
            System.out.println("2. Exit");

            int option = getValidatedMenuOption();

            if (option == 1) {
                getConfigurationsFromUser();
                startTicketSystem();
            } else if (option == 2) {
                System.out.println("Successfully exited.");
                running = false;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    public int getValidatedMenuOption() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Please select an option (1 for Start, 2 for Exit): ");
            if (scanner.hasNextInt()) {
                int option = scanner.nextInt();
                if (option == 1 || option == 2) {
                    return option;
                }
            }
            System.out.println("Invalid input. Please enter 1 or 2.");
            scanner.nextLine();
        }
    }

    public void getConfigurationsFromUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter event name: ");
        String eventName = scanner.nextLine();
        Config.setEventName(eventName);
        storeLog("Event Name: " + eventName);

        System.out.print("Enter event venue: ");
        String eventVenue = scanner.nextLine();
        Config.setEventVenue(eventVenue);
        storeLog("Event Venue: " + eventVenue);

        System.out.print("Enter maximum ticket capacity: ");
        int maxTicketCapacity = getValidatedIntegerInput();
        Config.setMaxTicketCapacity(maxTicketCapacity);
        storeLog("Max Ticket Capacity: " + maxTicketCapacity);

        System.out.print("Enter number of VIP customers: ");
        int numVIPCustomers = getValidatedIntegerInput();
        Config.setNumVIPCustomers(numVIPCustomers);
        storeLog("Number of VIP Customers: " + numVIPCustomers);

        System.out.print("Enter number of customers: ");
        int numCustomers = getValidatedIntegerInput();
        Config.setNumCustomers(numCustomers);
        storeLog("Number of Customers: " + numCustomers);

        System.out.print("Enter number of vendors: ");
        int numVendors = getValidatedIntegerInput();
        Config.setNumVendors(numVendors);
        storeLog("Number of Vendors: " + numVendors);

        System.out.print("Enter customer purchase rate (tickets per second): ");
        int customerPurchaseRate = getValidatedIntegerInput();
        Config.setCustomerPurchaseRate(customerPurchaseRate);
        storeLog("Customer Purchase Rate: " + customerPurchaseRate);

        System.out.print("Enter vendor input rate (tickets per second): ");
        int vendorInputRate = getValidatedIntegerInput();
        Config.setVendorInputRate(vendorInputRate);
        storeLog("Vendor Input Rate: " + vendorInputRate);

        System.out.print("Enter maximum tickets per customer: ");
        int maxTicketsPerCustomer = getValidatedIntegerInput();
        Config.setMaxTicketsPerCustomer(maxTicketsPerCustomer);
        storeLog("Max Tickets Per Customer: " + maxTicketsPerCustomer);

        System.out.print("Enter maximum tickets per vendor: ");
        int maxTicketsPerVendor = getValidatedIntegerInput();
        Config.setMaxTicketsPerVendor(maxTicketsPerVendor);
        storeLog("Max Tickets Per Vendor: " + maxTicketsPerVendor);

        Config.initializeTicketPool();
    }

    public int getValidatedIntegerInput() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                if (value >= 0) {
                    return value;
                }
            }
            System.out.println("Invalid input. Please enter a non-negative integer.");
            scanner.nextLine();
        }
    }

    public void startTicketSystem() throws JSONException {
        Thread[] vendorThreads = new Thread[Config.getNumVendors()];
        Thread[] vipCustomerThreads = new Thread[Config.getNumVIPCustomers()];
        Thread[] customerThreads = new Thread[Config.getNumCustomers()];

        for (int i = 0; i < Config.getNumVendors(); i++) {
            Vendor vendor = new Vendor(i + 1);
            vendorThreads[i] = new Thread(vendor);
            vendorThreads[i].start();
        }

        for (int i = 0; i < Config.getNumVIPCustomers(); i++) {
            VIPCustomer vipCustomer = new VIPCustomer(i + 1);
            vipCustomerThreads[i] = new Thread(vipCustomer);
            vipCustomerThreads[i].start();
        }

        for (Thread thread : vipCustomerThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < Config.getNumCustomers(); i++) {
            Customer customer = new Customer(i + 1);
            customerThreads[i] = new Thread(customer);
            customerThreads[i].start();
        }

        for (Thread thread : vendorThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (Thread thread : customerThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        handleThreadLogging();
    }

    private void handleThreadLogging() throws JSONException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to save the thread logs? (yes/no)");
        String response = scanner.nextLine().trim().toLowerCase();

        if ("yes".equals(response)) {
            storeThreadLogsToJson();
            System.out.println("Logs saved successfully.");
        } else {
            System.out.println("Logs not saved.");
        }

        System.out.println("Returning to the menu...");
    }

    private void storeThreadLogsToJson() throws JSONException {
        StringBuilder vendorLog = new StringBuilder();
        StringBuilder vipCustomerLog = new StringBuilder();
        StringBuilder customerLog = new StringBuilder();

        JSONObject log = new JSONObject();
        log.put("vendorLog", vendorLog.toString());
        log.put("vipCustomerLog", vipCustomerLog.toString());
        log.put("customerLog", customerLog.toString());

        try (FileWriter writer = new FileWriter("thread_log.json")) {
            writer.write(log.toString());
        } catch (IOException e) {
            System.out.println("Error saving logs: " + e.getMessage());
        }
    }

    private void storeLog(String logEntry) {
        try (FileWriter writer = new FileWriter("log_output.txt", true)) {
            writer.append(logEntry).append("\n");
        } catch (IOException e) {
            System.out.println("Error writing log: " + e.getMessage());
        }
    }
}
