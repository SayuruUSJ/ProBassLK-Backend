package lk.workbridge.marketplace.dto.responses;

import java.time.LocalTime;
import java.util.List;

public record WorkerMoreInfo(
        Boolean available,
        String title,
        List<BookingRequestResponse> bookingRequests,
        List<WorkerSkillResponse> skills,
        List<RatingResponse> ratings,
        Integer overallExperience,
        String about,
        Boolean emergencyAvailable,
        String nic,
        int workingDaysMask,
        LocalTime startTime,
        LocalTime endTime

) {
}
