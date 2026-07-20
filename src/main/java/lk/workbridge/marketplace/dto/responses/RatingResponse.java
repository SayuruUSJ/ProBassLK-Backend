package lk.workbridge.marketplace.dto.responses;

public record RatingResponse(
        long id,
        int stars,
        String comment,
        String workerId,
        String workerName,
        String clientId,
        String clientName,
        String jobTitle
        ) {
}
