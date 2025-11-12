package com.example.project_reactnative.service;

import com.example.project_reactnative.model.dto.request.ReviewRequest;
import com.example.project_reactnative.model.dto.response.ReviewResponse;
import com.example.project_reactnative.model.entity.Hotel;
import com.example.project_reactnative.model.entity.Review;
import com.example.project_reactnative.model.entity.Room;
import com.example.project_reactnative.model.entity.User;
import com.example.project_reactnative.repository.AuthRepository;
import com.example.project_reactnative.repository.HotelRepository;
import com.example.project_reactnative.repository.ReviewRepository;
import com.example.project_reactnative.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ReviewServiceImp implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private AuthRepository authRepository;

    @Override
    public ReviewResponse addReview(ReviewRequest reviewRequest) {
        Room room = roomRepository.findById(reviewRequest.getRoomId()).orElseThrow(() -> new NoSuchElementException("Không tìm thấy phòng!"));
        Hotel hotel = hotelRepository.findById(reviewRequest.getHotelId()).orElseThrow(() -> new NoSuchElementException("Không tìm thấy khách sạn!"));
        User user = authRepository.findById(reviewRequest.getUserId()).orElseThrow(() -> new NoSuchElementException("Không tìm thấy user!"));

        Review review = Review.builder()
                .hotel(hotel)
                .user(user)
                .room(room)
                .rating(reviewRequest.getRating())
                .comment(reviewRequest.getComment())
                .commentDate(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .build();

        Review result = reviewRepository.save(review);
        return convertToReviewResponse(result);
    }

    @Override
    public List<ReviewResponse> getReviewsByRoom(Long hotelId, Long roomId) {
        List<Review> reviewResponses = reviewRepository.getReviewsByHotelIdAndRoomId(hotelId, roomId);
        return reviewResponses.stream()
                .sorted(Comparator.comparing(Review::getCommentDate).reversed())
                .map(this::convertToReviewResponse)
                .toList();
    }

    public ReviewResponse convertToReviewResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getCommentDate(),
                review.getComment(),
                review.getUser().getAvatar(),
                review.getUser().getFullName(),
                review.getRating()
        );
    }
}
