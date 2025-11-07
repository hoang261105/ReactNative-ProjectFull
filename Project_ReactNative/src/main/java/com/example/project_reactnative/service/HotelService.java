package com.example.project_reactnative.service;

import com.example.project_reactnative.model.dto.response.HotelResponse;
import com.example.project_reactnative.model.entity.Hotel;

import java.util.List;

public interface HotelService {
    List<HotelResponse> getHotelsByLocation(List<Long> provinceIds, String sortBy);
    HotelResponse getHotelById(Long hotelId);
}
