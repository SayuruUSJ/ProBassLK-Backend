package lk.workbridge.marketplace.service.Impl;

import lk.workbridge.marketplace.dto.ClientBookingRequestAD;
import lk.workbridge.marketplace.entity.Client;
import lk.workbridge.marketplace.entity.ClientBookingRequestedAdvertisement;
import lk.workbridge.marketplace.entity.User;
import lk.workbridge.marketplace.entity.Worker;
import lk.workbridge.marketplace.repository.ClientBookingRequestAdvertisementRepository;
import lk.workbridge.marketplace.repository.UserRepository;
import lk.workbridge.marketplace.service.ClientBookingRequestAdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ClientBookingBookingRequestAdvertisementServiceImpl implements ClientBookingRequestAdvertisementService {
    private final ClientBookingRequestAdvertisementRepository clientRequestAdvertisementRepository;
    private final UserRepository userRepository;
    @Override
    public String requestAdvertisement(ClientBookingRequestAD requestAD) {
        try {
            User userOne=userRepository.findById(requestAD.getWorkerId())
                    .orElseThrow(() -> new RuntimeException("Worker not found."));
            Worker worker=(Worker) userOne;
            User userTwo=userRepository.findById(requestAD.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client not found."));
            Client client=(Client)userTwo;
            ClientBookingRequestedAdvertisement advertisement = new ClientBookingRequestedAdvertisement();

            advertisement.setWorker(worker);
            advertisement.setWorkerEmail(requestAD.getWorkerEmail());
            advertisement.setRequestedService(requestAD.getRequestedService());
            advertisement.setRequestedDate(requestAD.getRequestedDate());
            advertisement.setClient(client);
            advertisement.setClientName(requestAD.getClientName());
            advertisement.setClientContactNumber(requestAD.getClientContactNumber());
            advertisement.setLocation(requestAD.getLocation());
            advertisement.setDescription(requestAD.getDescription());
            advertisement.setStatus("PENDING");

            if (clientRequestAdvertisementRepository.existsByWorkerIdAndRequestedDate(
                    requestAD.getWorkerId(),
                    requestAD.getRequestedDate())) {

                throw new RuntimeException("Worker is already booked for this date.");
            } else {
                clientRequestAdvertisementRepository.save(advertisement);
                return "Advertisement request sent successfully.";
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

        ClientBookingRequestedAdvertisement advertisement =
                clientRequestAdvertisementRepository.findById(advertisementId)
                        .orElseThrow(() ->
                                new RuntimeException("Advertisement not found."));

        advertisement.setStatus(status.toUpperCase());

        clientRequestAdvertisementRepository.save(advertisement);
        return true;
    }

    @Override
    public List<ClientBookingRequestedAdvertisement> getAllRequestsByWorkerId(String workerId) {

        List<ClientBookingRequestedAdvertisement> requestedAdvertisements=clientRequestAdvertisementRepository.findByWorkerId(workerId);
        if(requestedAdvertisements.isEmpty()) {
            throw new RuntimeException("No requests found for the given worker ID.");
        }
        return requestedAdvertisements;
    }

    @Override
    public Boolean updateCompleteOrIncompleteJobs(String advertisementId, String status) {
        if (!status.equalsIgnoreCase("COMPLETED") &&
                !status.equalsIgnoreCase("INCOMPLETE")) {

            throw new RuntimeException("Invalid status.");
        }

        ClientBookingRequestedAdvertisement advertisement =
                clientRequestAdvertisementRepository.findById(advertisementId)
                        .orElseThrow(() ->
                                new RuntimeException("Advertisement not found."));

        advertisement.setStatus(status.toUpperCase());

        clientRequestAdvertisementRepository.save(advertisement);
        return true;
    }


}
