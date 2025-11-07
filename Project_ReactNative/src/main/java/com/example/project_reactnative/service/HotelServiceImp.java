package com.example.project_reactnative.service;

import com.example.project_reactnative.model.dto.response.HotelResponse;
import com.example.project_reactnative.model.entity.Hotel;
import com.example.project_reactnative.model.entity.HotelImage;
import com.example.project_reactnative.repository.HotelImageRepository;
import com.example.project_reactnative.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class HotelServiceImp implements HotelService {
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelImageRepository hotelImageRepository;

    @Override
    public List<HotelResponse> getHotelsByLocation(List<Long> provinceIds, String sortBy) {
        List<Hotel> hotels;

        if (provinceIds == null || provinceIds.isEmpty()) {
            hotels = hotelRepository.findAll();
        } else {
            hotels = hotelRepository.findByProvinceIdIn(provinceIds);
        }

        if (sortBy != null && !sortBy.isEmpty()) {
            switch (sortBy.toLowerCase()) {
                case "az":
                    hotels.sort(Comparator.comparing(Hotel::getHotelName, String.CASE_INSENSITIVE_ORDER));
                    break;
                case "za":
                    hotels.sort(Comparator.comparing(Hotel::getHotelName, String.CASE_INSENSITIVE_ORDER).reversed());
                    break;
                default:
                    break;
            }
        }

        return hotels.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public HotelResponse getHotelById(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new NoSuchElementException("Không tìm thấy khách sạn!"));
        return convertToDTO(hotel);
    }

    private HotelResponse convertToDTO(Hotel hotel) {
        if (hotel == null) return null;

        List<HotelImage> images = hotelImageRepository.findByHotelId(hotel.getId());

        return new HotelResponse(
                hotel.getId(),
                hotel.getHotelName(),
                hotel.getHotelAddress(),
                images.get(0).getImageUrl(),
                hotel.getProvince() != null ? hotel.getProvince().getProvinceName() : null
        );
    }


}
