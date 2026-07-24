package lk.workbridge.marketplace.dto.responses;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RatingResponse(
        long id,
        int stars,
        String comment,
        String workerId,
        String workerName,
        String clientId,
        String clientName,
        String jobTitle,
        String providerReply,
        LocalDateTime createdAt

        ) {
}
