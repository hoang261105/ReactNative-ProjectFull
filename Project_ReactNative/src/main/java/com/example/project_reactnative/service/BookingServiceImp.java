package com.example.project_reactnative.service;

import com.example.project_reactnative.model.dto.request.BookingRequest;
import com.example.project_reactnative.model.dto.response.BookingResponse;
import com.example.project_reactnative.model.entity.*;
import com.example.project_reactnative.repository.*;
import com.example.project_reactnative.security.exception.CustomValidationException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;

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

        Room room = roomRepository.findById(bookingRequest.getRoomId())
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy phòng!"));
        User user = authRepository.findById(bookingRequest.getUserId())
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy user!"));
        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelId())
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy khách sạn!"));
        PaymentMethod paymentMethod = paymentMethodRepository.findById(bookingRequest.getPaymentMethodId())
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy phuơng thức thanh toán!"));

        LocalDate checkIn = bookingRequest.getCheckInDate();
        LocalDate checkOut = bookingRequest.getCheckOutDate();

        int daysBetween = Period.between(checkIn, checkOut).getDays();
        if (daysBetween <= 0) {
            errors.put("errorDate", "Ngày nhận phòng phải nhỏ hơn ngày trả phòng!");
            throw new CustomValidationException(errors);
        }

        // --- Kiểm tra xem có booking CONFIRMED nào trùng phòng không ---
        boolean conflict = bookingRepository.existsByRoomAndStatusAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                room,
                Status.CONFIRMED,
                checkOut,
                checkIn
        );

        if (conflict) {
            errors.put("conflictBooking", "Phòng này đã được đặt trong khoảng thời gian bạn chọn!");
            throw new CustomValidationException(errors);
        }

        BigDecimal totalPrice = room.getPrice().multiply(BigDecimal.valueOf(daysBetween));

        Booking booking = Booking.builder()
                .id((long) Math.ceil(Math.random() * 10000000))
                .checkInDate(checkIn)
                .checkOutDate(checkOut)
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

    @Override
    public List<BookingResponse> getBookingsByRoomId(Long roomId, Status status) {
        List<Booking> bookings = bookingRepository.getBookingsByRoomIdAndStatus(roomId, status);
        return bookings.stream().map(this::convertToResponse).toList();
    }

    public BookingResponse confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException("Booking không tồn tại!"));

        // Lấy tất cả booking trùng phòng đang PENDING
        List<Booking> pendingOverlaps = bookingRepository
                .findAllByRoomAndStatusInAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                        booking.getRoom(),
                        List.of(Status.PENDING, Status.CONFIRMED),
                        booking.getCheckOutDate(),
                        booking.getCheckInDate()
                );

        // Nếu có booking CONFIRMED overlap → báo lỗi
        boolean conflict = pendingOverlaps.stream()
                .anyMatch(b -> b.getStatus() == Status.CONFIRMED && !b.getId().equals(bookingId));

        if (conflict) {
            throw new CustomValidationException(Map.of("errorBooking", "Phòng đã được đặt trong khoảng thời gian này!"));
        }

        // Confirm booking hiện tại
        booking.setStatus(Status.CONFIRMED);
        bookingRepository.save(booking);

        // Hủy tất cả PENDING overlap
        pendingOverlaps.stream()
                .filter(b -> b.getStatus() == Status.PENDING && !b.getId().equals(bookingId))
                .forEach(b -> {
                    b.setStatus(Status.CANCELLED);
                    bookingRepository.save(b);
                });

        return convertToResponse(booking);
    }

    @Override
    public BookingResponse cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NoSuchElementException("Không tìm thấy booking!"));

        if (booking.getStatus() == Status.CONFIRMED) {
            throw new CustomValidationException(Map.of("errorCanceled", "Đơn này ở trạng thái đã đặt, không thể hủy!"));
        } else if (booking.getStatus() == Status.CANCELLED) {
            throw new CustomValidationException(Map.of("errorCanceled", "Đơn này ở trạng thái đã hủy, không thể hủy!"));
        } else if (booking.getStatus() == Status.CHECKED_OUT) {
            throw new CustomValidationException(Map.of("errorCanceled", "Đơn này ở trạng thái đã trả phòng, không thể hủy!"));
        }

        booking.setStatus(Status.CANCELLED);
        Booking saved = bookingRepository.save(booking);
        return convertToResponse(saved);
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
