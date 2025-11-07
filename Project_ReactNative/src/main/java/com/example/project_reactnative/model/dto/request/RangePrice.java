package com.example.project_reactnative.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RangePrice {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
