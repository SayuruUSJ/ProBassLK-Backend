package lk.workbridge.marketplace.dto.responses;

import lk.workbridge.marketplace.enums.Role;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

public record ServiceProviderProfile(
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

        List<WorkerSkillResponse> skills,

         boolean available,
        String title,
        Integer overallExperience,
        String about,
        Boolean emergencyAvailable,
        String nic,
        Set<DayOfWeek> workingDaysMask,
        LocalTime startTime,
        LocalTime endTime
) {
}
