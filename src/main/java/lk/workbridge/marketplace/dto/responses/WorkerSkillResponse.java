package lk.workbridge.marketplace.dto.responses;

import lk.workbridge.marketplace.entity.JobRole;

public record WorkerSkillResponse(
       JobRole jobRole,
        Double dailyRate
) {
}
