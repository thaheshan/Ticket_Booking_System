package com.thaheshan.Booking.Controller;

import com.thaheshan.Booking.Entity.Bookings;
import com.thaheshan.Booking.Service.BookingService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;




@NoArgsConstructor(force = true)
@Setter
@Getter

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Endpoint to create a new booking
    @PostMapping
    public Bookings createBooking(@RequestBody Bookings bookings) {
        return bookingService.saveBooking(bookings);  // Create booking and save it using the service
    }

    // Endpoint for vendors to add tickets to the pool
    @PostMapping("/add-ticket")
    public String addTicket(@RequestParam String ticket) {
        bookingService.addTicket(ticket);
        return "Ticket added: " + ticket;
    }

    // Endpoint for customers to purchase tickets
    @GetMapping("/purchase-ticket")
    public String purchaseTicket() {
        String ticket = bookingService.purchaseTicket();
        if (ticket != null) {
            return "Ticket purchased: " + ticket;
        } else {
            return "No tickets available for purchase.";
        }
    }

    // Endpoint to get current ticket status
    @GetMapping("/ticket-status")
    public String getTicketStatus() {
        return bookingService.getTicketStatus();
    }
}
