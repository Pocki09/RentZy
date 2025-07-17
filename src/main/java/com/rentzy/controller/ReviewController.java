package com.rentzy.controller;

import com.rentzy.model.dto.FilterParams;
import com.rentzy.model.dto.ReviewDTO;
import com.rentzy.service.ReviewService;
import com.rentzy.utils.PageableUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/reviews")
@Validated
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<Page<ReviewDTO>> getAllReviews(@Valid @ModelAttribute FilterParams filterParams) {

        Pageable pageable = PageableUtils.buildPageable(filterParams, 10);

        Page<ReviewDTO> reviews = reviewService.getAllReviews(pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ReviewDTO>> getReviewsByUserId(@PathVariable String userId,
                                                              @Valid @ModelAttribute FilterParams filterParams) {

        Pageable pageable = PageableUtils.buildPageable(filterParams, 10);
        Page<ReviewDTO> reviews = reviewService.getReviewByUserId(userId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Page<ReviewDTO>> getReviewsByPostId(@PathVariable String postId,
                                                              @Valid @ModelAttribute FilterParams filterParams) {

        Pageable pageable = PageableUtils.buildPageable(filterParams, 10);
        Page<ReviewDTO> reviews = reviewService.getReviewByPostId(postId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable String id) {
        ReviewDTO review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(
            @Valid @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO created = reviewService.createReview(reviewDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable String id,
            @Valid @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updated = reviewService.updateReview(id, reviewDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable String id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
