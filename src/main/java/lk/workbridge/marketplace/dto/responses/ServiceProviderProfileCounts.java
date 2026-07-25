package lk.workbridge.marketplace.dto.responses;

public record ServiceProviderProfileCounts(
        long pendingHireRequestCount,
        long pendingApplicationCount,
        long OngoingJobs
) {
}
