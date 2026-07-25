package lk.workbridge.marketplace.dto.responses;

import java.util.List;

public record ClientMoreInfo(
        String organizationName,
        List<BookingRequestResponse> bookingRequests

) {
}
