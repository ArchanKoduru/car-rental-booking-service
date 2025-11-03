package com.xyz.carrental.booking.repository;

import com.xyz.carrental.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, String> {
}

