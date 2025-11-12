package com.example.project_reactnative.repository;

import com.example.project_reactnative.model.entity.Booking;
import com.example.project_reactnative.model.entity.Room;
import com.example.project_reactnative.model.entity.Status;
import com.example.project_reactnative.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> getBookingsByUserIdAndStatus(Long userId, Status status);
    List<Booking> getBookingsByRoomIdAndStatus(Long roomId, Status status);

    boolean existsByRoomAndStatusAndCheckInDateLessThanAndCheckOutDateGreaterThan(
            Room room,
            Status status,
            LocalDate checkOut,
            LocalDate checkIn
    );

    List<Booking> findAllByRoomAndStatusInAndCheckInDateLessThanAndCheckOutDateGreaterThan(
            Room room,
            List<Status> statuses,
            LocalDate checkOutDate,
            LocalDate checkInDate
    );

}
