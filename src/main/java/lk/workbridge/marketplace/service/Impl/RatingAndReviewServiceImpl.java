package lk.workbridge.marketplace.service.Impl;

import lk.workbridge.marketplace.dto.RatingRequest;
import lk.workbridge.marketplace.dto.responses.RatingResponse;
import lk.workbridge.marketplace.entity.Client;
import lk.workbridge.marketplace.entity.Rating;
import lk.workbridge.marketplace.entity.User;
import lk.workbridge.marketplace.entity.Worker;
import lk.workbridge.marketplace.repository.RatingRepository;
import lk.workbridge.marketplace.repository.UserRepository;
import lk.workbridge.marketplace.service.RatingAndReviewService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.hc.core5.http2.impl.nio.ClientH2IOEventHandler;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingAndReviewServiceImpl implements RatingAndReviewService {
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;

    @Override
    public String addRatingAndReview(RatingRequest ratingRequest) {
        try {

            Rating rating = new Rating();
            User userOne = userRepository.findById(ratingRequest.getWorkerId())
                    .orElseThrow(() -> new RuntimeException("User not found."));

            Worker worker = (Worker) userOne;

            User userTwo = userRepository.findById(ratingRequest.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client not found."));
            Client client = (Client) userTwo;
            rating.setWorker(worker);
            rating.setComment(ratingRequest.getComment());
            rating.setClient(client);
            rating.setCreatedAt(LocalDateTime.now());
            rating.setStars(ratingRequest.getStars());
            ratingRepository.save(rating);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error occurred while sending the request.", e);
        }
        return "review added successfully";
    }

    @Override
    public Page<RatingResponse> getRatingsAndReviewsClient(int page, int size, String clientId) {
        User userOne = userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("User not found."));
        Client client = (Client) userOne;
        Pageable pageable = PageRequest.of(page, size);

        return  ratingRepository.findByClient(client, pageable)
                .map(this::mapToRatingResponse);
    }

    @Override
    public String addReplyToReview(long id, String providerReply) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rating not found with ID: " + id));
        if (providerReply == null || providerReply.trim().isEmpty()) {
            throw new RuntimeException("Reply cannot be empty");
        }


        if (rating.getProviderReply() != null && !rating.getProviderReply().isEmpty()) {
            throw new RuntimeException("A reply already exists for this review. You can edit it instead.");
        }
        rating.setProviderReply(providerReply);
        ratingRepository.save(rating);

        return "Reply added successfully to review";
    }


    private RatingResponse mapToRatingResponse(Rating rating) {
        Worker worker = rating.getWorker();
        Client client = rating.getClient();

        String workerName = buildFullName(worker);
        String clientName = buildFullName(client);
        String jobTitle = worker.getTitle();

        return new RatingResponse(
                rating.getId(),
                rating.getStars(),
                rating.getComment() != null ? rating.getComment() : "No comment provided",
                worker != null ? worker.getId() : null,
                workerName,
                client != null ? client.getId() : null,
                clientName,
                jobTitle
        );


    }
    private String buildFullName(User user) {
        if (user == null) {
            return "Unknown User";
        }
        String firstName = user.getFirstName() != null ? user.getFirstName() : "";
        String lastName = user.getLastName() != null ? user.getLastName() : "";
        String fullName = (firstName + " " + lastName).trim();
        return fullName.isEmpty() ? "Unknown User" : fullName;
    }
}
