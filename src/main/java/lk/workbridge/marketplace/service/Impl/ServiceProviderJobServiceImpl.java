package lk.workbridge.marketplace.service.Impl;

import jakarta.persistence.Entity;
import lk.workbridge.marketplace.dto.responses.ClientJobs;
import lk.workbridge.marketplace.dto.responses.ServiceProviderJobs;
import lk.workbridge.marketplace.entity.ApplicationsForWantedAdvertisements;
import lk.workbridge.marketplace.entity.HireRequest;
import lk.workbridge.marketplace.entity.ServiceWantedAdvertisement;
import lk.workbridge.marketplace.entity.Worker;
import lk.workbridge.marketplace.repository.ApplicationForWantedADRepository;
import lk.workbridge.marketplace.repository.HireRequestRepository;
import lk.workbridge.marketplace.service.ServiceProviderJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceProviderJobServiceImpl implements ServiceProviderJobService {
    private final ApplicationForWantedADRepository applicationForWantedADRepository;
    private final HireRequestRepository hireRequestRepository;

    @Override
    public List<ServiceProviderJobs> getServiceProviderOngoingJobs(String serviceProviderId, String jobStatus) {
        List<ServiceProviderJobs> allJobs = new ArrayList<>();


        List<String> statuses = Arrays.asList("INPROGRESS",jobStatus);
        List<HireRequest> hireRequests = hireRequestRepository
                .findByWorkerIdAndStatus(serviceProviderId, statuses);

        if (!hireRequests.isEmpty()) {
            List<ServiceProviderJobs> hireJobs = hireRequests.stream()
                    .map(this::mapToServiceProviderJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(hireJobs);
        }


        List<ApplicationsForWantedAdvertisements> applications =
                applicationForWantedADRepository.findByWorkerIdAndStatuses(serviceProviderId, statuses);

        if (!applications.isEmpty()) {
            List<ServiceProviderJobs> applicationJobs = applications.stream()
                    .map(this::mapApplicationToServiceProviderJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(applicationJobs);
        }
        return allJobs;
    }

    @Override
    public List<ServiceProviderJobs> getServiceProviderCompletedJobs(String serviceProviderId, String jobStatus) {
        List<ServiceProviderJobs> allJobs = new ArrayList<>();


        List<String> statuses = Arrays.asList(jobStatus, "CONFIRMED");
        List<HireRequest> hireRequests = hireRequestRepository
                .findByWorkerIdAndStatus(serviceProviderId, statuses);

        if (!hireRequests.isEmpty()) {
            List<ServiceProviderJobs> hireJobs = hireRequests.stream()
                    .map(this::mapToServiceProviderJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(hireJobs);
        }


        List<ApplicationsForWantedAdvertisements> applications =
                applicationForWantedADRepository.findByWorkerIdAndStatuses(serviceProviderId, statuses);

        if (!applications.isEmpty()) {
            List<ServiceProviderJobs> applicationJobs = applications.stream()
                    .map(this::mapApplicationToServiceProviderJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(applicationJobs);
        }
        return allJobs;
    }

    @Override
    public List<ServiceProviderJobs> getServiceProviderCancelledJobs(String serviceProviderId, String jobStatus) {
        List<ServiceProviderJobs> allJobs = new ArrayList<>();


        List<String> statuses = Arrays.asList(jobStatus, "CANCELLED");
        List<HireRequest> hireRequests = hireRequestRepository
                .findByWorkerIdAndStatus(serviceProviderId, statuses);

        if (!hireRequests.isEmpty()) {
            List<ServiceProviderJobs> hireJobs = hireRequests.stream()
                    .map(this::mapToServiceProviderJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(hireJobs);
        }


        List<ApplicationsForWantedAdvertisements> applications =
                applicationForWantedADRepository.findByWorkerIdAndStatuses(serviceProviderId, statuses);

        if (!applications.isEmpty()) {
            List<ServiceProviderJobs> applicationJobs = applications.stream()
                    .map(this::mapApplicationToServiceProviderJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(applicationJobs);
        }
        return allJobs;
    }

    private ServiceProviderJobs mapToServiceProviderJobs(HireRequest hireRequest) {
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

        String description = hireRequest.getDescription() != null ?
                hireRequest.getDescription() :
                "No description provided";

        return new ServiceProviderJobs(
                requestedService,
                status,
                serviceProviderName,
                fullAddress,
                hireRequest.getRequestedDate(),
                hireRequest.getCreatedAt(),
                contactNumber,
                description
        );
    }

    private ServiceProviderJobs mapApplicationToServiceProviderJobs(ApplicationsForWantedAdvertisements application) {
        ServiceWantedAdvertisement advertisement = application.getAdvertisement();
        Worker worker = application.getWorker();

        String serviceProviderName = worker != null ?
                worker.getFirstName() + " " + worker.getLastName() :
                "Unknown Service Provider";

        String requestedService = advertisement != null && advertisement.getTitle() != null ?
                advertisement.getTitle() :
                "Service not specified";

        // Using location from ServiceWantedAdvertisement
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

        String description = application.getMessage() != null ?
                application.getMessage() :
                "No description provided";

        return new ServiceProviderJobs(
                requestedService,
                status,
                serviceProviderName,
                fullAddress,
                requestedDate,
                application.getCreatedAt(),
                contactNumber,
                description
        );
    }
}
