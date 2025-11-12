package com.example.project_reactnative.service;

import com.example.project_reactnative.model.dto.request.BookingRequest;
import com.example.project_reactnative.model.dto.response.BookingResponse;
import com.example.project_reactnative.model.entity.Booking;
import com.example.project_reactnative.model.entity.Status;

import java.util.List;

public interface BookingService {
    BookingResponse booking(BookingRequest bookingRequest);

    List<BookingResponse> getBookingsByStatus(Long userId, Status status);
    List<BookingResponse> getBookingsByRoomId(Long roomId, Status status);

    BookingResponse confirmBooking(Long bookingId);

    BookingResponse cancelBooking(Long bookingId);
}
