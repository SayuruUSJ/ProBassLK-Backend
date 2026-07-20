package lk.workbridge.marketplace.service;

import lk.workbridge.marketplace.dto.RatingRequest;
import lk.workbridge.marketplace.dto.responses.RatingResponse;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;

public interface RatingAndReviewService {

String addRatingAndReview(RatingRequest ratingRequest);
Page<RatingResponse> getRatingsAndReviewsClient(int page, int size, String clientId);
String addReplyToReview(long id,String providerReply);
}
