package lk.workbridge.marketplace.controller;

import lk.workbridge.marketplace.dto.RatingRequest;
import lk.workbridge.marketplace.service.RatingAndReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/ratings")
@RequiredArgsConstructor
public class RatingAndReviewController {

    private final RatingAndReviewService ratingAndReviewService;


    @PostMapping("/add-review")
    public ResponseEntity<?> addRating(
            @RequestBody RatingRequest requestDTO
            ) {

        String response = ratingAndReviewService.addRatingAndReview(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
