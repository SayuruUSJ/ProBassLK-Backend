package lk.workbridge.marketplace.service.Impl;

import lk.workbridge.marketplace.dto.responses.ClientJobs;
import lk.workbridge.marketplace.entity.ApplicationsForWantedAdvertisements;
import lk.workbridge.marketplace.entity.HireRequest;
import lk.workbridge.marketplace.entity.ServiceWantedAdvertisement;
import lk.workbridge.marketplace.entity.Worker;
import lk.workbridge.marketplace.repository.ApplicationForWantedADRepository;
import lk.workbridge.marketplace.repository.HireRequestRepository;
import lk.workbridge.marketplace.service.ClientJobsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ClientJobsServiceImpl implements ClientJobsService {
    private final ApplicationForWantedADRepository applicationForWantedADRepository;
    private final HireRequestRepository hireRequestRepository;

    @Override
    public List<ClientJobs> getClientOngoingJobs(String clientId, String jobStatus) {
        List<ClientJobs> allJobs = new ArrayList<>();

        if (!jobStatus.equalsIgnoreCase("CONFIRMED")) {

            throw new RuntimeException("Invalid status.");
        }
        List<HireRequest> hireRequests = hireRequestRepository
                .findByClientIdAndStatus(clientId, jobStatus);

        if (!hireRequests.isEmpty()) {
            List<ClientJobs> hireJobs = hireRequests.stream()
                    .map(this::mapToClientJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(hireJobs);
        }


        List<ApplicationsForWantedAdvertisements> applications =
                applicationForWantedADRepository.findByClientIdAndStatus(clientId, jobStatus);

        if (!applications.isEmpty()) {
            List<ClientJobs> applicationJobs = applications.stream()
                    .map(this::mapApplicationToClientJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(applicationJobs);
        }
       return allJobs;
    }

    @Override
    public List<ClientJobs> getClientCompletedJobs(String clientId, String jobStatus) {
        List<ClientJobs> allJobs = new ArrayList<>();
        if (!jobStatus.equalsIgnoreCase("COMPLETED")) {

            throw new RuntimeException("Invalid status.");
        }
        List<HireRequest> hireRequests = hireRequestRepository
                .findByClientIdAndStatus(clientId, jobStatus);

        if (!hireRequests.isEmpty()) {
            List<ClientJobs> hireJobs = hireRequests.stream()
                    .map(this::mapToClientJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(hireJobs);
        }


        List<ApplicationsForWantedAdvertisements> applications =
                applicationForWantedADRepository.findByClientIdAndStatus(clientId, jobStatus);

        if (!applications.isEmpty()) {
            List<ClientJobs> applicationJobs = applications.stream()
                    .map(this::mapApplicationToClientJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(applicationJobs);
        }
        return allJobs;
    }

    @Override
    public List<ClientJobs> getClientCancelledJobs(String clientId, String jobStatus) {
        List<ClientJobs> allJobs = new ArrayList<>();
        if (!jobStatus.equalsIgnoreCase("CANCELLED")) {

            throw new RuntimeException("Invalid status.");
        }

        List<HireRequest> hireRequests = hireRequestRepository
                .findByClientIdAndStatus(clientId, jobStatus);

        if (!hireRequests.isEmpty()) {
            List<ClientJobs> hireJobs = hireRequests.stream()
                    .map(this::mapToClientJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(hireJobs);
        }


        List<ApplicationsForWantedAdvertisements> applications =
                applicationForWantedADRepository.findByClientIdAndStatus(clientId, jobStatus);

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
                requestedService,
                status,
                serviceProviderName,
                fullAddress,
                hireRequest.getRequestedDate(),
                hireRequest.getCreatedAt(),
                contactNumber
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
                requestedService,
                status,
                serviceProviderName,
                fullAddress,
                requestedDate,
                application.getCreatedAt(),
                contactNumber
        );
    }
}
