package com.example.project_reactnative.service;

import com.example.project_reactnative.model.dto.request.BookingRequest;
import com.example.project_reactnative.model.dto.response.BookingResponse;
import com.example.project_reactnative.model.entity.*;
import com.example.project_reactnative.repository.*;
import com.example.project_reactnative.security.exception.CustomValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class BookingServiceImp implements BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private RoomImageRepository roomImageRepository;

    @Override
    public BookingResponse booking(BookingRequest bookingRequest) {
        Map<String, String> errors = new HashMap<>();

        Room room = roomRepository.findById(bookingRequest.getRoomId()).orElseThrow(() -> new NoSuchElementException("Không tìm thấy phòng!"));
        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelId()).orElseThrow(() -> new NoSuchElementException("Không tìm thấy khách sạn!"));
        User user = authRepository.findById(bookingRequest.getUserId()).orElseThrow(() -> new NoSuchElementException("Không tìm thấy user!"));
        PaymentMethod paymentMethod = paymentMethodRepository.findById(bookingRequest.getPaymentMethodId()).orElseThrow(() -> new NoSuchElementException("Không tìm thấy phương thức thanh toán!"));

        LocalDate checkIn = bookingRequest.getCheckInDate();
        LocalDate checkOut = bookingRequest.getCheckOutDate();
        int daysBetween = Period.between(checkIn, checkOut).getDays();
        if (daysBetween <= 0) {
            errors.put("errorDate", "Ngày nhận phòng phải nhỏ hơn ngày trả phòng!");
            throw new CustomValidationException(errors);
        }

        BigDecimal totalPrice = room.getPrice().multiply(BigDecimal.valueOf(daysBetween));

        Booking booking = Booking.builder()
                .id((long) Math.ceil(Math.random() * 10000000))
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .room(room)
                .hotel(hotel)
                .user(user)
                .totalPrice(totalPrice)
                .adults(bookingRequest.getAdults())
                .children(bookingRequest.getChildren())
                .infants(bookingRequest.getInfants())
                .paymentOption(bookingRequest.getPaymentOption())
                .paymentMethod(paymentMethod)
                .status(Status.PENDING)
                .build();

        Booking savedBooking = bookingRepository.save(booking);
        return convertToResponse(savedBooking);
    }

    @Override
    public List<BookingResponse> getBookingsByStatus(Long userId, Status status) {
        List<Booking> bookings = bookingRepository.getBookingsByUserIdAndStatus(userId, status);
        return bookings.stream().map(this::convertToResponse).toList();
    }

    public BookingResponse convertToResponse(Booking booking) {
        if (booking == null) return null;

        List<RoomImage> roomImages = roomImageRepository.findByRoomId(booking.getRoom().getId());

        return new BookingResponse(
                booking.getId(),
                booking.getRoom().getId(),
                booking.getHotel().getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getRoom().getTitle(),
                booking.getHotel().getHotelName(),
                roomImages.get(0).getImageUrl(),
                booking.getStatus()
        );
    }
}
