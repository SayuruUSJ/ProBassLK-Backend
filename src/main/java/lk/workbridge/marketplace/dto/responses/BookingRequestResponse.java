package lk.workbridge.marketplace.dto.responses;

import java.time.LocalDate;

public record BookingRequestResponse(
        String id,
        String status,
        String clientId,
        String clientName,
        String advertisementId,
        String workerEmail,
        String requestedService,
        LocalDate requestedDate,
        String clientContactNumber,
        String location

) {
}
