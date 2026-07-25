package lk.workbridge.marketplace.dto.responses;

import java.time.LocalDate;

public record HireRequestResponse(
        String id,
        String clientName,
        String serviceTitle,
        String description,
        String fullAddress,
        LocalDate requestedDate,
        LocalDate createAt,
        Double offeredRate,
        String status,
        String clientProfileImageUrl

) {
}
