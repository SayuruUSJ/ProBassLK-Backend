package lk.workbridge.marketplace.service.Impl;

import jakarta.transaction.Transactional;
import lk.workbridge.marketplace.dto.responses.ClientJobs;
import lk.workbridge.marketplace.entity.ApplicationsForWantedAdvertisements;
import lk.workbridge.marketplace.entity.HireRequest;
import lk.workbridge.marketplace.entity.ServiceWantedAdvertisement;
import lk.workbridge.marketplace.entity.Worker;
import lk.workbridge.marketplace.repository.ApplicationForWantedADRepository;
import lk.workbridge.marketplace.repository.HireRequestRepository;
import lk.workbridge.marketplace.service.ClientJobsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ClientJobsServiceImpl implements ClientJobsService {
    private final ApplicationForWantedADRepository applicationForWantedADRepository;
    private final HireRequestRepository hireRequestRepository;

    @Transactional
    @Override
    public List<ClientJobs> getClientOngoingJobs(String clientId) {
        List<ClientJobs> allJobs = new ArrayList<>();


        List<String> statuses = Arrays.asList("IN_PROGRESS", "ACCEPTED");
        List<HireRequest> hireRequests = hireRequestRepository
                .findByClientIdAndStatus(clientId, statuses);

        if (!hireRequests.isEmpty()) {
            List<ClientJobs> hireJobs = hireRequests.stream()
                    .map(this::mapToClientJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(hireJobs);
        }

        List<String> statuses2 = Arrays.asList("IN_PROGRESS", "CONFIRMED");
        List<ApplicationsForWantedAdvertisements> applications =
                applicationForWantedADRepository.findByClientIdAndStatus(clientId, statuses2);

        if (!applications.isEmpty()) {
            List<ClientJobs> applicationJobs = applications.stream()
                    .map(this::mapApplicationToClientJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(applicationJobs);
        }
        return allJobs;
    }

    @Transactional
    @Override
    public List<ClientJobs> getClientCompletedJobs(String clientId) {
        List<ClientJobs> allJobs = new ArrayList<>();

        List<String> statuses = Arrays.asList("completed", "COMPLETED");
        List<HireRequest> hireRequests = hireRequestRepository
                .findByClientIdAndStatus(clientId, statuses);

        if (!hireRequests.isEmpty()) {
            List<ClientJobs> hireJobs = hireRequests.stream()
                    .map(this::mapToClientJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(hireJobs);
        }

        List<String> statuses2 = Arrays.asList("completed", "COMPLETED");
        List<ApplicationsForWantedAdvertisements> applications =
                applicationForWantedADRepository.findByClientIdAndStatus(clientId, statuses2);

        if (!applications.isEmpty()) {
            List<ClientJobs> applicationJobs = applications.stream()
                    .map(this::mapApplicationToClientJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(applicationJobs);
        }
        return allJobs;
    }

    @Transactional
    @Override
    public List<ClientJobs> getClientCancelledJobs(String clientId) {
        List<ClientJobs> allJobs = new ArrayList<>();

        List<String> statuses = Arrays.asList("CANCELLED", "cancelled");
        List<HireRequest> hireRequests = hireRequestRepository
                .findByClientIdAndStatus(clientId, statuses);

        if (!hireRequests.isEmpty()) {
            List<ClientJobs> hireJobs = hireRequests.stream()
                    .map(this::mapToClientJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(hireJobs);
        }

        List<String> statuses2 = Arrays.asList("cancelled", "CANCELLED");
        List<ApplicationsForWantedAdvertisements> applications =
                applicationForWantedADRepository.findByClientIdAndStatus(clientId, statuses2);

        if (!applications.isEmpty()) {
            List<ClientJobs> applicationJobs = applications.stream()
                    .map(this::mapApplicationToClientJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(applicationJobs);
        }
        return allJobs;
    }


    private ClientJobs mapToClientJobs(HireRequest hireRequest) {
        Worker worker = hireRequest.getWorker();

        String serviceProviderName = worker != null ?
                worker.getFirstName() + " " + worker.getLastName() :
                "Unknown Service Provider";

        String fullAddress = hireRequest.getLocation() != null ?
                hireRequest.getLocation() :
                "Address not available";

        String contactNumber = worker != null && worker.getPrimaryPhoneNumber() != null ?
                worker.getPrimaryPhoneNumber() :
                "Contact not available";

        String requestedService = hireRequest.getRequestedService() != null ?
                hireRequest.getRequestedService() :
                "Service not specified";

        String status = hireRequest.getStatus() != null ?
                hireRequest.getStatus() :
                "UNKNOWN";

        return new ClientJobs(
                hireRequest.getId(),
                requestedService,
                status,
                serviceProviderName,
                worker.getId(),
                hireRequest.getLocation(),
                hireRequest.getRequestedDate(),
                hireRequest.getCreatedAt(),
                contactNumber,
                hireRequest.getWorker().getProfileImageUrl()
        );
    }


    private ClientJobs mapApplicationToClientJobs(ApplicationsForWantedAdvertisements application) {
        ServiceWantedAdvertisement advertisement = application.getAdvertisement();
        Worker worker = application.getWorker();

        String serviceProviderName = worker != null ?
                worker.getFirstName() + " " + worker.getLastName() :
                "Unknown Service Provider";

        String requestedService = advertisement != null && advertisement.getTitle() != null ?
                advertisement.getTitle() :
                "Service not specified";

        String fullAddress = advertisement != null && advertisement.getFullAddress() != null ?
                advertisement.getFullAddress() :
                "Address not available";

        LocalDate requestedDate = advertisement != null ?
                advertisement.getRequiredDate() :
                null;

        String contactNumber = worker != null && worker.getPrimaryPhoneNumber() != null ?
                worker.getPrimaryPhoneNumber() :
                "Contact not available";

        String status = application.getStatus() != null ?
                application.getStatus() :
                "UNKNOWN";

        return new ClientJobs(
                null,
                requestedService,
                status,
                serviceProviderName,
                worker.getId(),
                fullAddress,
                requestedDate,
                application.getCreatedAt(),
                contactNumber,
                application.getWorker().getProfileImageUrl()
        );
    }
}
