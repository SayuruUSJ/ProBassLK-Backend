package lk.workbridge.marketplace.service.Impl;

import lk.workbridge.marketplace.dto.ServiceProviderRequestForWantedAD;
import lk.workbridge.marketplace.entity.ClientBookingRequestedAdvertisement;
import lk.workbridge.marketplace.entity.ServiceProvidersRequestsForWantedAdvertisements;
import lk.workbridge.marketplace.entity.ServiceWantedAdvertisement;
import lk.workbridge.marketplace.entity.User;
import lk.workbridge.marketplace.entity.Worker;
import lk.workbridge.marketplace.repository.ServiceProviderRequestForWantedADRepository;
import lk.workbridge.marketplace.repository.ServiceWantedAdvertisementRepository;
import lk.workbridge.marketplace.repository.UserRepository;
import lk.workbridge.marketplace.service.ServiceProviderRequestForWantedADService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceProviderRequestForWantedADServiceImpl implements ServiceProviderRequestForWantedADService {
    private final UserRepository userRepository;
    private final ServiceProviderRequestForWantedADRepository serviceProviderRequestRepository;
    private final ServiceWantedAdvertisementRepository serviceWantedAdvertisementRepository;

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


            if (serviceProviderRequestRepository.countRequest(
        requestForWantedAD.getAdvertisement_id(),
        requestForWantedAD.getWorkerId()) > 0) {

    throw new RuntimeException("You have already requested this advertisement.");
}

            ServiceProvidersRequestsForWantedAdvertisements request =new ServiceProvidersRequestsForWantedAdvertisements();

            request.setAdvertisement(advertisement);
            request.setWorker(worker);
            request.setMessage(requestForWantedAD.getMessage());
            request.setDailyRate(requestForWantedAD.getDailyRate());
            request.setStatus("PENDING");

            serviceProviderRequestRepository.save(request);

            return "Request sent successfully.";

        } catch (DataAccessException e) {
            throw new RuntimeException("Database error occurred while sending the request.", e);
        }
    }

    @Override
    public String updateRequest(String ad_status,int request_id,String request_status) {
        if (!request_status.equalsIgnoreCase("ACCEPTED") &&
                !request_status.equalsIgnoreCase("REJECTED")) {

            throw new RuntimeException("Invalid status.");
        }

        ServiceProvidersRequestsForWantedAdvertisements advertisement =
                serviceProviderRequestRepository.findById(request_id)
                        .orElseThrow(() ->
                                new RuntimeException("Advertisement not found."));
   ServiceWantedAdvertisement serviceWantedAdvertisement= serviceWantedAdvertisementRepository.findById(advertisement.getAdvertisement().getAdvertisement_id())
                .orElseThrow(() ->
                        new RuntimeException("Advertisement not found."));

   serviceWantedAdvertisement.setStatus(ad_status.toUpperCase());
        advertisement.setStatus(request_status.toUpperCase());
        serviceProviderRequestRepository.save(advertisement);
        serviceWantedAdvertisementRepository.save(serviceWantedAdvertisement);

        return "request updated successfully";

    }

}
