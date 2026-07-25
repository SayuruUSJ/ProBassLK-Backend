package lk.workbridge.marketplace.dto.responses;

import lk.workbridge.marketplace.entity.JobRole;
import lk.workbridge.marketplace.enums.RateType;

public record WorkerSkillResponse(
        Long id,
        JobRole jobRole,
        Double dailyRate,
        RateType rateType,
        Integer experience,
        String description,
        Boolean negotiable
) {
}
