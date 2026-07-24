package lk.workbridge.marketplace.dto.responses;

import java.time.LocalDate;

public record ServiceProviderJobs(
        String requestedService,
        String status,
        String clientName,
        String fullAddress,
        LocalDate requestedDate,
        LocalDate createdAt,
        String clientContactNumber,
        String description,
        String hireRequestId,
        int applicationId,
        String clientProfileImageUrl


) {
}
