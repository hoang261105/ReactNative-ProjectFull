package com.example.project_reactnative.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDateRange {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
