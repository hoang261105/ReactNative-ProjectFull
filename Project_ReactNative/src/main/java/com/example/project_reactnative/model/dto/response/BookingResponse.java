package com.example.project_reactnative.model.dto.response;

import com.example.project_reactnative.model.entity.PaymentMethod;
import com.example.project_reactnative.model.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long bookingId;
    private Long roomId;
    private Long hotelId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String roomName;
    private String hotelName;
    private String imageURL;
    private Status status;
}
