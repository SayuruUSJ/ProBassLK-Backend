package lk.workbridge.marketplace.service.Impl;

import lk.workbridge.marketplace.dto.HireRequestAD;
import lk.workbridge.marketplace.dto.responses.ClientJobs;
import lk.workbridge.marketplace.dto.responses.HireRequestCreatedResponse;
import lk.workbridge.marketplace.dto.responses.HireRequestResponse;
import lk.workbridge.marketplace.entity.HireRequestCancellationResult;
import lk.workbridge.marketplace.entity.Client;
import lk.workbridge.marketplace.entity.HireRequest;
import lk.workbridge.marketplace.entity.ServiceProviderAdvertisement;
import lk.workbridge.marketplace.entity.User;
import lk.workbridge.marketplace.entity.Worker;
import lk.workbridge.marketplace.repository.HireRequestCancellationResultRepository;
import lk.workbridge.marketplace.repository.HireRequestRepository;
import lk.workbridge.marketplace.repository.ServiceProviderAdvertisementRepository;
import lk.workbridge.marketplace.repository.UserRepository;
import lk.workbridge.marketplace.service.HireRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HireRequestServiceImpl implements HireRequestService {
    private final HireRequestRepository hireRequestRepository;
    private final UserRepository userRepository;
    private final ServiceProviderAdvertisementRepository serviceProviderAdvertisementRepository;
    private final HireRequestCancellationResultRepository hireRequestCancellationResultRepository;

    @Override
    public HireRequestCreatedResponse requestAdvertisement(HireRequestAD requestAD) {
        try {
            User userOne = userRepository.findById(requestAD.getWorkerId())
                    .orElseThrow(() -> new RuntimeException("Worker not found."));
            Worker worker = (Worker) userOne;
            User userTwo = userRepository.findById(requestAD.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client not found."));
            Client client = (Client) userTwo;
            HireRequest advertisement = new HireRequest();

            ServiceProviderAdvertisement serviceProviderAdvertisement = serviceProviderAdvertisementRepository.findById(requestAD.getAdvertisementId())
                    .orElseThrow(() -> new RuntimeException("Advertisement not found."));
            advertisement.setWorker(worker);
            advertisement.setServiceProviderAdvertisement(serviceProviderAdvertisement);
            advertisement.setWorkerEmail(requestAD.getWorkerEmail());
            advertisement.setRequestedService(requestAD.getRequestedService());
            advertisement.setRequestedDate(requestAD.getRequestedDate());
            advertisement.setClient(client);
            advertisement.setClientName(requestAD.getClientName());
            advertisement.setClientContactNumber(requestAD.getClientContactNumber());
            advertisement.setLocation(requestAD.getLocation());
            advertisement.setDescription(requestAD.getDescription());
            advertisement.setCreatedAt(LocalDate.now());
            advertisement.setRateForRequiredService(requestAD.getRateForRequiredService());
            advertisement.setStatus("PENDING");

            if (hireRequestRepository.existsByWorkerIdAndRequestedDate(
                    requestAD.getWorkerId(),
                    requestAD.getRequestedDate())) {

                throw new RuntimeException("Worker is already booked for this date.");
            } else {
                hireRequestRepository.save(advertisement);
                return new HireRequestCreatedResponse(
                        advertisement.getId(),
                        advertisement.getStatus(),
                        "advertisement created successfully"
                );
            }


        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while saving advertisement.", e);
        }
    }

    @Override
    public Boolean acceptOrReject(String advertisementId, String status) {
        if (!status.equalsIgnoreCase("ACCEPTED") &&
                !status.equalsIgnoreCase("REJECTED")) {

            throw new RuntimeException("Invalid status.");
        }

        HireRequest advertisement =
                hireRequestRepository.findById(advertisementId)
                        .orElseThrow(() ->
                                new RuntimeException("Advertisement not found."));


        advertisement.setStatus(status.toUpperCase());

        hireRequestRepository.save(advertisement);
        return true;
    }

    @Override
    public List<HireRequestResponse> getAllRequestsByWorkerId(String workerId) {

        List<HireRequest> requestedAdvertisements = hireRequestRepository.findByWorkerId(workerId);
        System.out.println("Number of records found: " + requestedAdvertisements.size());
        System.out.println("WorkerId: " + workerId);
        if (requestedAdvertisements.isEmpty()) {
            throw new RuntimeException("No requests found for the given worker ID.");
        }
        return requestedAdvertisements
                .stream()
                .map(this::mapToHireRequestResponse)
                .collect(Collectors.toList())
                ;

    }

    @Override
    public List<HireRequestResponse> getAllPendingRequestsByWorkerId(String workerId) {
        List<String> statuses = Arrays.asList("pending", "PENDING");
        List<HireRequest> hireRequests = hireRequestRepository.findByWorkerIdAndStatus(workerId, statuses);

        if (hireRequests.isEmpty()) {
            throw new RuntimeException("No requests found for the given worker ID.");
        }
        return hireRequests
                .stream()
                .map(this::mapToHireRequestResponse)
                .collect(Collectors.toList())
                ;
    }

    @Override
    public List<HireRequestResponse> getAllAcceptedRequestsByWorkerId(String workerId) {
        List<String> statuses = Arrays.asList("accepted", "ACCEPTED");
        List<HireRequest> hireRequests = hireRequestRepository.findByWorkerIdAndStatus(workerId, statuses);

        if (hireRequests.isEmpty()) {
            throw new RuntimeException("No requests found for the given worker ID.");
        }
        return hireRequests
                .stream()
                .map(this::mapToHireRequestResponse)
                .collect(Collectors.toList())
                ;
    }

    @Override
    public List<HireRequestResponse> getAllRejectedRequestsByWorkerId(String workerId) {
        List<String> statuses = Arrays.asList("rejected", "REJECTED");
        List<HireRequest> hireRequests = hireRequestRepository.findByWorkerIdAndStatus(workerId, statuses);

        if (hireRequests.isEmpty()) {
            throw new RuntimeException("No requests found for the given worker ID.");
        }
        return hireRequests
                .stream()
                .map(this::mapToHireRequestResponse)
                .collect(Collectors.toList())
                ;
    }

    private HireRequestResponse mapToHireRequestResponse(HireRequest hireRequest) {
        return new HireRequestResponse(
                hireRequest.getId(),
                hireRequest.getClientName(),
                hireRequest.getRequestedService(),
                hireRequest.getDescription(),
                hireRequest.getLocation(),
                hireRequest.getRequestedDate(),
                hireRequest.getCreatedAt(),
                hireRequest.getRateForRequiredService(),
                hireRequest.getStatus()

        );
    }

    @Transactional
@Override
public Boolean updateCompleteOrIncompleteJobs(String advertisementId, String status) {
    try {
        // Validate status
        if (!"COMPLETED".equalsIgnoreCase(status) && !"INCOMPLETED".equalsIgnoreCase(status)) {
            throw new IllegalArgumentException("Invalid status. Allowed values: COMPLETED, INCOMPLETED");
        }

        // Find hire request
        HireRequest hireRequest = hireRequestRepository.findById(advertisementId)
                .orElseThrow(() -> new RuntimeException("Advertisement not found with ID: " + advertisementId));

        String statusUpperCase = status.toUpperCase();
        hireRequest.setStatus(statusUpperCase);

        if ("COMPLETED".equalsIgnoreCase(status)) {
            Worker worker = hireRequest.getWorker();
            if (worker == null) {
                throw new RuntimeException("Worker not associated with this advertisement");
            }

            // Update worker availability
            worker.setAvailable(true);
            userRepository.save(worker);

            // Update service provider advertisement
            ServiceProviderAdvertisement serviceProviderAdvertisement = 
                    serviceProviderAdvertisementRepository
                            .findAdvertisementByWorker(worker.getId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Service provider advertisement not found for worker: " + worker.getId()));
            serviceProviderAdvertisement.setStatus("PUBLISHED");
            serviceProviderAdvertisement.setUpdatedAt(LocalDate.now());
            serviceProviderAdvertisementRepository.save(serviceProviderAdvertisement);
        }

        hireRequestRepository.save(hireRequest);
        return true;
        
    } catch (Exception e) {
        // Log the error
        System.err.println("Error updating advertisement status: " + e.getMessage());
        throw new RuntimeException("Failed to update advertisement status: " + e.getMessage());
    }
}

    @Override
    public String cancelRequest(String id) {

        HireRequest request = hireRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Advertisement not found."));
        LocalDate currentDate = LocalDate.now();
        LocalDate requestedDate = request.getRequestedDate();
        long daysUntilService = ChronoUnit.DAYS.between(currentDate, requestedDate);


        HireRequestCancellationResult result = new HireRequestCancellationResult();
        result.setRequest(request);
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

        request.setStatus("CANCELLED");

        hireRequestCancellationResultRepository.save(result);
        hireRequestRepository.save(request);

        // 8. Send notifications
        // notificationService.sendCancellationNotification(request, result);

        // 9. If refund, process it
//            if (result.getIsRefundProcessed() && result.getRefundAmount() > 0) {
//                processRefund(request, result);
//            }
//
//            log.info("Request {} cancelled. Type: {}, Fee: {}, Refund: {}, Days: {}",
//                    id, result.getCancellationType(), result.getCancellationFee(),
//                    result.getRefundAmount(), daysUntilService);

        return result.getMessage();
    }

    @Override
    public List<ClientJobs> getClientOngoingJobs(String clientId, String jobStatus) {

        List<String> statuses = Arrays.asList("IN_PROGRESS", jobStatus);
        List<HireRequest> hireRequests = hireRequestRepository
                .findByClientIdAndStatus(clientId, statuses);

        if (hireRequests.isEmpty()) {
            return Collections.emptyList();
        }

        return hireRequests.stream()
                .map(this::mapToClientJobs)
                .collect(Collectors.toList());
    }


    private ClientJobs mapToClientJobs(HireRequest hireRequest) {

        Worker worker = hireRequest.getWorker();

        String serviceProviderName = worker.getFirstName() + " " + worker.getLastName();


        String fullAddress = hireRequest.getLocation();


        String contactNumber = worker.getPrimaryPhoneNumber() != null ?
                worker.getPrimaryPhoneNumber() :
                "Contact not available";


        String requestedService = hireRequest.getRequestedService();


        String status = hireRequest.getStatus() != null ?
                hireRequest.getStatus() :
                "UNKNOWN";


        LocalDate requestedDate = hireRequest.getRequestedDate();
        LocalDate createdAt = hireRequest.getCreatedAt();


        return new ClientJobs(
                hireRequest.getId(),
                requestedService,
                status,
                serviceProviderName,
                fullAddress,
                requestedDate,
                createdAt,
                contactNumber
        );
    }

}
