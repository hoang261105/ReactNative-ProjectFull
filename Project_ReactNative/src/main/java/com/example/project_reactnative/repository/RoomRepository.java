package com.example.project_reactnative.repository;

import com.example.project_reactnative.model.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findRoomsByTitleContainingIgnoreCase(String roomName);
    List<Room> findRoomsByHotelId(Long hotelId);
    List<Room> findRoomsByHotelIdAndPriceGreaterThanEqualAndPriceLessThanEqual(Long hotelId, BigDecimal minPrice, BigDecimal maxPrice);
}
