package lk.workbridge.marketplace.dto.responses;

import java.util.List;

public record WorkerMoreInfo(
        Boolean available,
        String title,
        List<BookingRequestResponse> bookingRequests,
        List<WorkerSkillResponse> skills,
        List<RatingResponse> ratings
) {
}
