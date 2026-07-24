package lk.workbridge.marketplace.dto.responses;

import lk.workbridge.marketplace.enums.ContactMethod;
import lk.workbridge.marketplace.enums.PaymentType;

import java.time.LocalDate;
import java.time.LocalTime;

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

        Long applicationCount,
        ContactMethod preferredContactMethod,
        String city,
        String district,
        LocalTime startTime,
        String expectedDuration,
        LocalDate applicationDeadline,
        Boolean isWorkDateFlexible,
        String requiredSkills,
        Integer noOfWorkersRequired,
        PaymentType paymentType,
        Double offeredRate,
        Boolean isRateNegotiable,
        String additionalInstructions,
        boolean isUrgent,
        LocalDate createdAt,
        LocalDate updatedAt,
        String clientProfileImageUrl
) {
}
