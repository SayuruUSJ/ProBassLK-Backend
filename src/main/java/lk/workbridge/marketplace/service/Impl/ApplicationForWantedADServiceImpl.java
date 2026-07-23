package lk.workbridge.marketplace.service.Impl;

import jakarta.transaction.Transactional;
import lk.workbridge.marketplace.dto.ServiceProviderRequestForWantedAD;
import lk.workbridge.marketplace.dto.responses.ClientJobs;
import lk.workbridge.marketplace.dto.responses.WantedAdvertisementApplication;
import lk.workbridge.marketplace.entity.ApplicationRequestCancellationResult;
import lk.workbridge.marketplace.entity.ApplicationsForWantedAdvertisements;
import lk.workbridge.marketplace.entity.HireRequestCancellationResult;
import lk.workbridge.marketplace.entity.ServiceProviderAdvertisement;
import lk.workbridge.marketplace.entity.ServiceWantedAdvertisement;
import lk.workbridge.marketplace.entity.User;
import lk.workbridge.marketplace.entity.Worker;
import lk.workbridge.marketplace.repository.ApplicationForWantedADRepository;
import lk.workbridge.marketplace.repository.ApplicationRequestCancellationResultRepository;
import lk.workbridge.marketplace.repository.RatingRepository;
import lk.workbridge.marketplace.repository.ServiceProviderAdvertisementRepository;
import lk.workbridge.marketplace.repository.ServiceWantedAdvertisementRepository;
import lk.workbridge.marketplace.repository.UserRepository;
import lk.workbridge.marketplace.service.ApplicationForWantedADService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationForWantedADServiceImpl implements ApplicationForWantedADService {
    private final UserRepository userRepository;
    private final ServiceProviderAdvertisementRepository serviceProviderAdvertisementRepository;
    private final ApplicationForWantedADRepository applicationForWantedADRepository;
    private final ServiceWantedAdvertisementRepository serviceWantedAdvertisementRepository;
    private final RatingRepository ratingRepository;
    private final ApplicationRequestCancellationResultRepository applicationRequestCancellationResultRepository;

    @Override
    public String createNewRequest(ServiceProviderRequestForWantedAD requestForWantedAD) {

        try {

            User user = userRepository.findById(requestForWantedAD.getWorkerId())
                    .orElseThrow(() -> new RuntimeException("Worker not found."));

            Worker worker = (Worker) user;

            ServiceWantedAdvertisement advertisement =
                    serviceWantedAdvertisementRepository.findById(
                                    requestForWantedAD.getAdvertisement_id())
                            .orElseThrow(() -> new RuntimeException("Advertisement not found."));


            if (applicationForWantedADRepository.countRequest(
                    requestForWantedAD.getAdvertisement_id(),
                    requestForWantedAD.getWorkerId()) > 0) {

                throw new RuntimeException("You have already requested this advertisement.");
            }
            ServiceProviderAdvertisement serviceProviderAdvertisement = serviceProviderAdvertisementRepository.findAdvertisementByWorker(worker.getId())
                    .orElseThrow(()->new RuntimeException("provider ad not found"));

            if (!Objects.equals(serviceProviderAdvertisement.getStatus(), "PUBLISHED")) {

                throw new RuntimeException("You cant apply this advertisement because admin not approved your advertisement");
            }

            ApplicationsForWantedAdvertisements request = new ApplicationsForWantedAdvertisements();

            request.setAdvertisement(advertisement);
            request.setWorker(worker);
            request.setMessage(requestForWantedAD.getMessage());
            request.setProposedRate(requestForWantedAD.getDailyRate());
            request.setCreatedAt(LocalDate.now());
            request.setPaymentType(advertisement.getPaymentType());
            request.setStatus("PENDING");

            applicationForWantedADRepository.save(request);

            return "Request sent successfully.";

        } catch (DataAccessException e) {
            throw new RuntimeException("Database error occurred while sending the request.", e);
        }
    }

    @Override
    public String updateRequest(String ad_status, int request_id, String request_status) {
        if (!request_status.equalsIgnoreCase("ACCEPTED") &&
                !request_status.equalsIgnoreCase("REJECTED")) {

            throw new RuntimeException("Invalid status.");
        }
        if (!ad_status.equalsIgnoreCase("CONFIRMED") &&
                !ad_status.equalsIgnoreCase("PUBLISHED")) {

            throw new RuntimeException("Invalid status.");
        }


        ApplicationsForWantedAdvertisements advertisement =
                applicationForWantedADRepository.findById(request_id)
                        .orElseThrow(() ->
                                new RuntimeException("Advertisement not found."));
        ServiceWantedAdvertisement serviceWantedAdvertisement = serviceWantedAdvertisementRepository.findById(advertisement.getAdvertisement().getAdvertisement_id())
                .orElseThrow(() ->
                        new RuntimeException("Advertisement not found."));

        serviceWantedAdvertisement.setStatus(ad_status.toUpperCase());
        advertisement.setStatus(request_status.toUpperCase());
        applicationForWantedADRepository.save(advertisement);
        serviceWantedAdvertisementRepository.save(serviceWantedAdvertisement);

        return "request updated successfully";

    }

    @Transactional
    @Override
    public Page<WantedAdvertisementApplication> getClientApplications(String clientId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return applicationForWantedADRepository.findByClientId(clientId, pageable)
                .map(this::mapToWantedAdvertisementApplication);

    }

    @Override
    public List<ClientJobs> getClientOngoingApplications(String clientId, String jobStatus) {

        List<String> statuses2 = Arrays.asList("IN_PROGRESS", "CONFIRMED");
        List<ApplicationsForWantedAdvertisements> applications =
                applicationForWantedADRepository.findByClientIdAndStatus(clientId, statuses2);

        if (applications.isEmpty()) {
            return Collections.emptyList();
        }


        return applications.stream()
                .map(this::mapApplicationToClientJobs)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Boolean updateCompleteOrIncompleteJobs(int applicationId, String status) {
        if (!status.equalsIgnoreCase("COMPLETED") &&
                !status.equalsIgnoreCase("INCOMPLETED")) {

            throw new RuntimeException("Invalid status.");
        }

           

        ApplicationsForWantedAdvertisements applicationsForWantedAdvertisements = applicationForWantedADRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Advertisement not found."));
        String serviceWantedAdvertisementId = applicationsForWantedAdvertisements.getAdvertisement().getAdvertisement_id();
        ServiceWantedAdvertisement byId = serviceWantedAdvertisementRepository.findById(serviceWantedAdvertisementId)
                .orElseThrow(() -> new RuntimeException("Wanted advertisement not found"));
                 ServiceProviderAdvertisement serviceProviderAdvertisement = serviceProviderAdvertisementRepository.findAdvertisementByWorker(applicationsForWantedAdvertisements.getWorker().getId())
                .orElseThrow(() -> new RuntimeException("Advertisement not found"));
        applicationsForWantedAdvertisements.setStatus(status);
        byId.setStatus(status);
        byId.setUpdatedAt(LocalDate.now());
        if (status.equals("COMPLETED")) {
            Worker worker = applicationsForWantedAdvertisements.getWorker();
            worker.setAvailable(true);
            userRepository.save(worker);
                        serviceProviderAdvertisement.setStatus("PUBLISHED");
        }
        return true;
    }

    @Override
    public String cancelRequest(int id) {

        ApplicationsForWantedAdvertisements applicationsForWantedAdvertisements = applicationForWantedADRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Advertisement not found."));
        LocalDate currentDate = LocalDate.now();
        LocalDate requestedDate = applicationsForWantedAdvertisements.getAdvertisement().getRequiredDate();
        long daysUntilService = ChronoUnit.DAYS.between(currentDate, requestedDate);


        ApplicationRequestCancellationResult result = new ApplicationRequestCancellationResult();
        result.setRequest(applicationsForWantedAdvertisements);
        result.setCancelledAt(LocalDateTime.now());

        result.setDaysUntilService(daysUntilService);
        result.setStatus("PROCESSED");


        if (daysUntilService >= 5) {

            result.setMessage("Cancelled with 5+ days notice. No cancellation fee applied. Full refund.");
        } else if (daysUntilService >= 3) {

            result.setMessage("Cancelled with 3-4 days notice. Rs. 300 cancellation fee applied.");


        } else if (daysUntilService >= 1) {

            result.setMessage("Cancelled with 1-2 days notice. Rs. 500 cancellation fee applied.");


        } else {

            throw new RuntimeException(
                    "Cannot cancel this request. Service is scheduled for today (" + requestedDate +
                            ") or has already passed. Please contact support."
            );
        }

        applicationsForWantedAdvertisements.setStatus("CANCELLED");

        applicationForWantedADRepository.save(applicationsForWantedAdvertisements);
        applicationRequestCancellationResultRepository.save(result);
        return result.getMessage();
    }

    @Transactional
    @Override
    public Page<WantedAdvertisementApplication> getServiceProvidersSpecificApplications(String serviceProviderId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return applicationForWantedADRepository.findByWorkerId(serviceProviderId, pageable)
                .map(this::mapToWantedAdvertisementApplication);
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

        LocalDate createdAt = application.getCreatedAt();

        return new ClientJobs(
                requestedService,
                status,
                serviceProviderName,
                fullAddress,
                requestedDate,
                createdAt,
                contactNumber
        );
    }

    @Transactional
    private WantedAdvertisementApplication mapToWantedAdvertisementApplication(ApplicationsForWantedAdvertisements application) {
        ServiceWantedAdvertisement advertisement = application.getAdvertisement();
        Worker worker = application.getWorker();
        Double averageStars = ratingRepository.getAverageStarsByWorkerId(worker.getId());


        return new WantedAdvertisementApplication(
                application.getRequestId(),
                advertisement.getTitle(),


                worker.getFirstName() + " " + worker.getLastName(),


                worker.getTitle(),


                worker.getOverallExperience(),


                worker.getVerificationStatus() != null && worker.getVerificationStatus(),


                advertisement.getDescription(),


                advertisement.getAdditionalInstructions(),

                advertisement.getServiceType(),


                advertisement.isRateNegotiable(),


                application.getProposedRate(),


                application.getPaymentType(),


                application.getStatus(),

                averageStars,

                worker.getId(),
                application.getAdvertisement().getAdvertisement_id(),
                application.getCreatedAt()
        );

    }

}
