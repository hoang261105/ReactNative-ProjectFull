package com.example.project_reactnative.service;

import com.example.project_reactnative.model.dto.request.ReviewRequest;
import com.example.project_reactnative.model.dto.response.ReviewResponse;
import com.example.project_reactnative.model.entity.Review;

import java.util.List;

public interface ReviewService {
    ReviewResponse addReview(ReviewRequest reviewRequest);
    List<ReviewResponse> getReviewsByRoom(Long hotelId, Long roomId);
}
