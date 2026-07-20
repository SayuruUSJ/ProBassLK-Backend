package lk.workbridge.marketplace.dto.responses;

import java.time.LocalDate;

public record ClientJobs(
        String requestedService,
        String status,
        String serviceProviderName,
        String fullAddress,
        LocalDate requestedDate,
        LocalDate createdAt,
        String serviceProviderContactNumber

) {
}
