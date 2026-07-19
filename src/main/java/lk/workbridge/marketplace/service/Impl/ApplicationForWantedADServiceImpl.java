package lk.workbridge.marketplace.service.Impl;

import jakarta.transaction.Transactional;
import lk.workbridge.marketplace.dto.ServiceProviderRequestForWantedAD;
import lk.workbridge.marketplace.dto.responses.WantedAdvertisementApplication;
import lk.workbridge.marketplace.entity.ApplicationsForWantedAdvertisements;
import lk.workbridge.marketplace.entity.ServiceWantedAdvertisement;
import lk.workbridge.marketplace.entity.User;
import lk.workbridge.marketplace.entity.Worker;
import lk.workbridge.marketplace.repository.ApplicationForWantedADRepository;
import lk.workbridge.marketplace.repository.RatingRepository;
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

@Service
@RequiredArgsConstructor
public class ApplicationForWantedADServiceImpl implements ApplicationForWantedADService {
    private final UserRepository userRepository;
    private final ApplicationForWantedADRepository applicationForWantedADRepository;
    private final ServiceWantedAdvertisementRepository serviceWantedAdvertisementRepository;
    private final RatingRepository ratingRepository;

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

            ApplicationsForWantedAdvertisements request = new ApplicationsForWantedAdvertisements();

            request.setAdvertisement(advertisement);
            request.setWorker(worker);
            request.setMessage(requestForWantedAD.getMessage());
            request.setProposedRate(requestForWantedAD.getDailyRate());
            request.setCreatedAt(LocalDate.now());
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

    @Transactional
    private WantedAdvertisementApplication mapToWantedAdvertisementApplication(ApplicationsForWantedAdvertisements application) {
        ServiceWantedAdvertisement advertisement = application.getAdvertisement();
        Worker worker = application.getWorker();
        Double averageStars = ratingRepository.getAverageStarsByWorkerId(worker.getId());


        return new WantedAdvertisementApplication(

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

                worker.getId()
        );

    }

}
