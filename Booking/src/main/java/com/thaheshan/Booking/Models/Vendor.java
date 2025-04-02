package com.thaheshan.Booking.Models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thaheshan.Booking.Configuration.Config;
import jakarta.persistence.*;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "Vendor")
@Data
public class Vendor implements Runnable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int vendorId;

    private int ticketsAdded;

    @Transient // This field is not persisted in the database
    private StringBuilder log;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String LOG_FILE = "thread_log.json";

    // Constructor for manual creation with logging
    public Vendor(int vendorId) {
        this.vendorId = vendorId;
        this.ticketsAdded = 0;
        this.log = new StringBuilder();
    }

    // Default constructor for JPA
    public Vendor() {
        this.log = new StringBuilder();
    }

    @Override
    public void run() {
        synchronized (Config.getTicketPool()) {
            while (ticketsAdded < Config.getMaxTicketsPerVendor()) {
                // Check if the ticket pool is full
                if (Config.isTicketPoolFull()) {
                    String waitMessage = "Vendor-" + vendorId + " is waiting: Ticket pool is full.";
                    System.out.println(waitMessage);
                    log.append(waitMessage).append("\n");
                    try {
                        Config.getTicketPool().wait(); // Wait for space in ticket pool
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Add tickets to the pool if space is available
                if (Config.getTicketPool().size() < Config.getMaxTicketCapacity()) {
                    String ticket = Config.addTicket(vendorId); // Add a ticket
                    ticketsAdded++;
                    String timestamp = LocalDateTime.now().format(formatter);
                    String addMessage = "Vendor-" + vendorId + " added " + ticket + " at " + timestamp + " for " + Config.getEventName() + " at " + Config.getEventVenue();
                    System.out.println(addMessage);
                    log.append(addMessage).append("\n");
                    Config.getTicketPool().notifyAll(); // Notify waiting threads
                }

                // Simulate vendor input rate
                try {
                    Thread.sleep(1000 / Config.getVendorInputRate());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // Log the finish message when done
        String finishMessage = "Vendor-" + vendorId + " finished adding tickets."+ " for " + Config.getEventName() + " at " + Config.getEventVenue();
        System.out.println(finishMessage);
        log.append(finishMessage).append("\n");

        // Save log to JSON after finishing
        saveLogToJson();
    }

    // Getter for the log content
    public String getLog() {
        return log.toString();
    }

    /**
     * Save the vendor log to a JSON file.
     */
    private void saveLogToJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(LOG_FILE);
        Map<String, Object> data = new HashMap<>();

        // Read existing JSON data
        if (file.exists()) {
            try {
                data = objectMapper.readValue(file, Map.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Add current vendor log, but avoid null entries
        if (log != null && log.length() > 0) {
            data.put("Vendor-" + vendorId, log.toString());
        }

        // Write the updated JSON data
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
