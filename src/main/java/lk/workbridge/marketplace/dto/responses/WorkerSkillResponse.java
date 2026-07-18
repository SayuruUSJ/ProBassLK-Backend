package lk.workbridge.marketplace.dto.responses;

import lk.workbridge.marketplace.entity.JobRole;
import lk.workbridge.marketplace.enums.RateType;

public record WorkerSkillResponse(
       JobRole jobRole,
        Double dailyRate,
       RateType rateType,
       int experience,
      String description,
       boolean negotiable
) {
}
