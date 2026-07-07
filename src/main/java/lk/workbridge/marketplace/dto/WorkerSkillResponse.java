package lk.workbridge.marketplace.dto;

import lk.workbridge.marketplace.entity.JobRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerSkillResponse {
    private JobRole jobRole;
    private Double dailyRate;
}
