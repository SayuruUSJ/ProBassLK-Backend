package lk.workbridge.marketplace.service.Impl;

import lk.workbridge.marketplace.dto.responses.ClientProfileCounts;
import lk.workbridge.marketplace.repository.ApplicationForWantedADRepository;
import lk.workbridge.marketplace.repository.HireRequestRepository;
import lk.workbridge.marketplace.service.ClientAndServiceProviderProfileCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientAndServiceProviderProfileCountServiceImpl implements ClientAndServiceProviderProfileCountService {
    private final HireRequestRepository hireRequestRepository;
    private final ApplicationForWantedADRepository applicationForWantedADRepository;

    @Override
    public ClientProfileCounts getCounts(String clientId) {

        long hireOngoingRequestCount = hireRequestRepository
                .countByClientId(clientId, "CONFIRMED");

        long applicationOngoingCount = applicationForWantedADRepository
                .countByClientIdAndStatus(clientId, "CONFIRMED");

        long ongoingJobs = hireOngoingRequestCount + applicationOngoingCount;

        long hirePendingRequestCount = hireRequestRepository
                .countByClientId(clientId, "PENDING");

        long applicationPendingCount = applicationForWantedADRepository
                .countByClientIdAndStatus(clientId, "PENDING");

        long pendingJobs = hirePendingRequestCount + applicationPendingCount;

        long hireActiveRequestCount = hireRequestRepository
                .countByClientId(clientId, "PUBLISHED");

        long applicationActiveCount = applicationForWantedADRepository
                .countByClientIdAndStatus(clientId, "PUBLISHED");
        long activeAds = hireActiveRequestCount + applicationActiveCount;
        return new ClientProfileCounts(
                activeAds,
                pendingJobs,
                ongoingJobs
        );
    }
}
