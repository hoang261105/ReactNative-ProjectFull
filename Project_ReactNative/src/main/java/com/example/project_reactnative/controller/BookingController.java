package com.example.project_reactnative.controller;

import com.example.project_reactnative.model.dto.request.BookingRequest;
import com.example.project_reactnative.model.dto.response.APIResponse;
import com.example.project_reactnative.model.dto.response.BookingDateRange;
import com.example.project_reactnative.model.dto.response.BookingResponse;
import com.example.project_reactnative.model.entity.Booking;
import com.example.project_reactnative.model.entity.Status;
import com.example.project_reactnative.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<APIResponse<BookingResponse>> addBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        return new ResponseEntity<>(new APIResponse<>(true, "Đặt phòng thành công", bookingService.booking(bookingRequest)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<BookingResponse>>> getBooking(@RequestParam(required = false) Long userId, @RequestParam(required = false) Status status) {
        return new ResponseEntity<>(new APIResponse<>(true, "Lọc trạng thái thành công!", bookingService.getBookingsByStatus(userId, status)), HttpStatus.OK);
    }

    @PatchMapping("/{bookingId}/confirm")
    public ResponseEntity<APIResponse<BookingResponse>> confirmBooking(@PathVariable Long bookingId) {
        BookingResponse confirmed = bookingService.confirmBooking(bookingId);
        return new ResponseEntity<>(
                new APIResponse<>(true, "Booking đã được xác nhận", confirmed),
                HttpStatus.OK
        );
    }

    @GetMapping("/room/{roomId}/booked-dates")
    public ResponseEntity<APIResponse<List<BookingDateRange>>> getBookedDates(@PathVariable Long roomId) {
        List<BookingResponse> bookings = bookingService.getBookingsByRoomId(roomId, Status.CONFIRMED);
        List<BookingDateRange> ranges = bookings.stream()
                .map(b -> new BookingDateRange(b.getCheckInDate(), b.getCheckOutDate()))
                .toList();
        return new ResponseEntity<>(new APIResponse<>(true, "Lấy danh sách thành công!", ranges), HttpStatus.OK);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<APIResponse<BookingResponse>> cancelBooking(@PathVariable Long id) {
        return new ResponseEntity<>(new APIResponse<>(true, "Hủy đặt phòng thành công!", bookingService.cancelBooking(id)), HttpStatus.OK);
    }
}
