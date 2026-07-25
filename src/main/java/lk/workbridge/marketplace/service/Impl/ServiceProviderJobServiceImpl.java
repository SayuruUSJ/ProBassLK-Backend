package lk.workbridge.marketplace.service.Impl;

import jakarta.transaction.Transactional;
import lk.workbridge.marketplace.dto.responses.ServiceProviderJobs;
import lk.workbridge.marketplace.entity.ApplicationsForWantedAdvertisements;
import lk.workbridge.marketplace.entity.HireRequest;
import lk.workbridge.marketplace.entity.ServiceProviderAdvertisement;
import lk.workbridge.marketplace.entity.ServiceWantedAdvertisement;
import lk.workbridge.marketplace.entity.User;
import lk.workbridge.marketplace.entity.Worker;
import lk.workbridge.marketplace.repository.ApplicationForWantedADRepository;
import lk.workbridge.marketplace.repository.HireRequestRepository;
import lk.workbridge.marketplace.repository.ServiceProviderAdvertisementRepository;
import lk.workbridge.marketplace.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final ServiceProviderAdvertisementRepository serviceProviderAdvertisementRepository;

    @Transactional
    @Override
    public List<ServiceProviderJobs> getServiceProviderOngoingJobs(String serviceProviderId) {
        List<ServiceProviderJobs> allJobs = new ArrayList<>();

        List<String> statuses = Arrays.asList("IN_PROGRESS", "ACCEPTED");
        List<HireRequest> hireRequests = hireRequestRepository
                .findByWorkerIdAndStatus(serviceProviderId, statuses);

        if (!hireRequests.isEmpty()) {
            List<ServiceProviderJobs> hireJobs = hireRequests.stream()
                    .map(this::mapToServiceProviderJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(hireJobs);
        }

        List<String> statuses2 = Arrays.asList("IN_PROGRESS", "CONFIRMED");
        List<ApplicationsForWantedAdvertisements> applications =
                applicationForWantedADRepository.findByWorkerIdAndStatuses(serviceProviderId, statuses2);

        if (!applications.isEmpty()) {
            List<ServiceProviderJobs> applicationJobs = applications.stream()
                    .map(this::mapApplicationToServiceProviderJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(applicationJobs);
        }
        return allJobs;
    }

    @Transactional
    @Override
    public List<ServiceProviderJobs> getServiceProviderCompletedJobs(String serviceProviderId) {
        List<ServiceProviderJobs> allJobs = new ArrayList<>();

        List<String> statuses = Arrays.asList("completed", "COMPLETED");
        List<HireRequest> hireRequests = hireRequestRepository
                .findByWorkerIdAndStatus(serviceProviderId, statuses);

        if (!hireRequests.isEmpty()) {
            List<ServiceProviderJobs> hireJobs = hireRequests.stream()
                    .map(this::mapToServiceProviderJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(hireJobs);
        }

        List<String> statuses2 = Arrays.asList("completed", "COMPLETED");
        List<ApplicationsForWantedAdvertisements> applications =
                applicationForWantedADRepository.findByWorkerIdAndStatuses(serviceProviderId, statuses2);

        if (!applications.isEmpty()) {
            List<ServiceProviderJobs> applicationJobs = applications.stream()
                    .map(this::mapApplicationToServiceProviderJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(applicationJobs);
        }
        return allJobs;
    }

    @Transactional
    @Override
    public List<ServiceProviderJobs> getServiceProviderCancelledJobs(String serviceProviderId) {
        List<ServiceProviderJobs> allJobs = new ArrayList<>();


        List<String> statuses = Arrays.asList("CANCELLED", "cancelled");
        List<HireRequest> hireRequests = hireRequestRepository
                .findByWorkerIdAndStatus(serviceProviderId, statuses);

        if (!hireRequests.isEmpty()) {
            List<ServiceProviderJobs> hireJobs = hireRequests.stream()
                    .map(this::mapToServiceProviderJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(hireJobs);
        }

        List<String> statuses2 = Arrays.asList("cancelled", "CANCELLED");
        List<ApplicationsForWantedAdvertisements> applications =
                applicationForWantedADRepository.findByWorkerIdAndStatuses(serviceProviderId, statuses2);

        if (!applications.isEmpty()) {
            List<ServiceProviderJobs> applicationJobs = applications.stream()
                    .map(this::mapApplicationToServiceProviderJobs)
                    .collect(Collectors.toList());
            allJobs.addAll(applicationJobs);
        }
        return allJobs;
    }

    @Transactional
    @Override
    public String handleServiceProviderAndAdvertisementAvailability(String serviceProviderId, String hireRequestId) {
        User user = userRepository.findById(serviceProviderId).orElseThrow(() -> new RuntimeException("User not found."));
        Worker worker = (Worker) user;
        worker.setAvailable(Boolean.FALSE);
        ServiceProviderAdvertisement advertisement = serviceProviderAdvertisementRepository.findAdvertisementByWorker(serviceProviderId)
                .orElseThrow(() -> new RuntimeException("Advertisement not found"));
        HireRequest hireRequest = hireRequestRepository.findById(hireRequestId)
                .orElseThrow(() -> new RuntimeException("Hire request not found"));
        advertisement.setStatus("NOT_AVAILABLE");
        hireRequest.setStatus("IN_PROGRESS");
        advertisement.setUpdatedAt(LocalDate.now());
        userRepository.save(worker);
        serviceProviderAdvertisementRepository.save(advertisement);
        hireRequestRepository.save(hireRequest);
        return "updated status successfully";
    }

    @Transactional
    @Override
    public String handleServiceWantedAdvertisement(int requestId, String serviceProviderId) {
        User user = userRepository.findById(serviceProviderId).orElseThrow(() -> new RuntimeException("User not found."));
        Worker worker = (Worker) user;
        worker.setAvailable(Boolean.FALSE);
        ApplicationsForWantedAdvertisements advertisement = applicationForWantedADRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Advertisement not found"));
        ServiceProviderAdvertisement serviceProviderAdvertisement = serviceProviderAdvertisementRepository.findAdvertisementByWorker(serviceProviderId)
                .orElseThrow(() -> new RuntimeException("Advertisement not found"));
        advertisement.setStatus("IN_PROGRESS");
        advertisement.getAdvertisement().setUpdatedAt(LocalDate.now());
        advertisement.getAdvertisement().setStatus("IN_PROGRESS");
        serviceProviderAdvertisement.setStatus("NOT_AVAILABLE");
        userRepository.save(worker);
        applicationForWantedADRepository.save(advertisement);
        return "Updated application and user status successfully";
    }

    private ServiceProviderJobs mapToServiceProviderJobs(HireRequest hireRequest) {
        Worker worker = hireRequest.getWorker();

        String serviceProviderName = worker != null ?
                worker.getFirstName() + " " + worker.getLastName() :
                "Unknown Service Provider";

        String clientName = hireRequest.getClientName();
        String fullAddress = hireRequest.getLocation() != null ?
                hireRequest.getLocation() :
                "Address not available";

        String contactNumber = hireRequest.getClientContactNumber();


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
                clientName,
                fullAddress,
                hireRequest.getRequestedDate(),
                hireRequest.getCreatedAt(),
                contactNumber,
                description,
                hireRequest.getId(),
                0,
                hireRequest.getClient().getProfileImageUrl()
        );
    }

    private ServiceProviderJobs mapApplicationToServiceProviderJobs(ApplicationsForWantedAdvertisements application) {
        ServiceWantedAdvertisement advertisement = application.getAdvertisement();
        Worker worker = application.getWorker();

        String serviceProviderName = worker != null ?
                worker.getFirstName() + " " + worker.getLastName() :
                "Unknown Service Provider";

        String clientName = application.getAdvertisement().getClient().getFirstName() + "" + application.getAdvertisement().getClient().getLastName();

        String requestedService = advertisement != null && advertisement.getTitle() != null ?
                advertisement.getTitle() :
                "Service not specified";


        String fullAddress = advertisement != null && advertisement.getFullAddress() != null ?
                advertisement.getFullAddress() :
                "Address not available";

        LocalDate requestedDate = advertisement != null ?
                advertisement.getRequiredDate() :
                null;

        String contactNumber = application.getAdvertisement().getClientContactNumber();

        String status = application.getStatus() != null ?
                application.getStatus() :
                "UNKNOWN";

        String description = application.getMessage() != null ?
                application.getMessage() :
                "No description provided";

        return new ServiceProviderJobs(
                requestedService,
                status,
                clientName,
                fullAddress,
                requestedDate,
                application.getCreatedAt(),
                contactNumber,
                description,
                null,
                application.getRequestId(),
                application.getAdvertisement().getClient().getProfileImageUrl()
        );
    }
}
