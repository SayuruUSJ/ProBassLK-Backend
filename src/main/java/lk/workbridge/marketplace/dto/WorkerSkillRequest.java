package lk.workbridge.marketplace.dto;

import jakarta.validation.constraints.NotNull;
import lk.workbridge.marketplace.enums.RateType;
import lk.workbridge.marketplace.enums.WorkerJobRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerSkillRequest {


    @NotNull
    private WorkerJobRole role;

    @NotNull
    private Double dailyRate;

    private RateType rateType;

    private int experience;

    private String description;
    private boolean negotiable;
}
