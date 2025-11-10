package com.example.project_reactnative.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    @NotNull(message = "Vui lòng nhập userId!")
    private Long userId;

    @NotNull(message = "Vui lòng nhập id phòng!")
    private Long roomId;

    @NotNull(message = "Vui lòng nhập id khách sạn!")
    private Long hotelId;

    @NotNull(message = "Vui lòng nhập ngày nhận phòng!")
    private LocalDate checkInDate;

    @NotNull(message = "Vui lòng nhập ngày trả phòng!")
    private LocalDate checkOutDate;

    @Min(value = 0, message = "Số người lớn phải lớn hơn 0!")
    private int adults;

    @Min(value = 0, message = "Số người trẻ em phải lớn hơn 0!")
    private int children;

    @Min(value = 0, message = "Số người trẻ sơ sinh phải lớn hơn 0!")
    private int infants;

    private String paymentOption;

    @NotNull(message = "Vui lòng chọn phương thức thanh toán!")
    private Long paymentMethodId;
}
