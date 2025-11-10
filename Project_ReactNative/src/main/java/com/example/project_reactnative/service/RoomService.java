package com.example.project_reactnative.service;

import com.example.project_reactnative.model.dto.request.RangePrice;
import com.example.project_reactnative.model.dto.response.RoomDetail;
import com.example.project_reactnative.model.dto.response.RoomImageResponse;
import com.example.project_reactnative.model.dto.response.RoomResponse;
import com.example.project_reactnative.model.entity.Room;

import java.math.BigDecimal;
import java.util.List;

public interface RoomService {
    List<RoomResponse> getRoomsByTitle(String title);
    RoomDetail getRoomById(Long id);
    List<RoomResponse> getRoomsByPriceAndSort(Long hotelId, RangePrice rangePrice, String sort);
    RoomImageResponse getImageById(Long imageId, Long roomId);
}
