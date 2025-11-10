package com.example.project_reactnative.repository;

import com.example.project_reactnative.model.entity.Room;
import com.example.project_reactnative.model.entity.RoomImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findRoomsByTitleContainingIgnoreCase(String roomName);
    List<Room> findRoomsByHotelId(Long hotelId);
    List<Room> findRoomsByHotelIdAndPriceGreaterThanEqualAndPriceLessThanEqual(Long hotelId, BigDecimal minPrice, BigDecimal maxPrice);
}
