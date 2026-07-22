package lk.workbridge.marketplace.dto.responses;

import lk.workbridge.marketplace.enums.Role;

import java.util.Date;

public record UserBasicInfo(
        String id,

        String username,

        String password,

        String email,

        String phoneNumber,

        String secondaryPhoneNumber,

        String landlineNumber,

        String whatsappNumber,

        String firstName,

        String lastName,

        String district,

        String address,

        Role role,

        Date createdAt,

        Boolean verificationStatus,

        String profileImageUrl

) {
}
