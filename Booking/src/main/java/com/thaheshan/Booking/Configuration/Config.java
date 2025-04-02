package com.thaheshan.Booking.Configuration;

import com.thaheshan.Booking.Models.Ticketpool;
import com.thaheshan.Booking.Models.Vendor;
import com.thaheshan.Booking.Models.Customer;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class Config {

    // Event and ticket pool configurations
    private static String eventName;
    private static String eventVenue;
    private static int maxTicketCapacity;
    private static int numCustomers;
    private static int numVIPCustomer;
    private static int numVendors;
    private static int customerPurchaseRate;
    private static int VIPcustomerPurchaseRate;
    private static int vendorInputRate;
    private static int maxTicketsPerCustomer;
    private static int maxTicketsPerVendor;
    private static int maxTicketsPerVIPCustomer;

    // Ticket pool to hold tickets, shared by customers and vendors
    private static Ticketpool ticketPool;

    // List of vendors and customers (assuming they are stored here)
    private static List<Vendor> vendors = new ArrayList<>(); // Initialize to prevent null pointer
    private static List<Customer> customers = new ArrayList<>(); // Initialize to prevent null pointer
    private static List<Customer> VIPcustomers = new ArrayList<>(); // Initialize to prevent null pointer

    // Lock to ensure thread safety while modifying the ticket pool
    private static final Lock ticketPoolLock = new ReentrantLock();

    // Initialize ticket pool with max capacity
    public static void initializeTicketPool() {
        ticketPool = new Ticketpool(maxTicketCapacity);
    }

    // Event configuration setters (existing ones)
    public static void setEventName(String eventName) {
        Config.eventName = eventName;
    }

    public static void setEventVenue(String eventVenue) {
        Config.eventVenue = eventVenue;
    }

    public static void setMaxTicketCapacity(int maxTicketCapacity) {
        Config.maxTicketCapacity = maxTicketCapacity;
    }

    public static void setNumCustomers(int numCustomers) {
        Config.numCustomers = numCustomers;
    }

    public static void setNumVIPCustomers(int numVIPCustomer) {
        Config.numVIPCustomer = numVIPCustomer;
    }

    public static void setNumVendors(int numVendors) {
        Config.numVendors = numVendors;
    }

    public static void setCustomerPurchaseRate(int customerPurchaseRate) {
        Config.customerPurchaseRate = customerPurchaseRate;
    }

    public static void setVIPCustomerPurchaseRate(int VIPcustomerPurchaseRate) {
        Config.VIPcustomerPurchaseRate = VIPcustomerPurchaseRate;
    }

    public static void setVendorInputRate(int vendorInputRate) {
        Config.vendorInputRate = vendorInputRate;
    }

    public static void setMaxTicketsPerCustomer(int maxTicketsPerCustomer) {
        Config.maxTicketsPerCustomer = maxTicketsPerCustomer;
    }

    public static void setMaxTicketsPerVIPCustomer(int maxTicketsPerVIPCustomerCustomer) {
        Config.maxTicketsPerVIPCustomer = maxTicketsPerVIPCustomer;
    }

    public static void setMaxTicketsPerVendor(int maxTicketsPerVendor) {
        Config.maxTicketsPerVendor = maxTicketsPerVendor;
    }

    // Getter methods for the configuration fields (existing ones)
    public static String getEventName() {
        return eventName;
    }

    public static String getEventVenue() {
        return eventVenue;
    }

    public static int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public static int getNumCustomers() {
        return numCustomers;
    }

    public static int getNumVIPCustomers() {
        return numVIPCustomer;
    }

    public static int getNumVendors() {
        return numVendors;
    }

    public static int getCustomerPurchaseRate() {
        return customerPurchaseRate;
    }

    public static int getVIPCustomerPurchaseRate() {
        return VIPcustomerPurchaseRate;
    }

    public static int getVendorInputRate() {
        return vendorInputRate;
    }

    public static int getMaxTicketsPerCustomer() {
        return maxTicketsPerCustomer;
    }

    public static int getMaxTicketsPerVIPCustomer() {
        return maxTicketsPerVIPCustomer;
    }

    public static int getMaxTicketsPerVendor() {
        return maxTicketsPerVendor;
    }

    // Getter for the ticket pool (the shared resource)
    public static Ticketpool getTicketPool() {
        return ticketPool;
    }

    // Method to check if the ticket pool is full (existing method)
    public static boolean isTicketPoolFull() {
        return ticketPool.size() >= maxTicketCapacity;
    }

    // Method to add a ticket to the pool (existing method)
    public static String addTicket(int vendorId) {
        return ticketPool.addTicket(vendorId);
    }

    // Getter methods for vendors and customers (updated to avoid returning null)
    public static List<Vendor> getVendors() {
        if (vendors == null) {
            vendors = new ArrayList<>(); // Initialize to avoid null
        }
        return vendors;
    }

    public static List<Customer> getCustomers() {
        if (customers == null) {
            customers = new ArrayList<>(); // Initialize to avoid null
        }
        return customers;
    }

    public static List<Customer> getVIPCustomers() {
        if (VIPcustomers == null) {
            VIPcustomers = new ArrayList<>();
        }
        return VIPcustomers;
    }

    // Method to generate logs for vendors and customers (updated)
    public static void generateLogs(StringBuilder vendorLog, StringBuilder customerLog , StringBuilder VIPCustomerLog) {
        // Append vendor logs
        if (vendors != null && !vendors.isEmpty()) {
            vendors.forEach(vendor -> vendorLog.append(vendor.getLog()).append("\n"));
        }

        // Append customer logs
        if (customers != null && !customers.isEmpty()) {
            customers.forEach(customer -> customerLog.append(customer.getLog()).append("\n"));
        }

        if (VIPcustomers != null && !VIPcustomers.isEmpty()) {
            VIPcustomers.forEach(customer -> VIPCustomerLog.append(customer.getLog()).append("\n"));
        }
    }

    // Method to handle VIP customer ticket purchase with synchronization
    public static void purchaseVIPTicket(int vipCustomerId, StringBuilder log) {
        // Lock the ticket pool to ensure thread safety
        ticketPoolLock.lock();
        try {
            if (!ticketPool.isEmpty()) {
                String ticket = ticketPool.pollForVIPCustomer(); // FIFO order
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String buyMessage = "VIPCustomer-" + vipCustomerId + " bought " + ticket + " at " + timestamp + " for " + getEventName() + " at " + getEventVenue();

                System.out.println(buyMessage); // Print purchase message
                log.append(buyMessage).append("\n"); // Append to log

                ticketPool.notifyAll(); // Notify waiting threads
            } else {
                System.out.println("No tickets available for VIPCustomer-" + vipCustomerId);
            }
        } finally {
            ticketPoolLock.unlock(); // Release the lock
        }
    }
}
