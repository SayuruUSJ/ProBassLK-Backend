package lk.workbridge.marketplace.dto.responses;

import lk.workbridge.marketplace.enums.Role;

import java.util.Date;
import java.util.List;

public record ClientProfile(
        String id,

        String username,

        String email,

        String phoneNumber,

        String secondaryPhoneNumber,

        String landLineNumber,

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
