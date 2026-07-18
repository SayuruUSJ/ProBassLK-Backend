package lk.workbridge.marketplace.dto.responses;

import java.time.LocalDate;

public record ServiceWantedADResponse(
        String advertisementId,

         String firstName,

         String lastName,

         String title,

         String clientContactNumber,

         String description,

         String serviceType,

         String location,

        LocalDate requiredDate,

         String status,

        Long applicationCount
) {
}
