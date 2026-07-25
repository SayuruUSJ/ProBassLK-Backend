package lk.workbridge.marketplace.dto.responses;

public record ClientProfileCounts(
        long activeAds,
        long pendingAds,
        long ongoingJobs
) {
}
