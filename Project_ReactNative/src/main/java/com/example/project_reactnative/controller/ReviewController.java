package com.example.project_reactnative.controller;

import com.example.project_reactnative.model.dto.request.ReviewRequest;
import com.example.project_reactnative.model.dto.response.APIResponse;
import com.example.project_reactnative.model.dto.response.ReviewResponse;
import com.example.project_reactnative.model.entity.Review;
import com.example.project_reactnative.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<APIResponse<ReviewResponse>> createReview(@RequestBody ReviewRequest reviewRequest){
        return new ResponseEntity<>(new APIResponse<>(true, "Thêm đánh giá thành công", reviewService.addReview(reviewRequest)), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<ReviewResponse>>> getReviewsByRoom(
            @RequestParam(required = false) Long hotelId,
            @RequestParam(required = false) Long roomId
    ){
        return new ResponseEntity<>(new APIResponse<>(true, "Lấy danh sách đánh giá thành công!", reviewService.getReviewsByRoom(hotelId, roomId)), HttpStatus.OK);
    }
}
