package com.thaheshan.Booking.Repository;

import com.thaheshan.Booking.Models.Customer;
import com.thaheshan.Booking.Entity.Customers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customers, Long> {
    // Additional custom query methods can go here if needed
}
