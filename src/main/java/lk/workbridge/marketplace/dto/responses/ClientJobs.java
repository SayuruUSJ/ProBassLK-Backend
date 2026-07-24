package lk.workbridge.marketplace.dto.responses;

import java.time.LocalDate;

public record ClientJobs(
        String hireRequestId,
        String requestedService,
        String status,
        String serviceProviderName,
        String serviceProviderId,
        String fullAddress,
        LocalDate requestedDate,
        LocalDate createdAt,
        String serviceProviderContactNumber

) {
}
