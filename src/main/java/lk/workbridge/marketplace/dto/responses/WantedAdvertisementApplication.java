package lk.workbridge.marketplace.dto.responses;

import lk.workbridge.marketplace.enums.PaymentType;

public record WantedAdvertisementApplication(

        String title,
        String serviceProviderName,
        String jobTitle,
        int overallExperience,
        Boolean serviceProviderVerificationStatus,
        String description,
        String additionalInstructions,
        String serviceType,
        Boolean isRateNegotiable,
        Double offeredRate,
        PaymentType paymentType,
        String status,
        Double ratingStars,
        String serviceProviderId


) {
}
