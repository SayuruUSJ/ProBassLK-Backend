package lk.workbridge.marketplace.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lk.workbridge.marketplace.entity.Worker;
import lk.workbridge.marketplace.entity.WorkerSkill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceProviderADResponse {

    private String serviceId;

    private String workerId;

    private String email;

    private String phoneNumber;

    private String firstName;

    private String lastName;

    private String district;

    private Boolean available;

    private String address;

    private String status;

    private String profileUrl;
   
    private List<WorkerSkillResponse> skills;

}
