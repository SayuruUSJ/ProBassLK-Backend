package lk.workbridge.marketplace.dto;

import lk.workbridge.marketplace.entity.Client;
import lk.workbridge.marketplace.entity.User;
import lk.workbridge.marketplace.entity.Worker;
import lk.workbridge.marketplace.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    private String id;

    private String username;

    private String email;

    private String phoneNumber;

    private String firstName;

    private String lastName;

    private String district;

    private String address;

    private Role role;

    private Date createdAt;

    private Boolean verificationStatus;

    private String profileImageUrl;
    private String organizationName;
    private List<WorkerSkillResponse> skills;
}
