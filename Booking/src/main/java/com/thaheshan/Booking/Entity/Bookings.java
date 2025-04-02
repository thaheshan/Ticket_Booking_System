package com.thaheshan.Booking.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@Entity
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    @Column(nullable = false)
    private LocalDate bookingDate;

    private String ticketId;  // Field for ticket information

    // No-argument constructor
    public Bookings() {}

    // Constructor with parameters
    public Bookings(String customerName, LocalDate bookingDate, String ticketId) {
        this.customerName = customerName;
        this.bookingDate = bookingDate;
        this.ticketId = ticketId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    // Override toString for better logging
    @Override
    public String toString() {
        return "Booking{id=" + id + ", customerName='" + customerName + "', bookingDate=" + bookingDate + ", ticketId='" + ticketId + "'}";
    }
}
