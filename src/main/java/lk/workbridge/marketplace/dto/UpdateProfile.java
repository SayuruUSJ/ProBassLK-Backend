package lk.workbridge.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfile {

    private String userId;

    private String phoneNumber;

    private String district;

    private String address;

    private String organizationName;

    private Boolean available;

    private Set<WorkerSkillRequest> skills;
}
