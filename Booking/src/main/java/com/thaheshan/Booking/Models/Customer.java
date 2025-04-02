package com.thaheshan.Booking.Models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thaheshan.Booking.Booking;
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
@Table(name = "Customer")
@Data
public class Customer implements Runnable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;

    private int ticketsBought;

    @Transient // This field is not persisted in the database
    private StringBuilder log;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String LOG_FILE = "thread_log.json";

    public Customer(int customerId) {
        this.customerId = customerId;
        this.ticketsBought = 0;
        this.log = new StringBuilder();
    }

    public Customer() {
        this.log = new StringBuilder();
    }

    @Override
    public void run() {
        synchronized (Config.getTicketPool()) {
            while (ticketsBought < Config.getMaxTicketsPerCustomer()) {
                if (Config.getTicketPool().isEmpty()) {
                    String waitMessage = "Customer-" + customerId + " is waiting: Ticket pool is empty.";
                    System.out.println(waitMessage);
                    log.append(waitMessage).append("\n");
                    try {
                        Config.getTicketPool().wait();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (!Config.getTicketPool().isEmpty()) {
                    String ticket = Config.getTicketPool().pollForCustomer();
                    ticketsBought++;
                    String timestamp = LocalDateTime.now().format(formatter);
                    String buyMessage = "Customer-" + customerId + " bought " + ticket + " at " + timestamp + " for " + Config.getEventName() + " at " + Config.getEventVenue();
                    System.out.println(buyMessage);
                    log.append(buyMessage).append("\n");
                    Config.getTicketPool().notifyAll();
                }

                try {
                    Thread.sleep(1000 / Config.getCustomerPurchaseRate());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        String finishMessage = "Customer-" + customerId + " finished purchasing tickets for" + Config.getEventName() + " at " + Config.getEventVenue() +"." ;

        System.out.println(finishMessage);
        log.append(finishMessage).append("\n");

        saveLogToJson();
    }

    public String getLog() {
        return log.toString();
    }

    private void saveLogToJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(LOG_FILE);
        Map<String, Object> data = new HashMap<>();

        if (file.exists()) {
            try {
                data = objectMapper.readValue(file, Map.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        data.put("Customer-" + customerId, log.toString());

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
