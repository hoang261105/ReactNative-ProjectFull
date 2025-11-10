package com.example.project_reactnative.repository;

import com.example.project_reactnative.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> getReviewsByHotelIdAndRoomId(Long hotelId, Long roomId);
}
