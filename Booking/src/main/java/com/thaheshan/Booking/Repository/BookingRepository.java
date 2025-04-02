package com.thaheshan.Booking.Repository;

import com.thaheshan.Booking.Entity.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Bookings, Long> {
    // Custom query methods can be added if needed
}
