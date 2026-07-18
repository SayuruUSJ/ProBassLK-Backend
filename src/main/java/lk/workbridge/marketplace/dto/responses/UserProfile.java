package lk.workbridge.marketplace.dto.responses;

import lk.workbridge.marketplace.enums.Role;

import java.util.Date;
import java.util.List;

public record UserProfile(
        String id,

        String username,

        String email,

        String phoneNumber,

        String firstName,

        String lastName,

        String district,

        String address,

        Role role,

        Date createdAt,

        Boolean verificationStatus,

        String profileImageUrl,
        String organizationName,
        List<WorkerSkillResponse> skills
) {
}
