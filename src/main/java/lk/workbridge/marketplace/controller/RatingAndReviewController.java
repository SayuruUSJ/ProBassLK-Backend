package lk.workbridge.marketplace.controller;

import lk.workbridge.marketplace.dto.RatingRequest;
import lk.workbridge.marketplace.dto.responses.RatingResponse;
import lk.workbridge.marketplace.service.RatingAndReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/get-client-reviews")
    public ResponseEntity<Page<RatingResponse>> getClientRatings(
            @RequestParam String clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<RatingResponse> ratings = ratingAndReviewService.getRatingsAndReviewsClient(page, size, clientId);

        if (ratings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(ratings);
    }

    @PutMapping("/add-reply-for-review")
    public ResponseEntity<?> addServiceProviderReplyForReview(
           @RequestParam long id,
           @RequestParam String providerReply
    ){
        return ResponseEntity.ok(ratingAndReviewService.addReplyToReview(id,providerReply));
    }

}

