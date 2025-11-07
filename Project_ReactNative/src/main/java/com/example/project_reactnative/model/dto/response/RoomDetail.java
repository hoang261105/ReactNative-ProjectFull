package com.example.project_reactnative.model.dto.response;

import com.example.project_reactnative.model.entity.Feature;
import com.example.project_reactnative.model.entity.RoomImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDetail{
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private int guestCount;
    private int bedroomCount;
    private int bedCount;
    private int bathRoomCount;

    private String hotelName;
    private List<RoomImageResponse> images;
    private List<FeatureResponse> features;
}
