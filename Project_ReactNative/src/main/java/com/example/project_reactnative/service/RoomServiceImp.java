package com.example.project_reactnative.service;

import com.example.project_reactnative.model.dto.request.RangePrice;
import com.example.project_reactnative.model.dto.response.FeatureResponse;
import com.example.project_reactnative.model.dto.response.RoomDetail;
import com.example.project_reactnative.model.dto.response.RoomImageResponse;
import com.example.project_reactnative.model.dto.response.RoomResponse;
import com.example.project_reactnative.model.entity.Feature;
import com.example.project_reactnative.model.entity.Room;
import com.example.project_reactnative.model.entity.RoomImage;
import com.example.project_reactnative.repository.FeatureRepository;
import com.example.project_reactnative.repository.RoomImageRepository;
import com.example.project_reactnative.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RoomServiceImp implements RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomImageRepository roomImageRepository;

    @Autowired
    private FeatureRepository featureRepository;

    @Override
    public List<RoomResponse> getRoomsByTitle(String title) {
        List<Room> rooms;

        if (title != null && !title.isEmpty()) {
            rooms = roomRepository.findRoomsByTitleContainingIgnoreCase(title);
        } else {
            rooms = roomRepository.findAll();
        }

        return rooms.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public RoomDetail getRoomById(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tìm thấy phòng!"));
        return convertToDetail(room);
    }

    @Override
    public List<RoomResponse> getRoomsByPriceAndSort(Long hotelId, RangePrice rangePrice, String sort) {
        List<Room> rooms;

        if (rangePrice.getMinPrice() != null || rangePrice.getMaxPrice() != null) {
            rooms = roomRepository.findRoomsByHotelIdAndPriceGreaterThanEqualAndPriceLessThanEqual(hotelId, rangePrice.getMinPrice(), rangePrice.getMaxPrice());
        } else {
            rooms = roomRepository.findRoomsByHotelId(hotelId);
        }

        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "price_asc":
                    rooms.sort(Comparator.comparing(Room::getPrice));
                    break;
                case "price_desc":
                    rooms.sort(Comparator.comparing(Room::getPrice).reversed());
                    break;
                case "title_az":
                    rooms.sort(Comparator.comparing(Room::getTitle, String.CASE_INSENSITIVE_ORDER));
                    break;
                case "title_za":
                    rooms.sort(Comparator.comparing(Room::getTitle, String.CASE_INSENSITIVE_ORDER).reversed());
                    break;
                default:
                    break;
            }
        }
        return rooms.stream()
                .map(this::convertToDTO).toList();
    }

    public RoomResponse convertToDTO(Room room) {
        if (room == null) return null;

        List<RoomImage> images = roomImageRepository.findByRoomId(room.getId());

        return new RoomResponse(
                room.getId(),
                room.getTitle(),
                images.get(0).getImageUrl(),
                room.getHotel().getHotelName(),
                room.getPrice()
        );
    }

    public RoomDetail convertToDetail(Room room) {
        List<RoomImage> images = roomImageRepository.findByRoomId(room.getId());
        List<RoomImageResponse> imageResponses = images.stream().map(this::convertToImageResponse).toList();

        List<Feature> features = featureRepository.findByRoomId(room.getId());
        List<FeatureResponse> featureResponses = features.stream().map(this::convertToDTO).toList();

        return new RoomDetail(
                room.getId(),
                room.getTitle(),
                room.getDescription(),
                room.getPrice(),
                room.getGuestCount(),
                room.getBedCount(),
                room.getBedroomCount(),
                room.getBathRoomCount(),
                room.getHotel().getHotelName(),
                imageResponses,
                featureResponses
        );
    }

    public RoomImageResponse convertToImageResponse(RoomImage roomImage) {
        if (roomImage == null) return null;

        return new RoomImageResponse(
                roomImage.getId(),
                roomImage.getImageUrl(),
                roomImage.getSize()
        );
    }

    public FeatureResponse convertToDTO(Feature feature) {
        if (feature == null) return null;

        return new FeatureResponse(
                feature.getId(),
                feature.getTitle(),
                feature.getIconName(),
                feature.getDescription()
        );
    }
}
