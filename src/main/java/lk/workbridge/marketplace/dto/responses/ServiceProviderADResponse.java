package lk.workbridge.marketplace.dto.responses;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public record ServiceProviderADResponse(
        String serviceId,

        String workerId,

        String email,

        String phoneNumber,

        String firstName,

        String lastName,

        String district,

        String jobTitle,

        Boolean available,

        String address,

        String status,

        String profileUrl,

        Double averageStars,

        long completedJobs,

        List<WorkerSkillResponse> skills,

        String about,

        int overallExperience,

        Set<DayOfWeek> workingDaysMask,

        LocalTime startTime,

        LocalTime endTime,

        Boolean emergencyAvailable,

        List<RatingResponse> ratings,

        long reviewCount,

        String profileImageUrl,

        String city


) {
}
