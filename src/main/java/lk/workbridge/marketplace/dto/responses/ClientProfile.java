package lk.workbridge.marketplace.dto.responses;

import lk.workbridge.marketplace.enums.Role;

import java.util.Date;

public record ClientProfile(
        String id,

        String username,

        String email,

        String phoneNumber,

        String secondaryPhoneNumber,

        String landLineNumber,

        String whatsappNumber,

        String firstName,

        String lastName,

        String district,

        String city,

        String address,

        Role role,

        Date createdAt,

        Boolean verificationStatus,

        String profileImageUrl,
        String organizationName
) {
}
