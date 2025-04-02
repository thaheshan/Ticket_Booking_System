package com.thaheshan.Booking.Models;

import java.util.LinkedList;
import java.util.Queue;

public class Ticketpool {

    // Queue to hold the tickets (FIFO)
    private Queue<String> tickets;
    private int maxCapacity;

    public Ticketpool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.tickets = new LinkedList<>();
    }

    // Add a ticket to the pool (synchronized to handle concurrency)
    public synchronized String addTicket(int vendorId) {
        if (tickets.size() < maxCapacity) {
            String ticket = "TICKET-" + vendorId + "-" + (tickets.size() + 1);
            tickets.add(ticket); // Add the ticket to the queue
            notifyAll(); // Notify all waiting threads
            return ticket;
        }
        return null; // If the pool is full, return null
    }

    // Get the current number of tickets in the pool (synchronized)
    public synchronized int size() {
        return tickets.size();
    }

    // Method to remove a ticket for customers (standard customers)
    public synchronized String pollForCustomer() {
        while (tickets.isEmpty()) {
            try {
                System.out.println("Customer is waiting: Ticket pool is empty.");
                wait(); // Wait until a ticket is available
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        notifyAll(); // Notify vendors and other threads
        return tickets.poll(); // Remove and return the head of the queue
    }

    // Method to remove a ticket for VIP customers (priority access)
    public synchronized String pollForVIPCustomer() {
        while (tickets.isEmpty()) {
            try {
                System.out.println("VIPCustomer is waiting: Ticket pool is empty.");
                wait(); // Wait until a ticket is available
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        notifyAll(); // Notify vendors and other threads
        return tickets.poll(); // Remove and return the head of the queue
    }

    // Check if the pool is empty (synchronized)
    public synchronized boolean isEmpty() {
        return tickets.isEmpty();
    }

    // Method for vendors to notify all waiting threads when tickets are added
    public synchronized void notifyAllPool() {
        notifyAll(); // Notify all threads waiting on this object
    }

    // Optional: Method to add multiple tickets to the pool (useful for vendor input rate)
    public synchronized void addMultipleTickets(int vendorId, int numberOfTickets) {
        for (int i = 0; i < numberOfTickets; i++) {
            if (tickets.size() < maxCapacity) {
                String ticket = "TICKET-" + vendorId + "-" + (tickets.size() + 1);
                tickets.add(ticket); // Add ticket to the queue
            } else {
                break; // Stop if max capacity is reached
            }
        }
        System.out.println("Vendor added " + numberOfTickets + " tickets.");
        notifyAll(); // Notify all waiting threads
    }

    // Optional: Print the tickets in the pool (for debugging purposes)
    public synchronized void printTickets() {
        System.out.println("Tickets in pool: ");
        tickets.forEach(System.out::println);
    }
}
