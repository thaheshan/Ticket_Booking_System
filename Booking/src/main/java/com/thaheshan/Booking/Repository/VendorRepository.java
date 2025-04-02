package com.thaheshan.Booking.Repository;


import com.thaheshan.Booking.Booking;

import com.thaheshan.Booking.Entity.Vendors;
import com.thaheshan.Booking.Models.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.thaheshan.Booking.Repository.VendorRepository")


public interface VendorRepository extends JpaRepository<Vendors, Long> {

}