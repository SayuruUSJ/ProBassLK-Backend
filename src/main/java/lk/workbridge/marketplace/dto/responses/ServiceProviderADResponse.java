package lk.workbridge.marketplace.dto.responses;

import java.util.List;

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

        List<WorkerSkillResponse> skills
) {
}
