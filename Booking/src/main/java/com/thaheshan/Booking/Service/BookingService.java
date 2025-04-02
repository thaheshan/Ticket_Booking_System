package com.thaheshan.Booking.Service;

import com.thaheshan.Booking.Entity.Bookings;
import com.thaheshan.Booking.Repository.BookingRepository;
import com.thaheshan.Booking.WebSocket.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final WebSocketHandler webSocketHandler;

    // Thread-safe ticket pool
    private final ConcurrentLinkedQueue<String> ticketQueue = new ConcurrentLinkedQueue<>();
    private final int maxTickets = 15;  // Max ticket capacity

    //we can use the repository interface implementation with using the keyword of Autowired
    @Autowired
    public BookingService(BookingRepository bookingRepository, WebSocketHandler webSocketHandler) {
        this.bookingRepository = bookingRepository;
        this.webSocketHandler = webSocketHandler;
    }

    // Method to add tickets to the pool (Vendor operation)
    public synchronized void addTicket(String ticket) {
        if (ticketQueue.size() < maxTickets) {
            ticketQueue.add(ticket);
            webSocketHandler.sendLog("Ticket added to the pool: " + ticket);
        } else {
            webSocketHandler.sendLog("Ticket pool is full. Cannot add more tickets.");
        }
    }

    // Method for customers to purchase tickets
    public synchronized String purchaseTicket() {
        String ticket = ticketQueue.poll();  // Removes the first ticket from the queue
        if (ticket != null) {
            webSocketHandler.sendLog("Ticket purchased: " + ticket);
            return ticket;
        } else {
            webSocketHandler.sendLog("No tickets available for purchase.");
            return null;
        }
    }

    // Method to get current ticket status
    public synchronized String getTicketStatus() {
        return "Current Tickets: " + ticketQueue.size() + " / Max Capacity: " + maxTickets;
    }

    @Transactional
    public Bookings saveBooking(Bookings bookings) {
        // Simulating progress messages for booking processing
        webSocketHandler.sendLog("Booking processing started.");

        // Simulate assigning a ticket to the booking (if a ticket is available)
        String ticketId = purchaseTicket();
        if (ticketId != null) {
            bookings.setTicketId(ticketId);  // Set the ticketId for the booking
        } else {
            webSocketHandler.sendLog("No ticket available for this booking.");
        }

        // Save the booking
        Bookings savedBooking = bookingRepository.save(bookings);

        // Send a progress log for the successful booking
        webSocketHandler.sendLog("Booking saved successfully: " + savedBooking.getId());
        return savedBooking;
    }
}
