package com.example.project_reactnative.controller;

import com.example.project_reactnative.model.dto.request.RangePrice;
import com.example.project_reactnative.model.dto.response.APIResponse;
import com.example.project_reactnative.model.dto.response.HotelResponse;
import com.example.project_reactnative.model.dto.response.RoomResponse;
import com.example.project_reactnative.model.entity.Hotel;
import com.example.project_reactnative.model.entity.Room;
import com.example.project_reactnative.service.HotelService;
import com.example.project_reactnative.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomService roomService;

    @GetMapping("/{provinceId}/hotels")
    public ResponseEntity<APIResponse<List<HotelResponse>>> getHotelsByProvince(
            @PathVariable Long provinceId,
            @RequestParam(required = false) String sortBy
    ) {
        List<Long> provinceIds = Collections.singletonList(provinceId);
        List<HotelResponse> hotels = hotelService.getHotelsByLocation(provinceIds, sortBy);
        return new ResponseEntity<>(new APIResponse<>(true, "Thành công", hotels), HttpStatus.OK);
    }

    @GetMapping("/hotels/{id}")
    public ResponseEntity<APIResponse<HotelResponse>> getHotelById(@PathVariable Long id) {
        return new ResponseEntity<>(new APIResponse<>(true, "Lấy chi tiết 1 khách sạn thành công!", hotelService.getHotelById(id)), HttpStatus.OK);
    }

    @GetMapping("/hotels")
    public ResponseEntity<APIResponse<List<HotelResponse>>> getHotelsByFilter(
            @RequestParam(name = "provinceId", required = false) List<Long> provinceIds,
            @RequestParam(name = "sortBy", required = false) String sortBy
    ) {
        List<HotelResponse> responses = hotelService.getHotelsByLocation(provinceIds, sortBy);
        return new ResponseEntity<>(new APIResponse<>(true, "Lọc khách sạn theo tỉnh thành thành công!", responses), HttpStatus.OK);
    }

    @GetMapping("/hotels/{hotelId}/rooms")
    public ResponseEntity<APIResponse<List<RoomResponse>>> getRoomsByHotelId(
            @PathVariable Long hotelId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String sortBy
    ){
        RangePrice rangePrice = new RangePrice();
        rangePrice.setMinPrice(minPrice);
        rangePrice.setMaxPrice(maxPrice);
        List<RoomResponse> roomResponses = roomService.getRoomsByPriceAndSort(hotelId, rangePrice, sortBy);
        return new ResponseEntity<>(new APIResponse<>(true, "Thành công!", roomResponses), HttpStatus.OK);
    }
}
