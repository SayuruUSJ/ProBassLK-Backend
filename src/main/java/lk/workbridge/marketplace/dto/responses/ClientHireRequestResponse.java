package lk.workbridge.marketplace.dto.responses;

import java.time.LocalDate;

public record ClientHireRequestResponse(
        String id,
        String workerId,
        String providerName,
        String providerContactNumber,
        String profileImageUrl,
        String serviceTitle,
        String description,
        String location,
        LocalDate requestedDate,
        LocalDate createdAt,
        Double offeredRate,
       String status,
        String serviceProviderImageUrl
) {
}
