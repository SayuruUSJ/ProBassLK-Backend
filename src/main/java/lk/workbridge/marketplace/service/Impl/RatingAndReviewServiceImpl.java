package lk.workbridge.marketplace.service.Impl;

import lk.workbridge.marketplace.dto.RatingRequest;
import lk.workbridge.marketplace.entity.Client;
import lk.workbridge.marketplace.entity.Rating;
import lk.workbridge.marketplace.entity.User;
import lk.workbridge.marketplace.entity.Worker;
import lk.workbridge.marketplace.repository.RatingRepository;
import lk.workbridge.marketplace.repository.UserRepository;
import lk.workbridge.marketplace.service.RatingAndReviewService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.apache.hc.core5.http2.impl.nio.ClientH2IOEventHandler;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingAndReviewServiceImpl implements RatingAndReviewService {
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    @Override
    public String addRatingAndReview(RatingRequest ratingRequest) {
        try{

            Rating rating=new Rating();
                User userOne = userRepository.findById(ratingRequest.getWorkerId())
                        .orElseThrow(() -> new RuntimeException("User not found."));

            Worker worker = (Worker) userOne;

            User userTwo= userRepository.findById(ratingRequest.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client not found."));
            Client client=(Client)userTwo;
            rating.setWorker(worker);
            rating.setComment(ratingRequest.getComment());
            rating.setClient(client);
            rating.setCreatedAt(LocalDateTime.now());
            rating.setStars(ratingRequest.getStars());
            ratingRepository.save(rating);
        }catch (DataAccessException e){
            throw new RuntimeException("Database error occurred while sending the request.", e);
        }
        return "review added succfullly";
    }
}
