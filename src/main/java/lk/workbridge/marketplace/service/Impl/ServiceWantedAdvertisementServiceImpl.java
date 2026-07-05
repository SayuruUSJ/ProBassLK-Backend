package lk.workbridge.marketplace.service.Impl;

import lk.workbridge.marketplace.dto.ServiceWantedAD;
import lk.workbridge.marketplace.entity.Client;
import lk.workbridge.marketplace.entity.ClientBookingRequestedAdvertisement;
import lk.workbridge.marketplace.entity.ServiceWantedAdvertisement;
import lk.workbridge.marketplace.entity.User;
import lk.workbridge.marketplace.entity.Worker;
import lk.workbridge.marketplace.repository.ServiceWantedAdvertisementRepository;
import lk.workbridge.marketplace.repository.UserRepository;
import lk.workbridge.marketplace.service.ServiceWantedAdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceWantedAdvertisementServiceImpl implements ServiceWantedAdvertisementService {

    private final ServiceWantedAdvertisementRepository serviceWantedAdvertisementRepository;
    private final UserRepository userRepository;
    @Override
    public String requestAdvertisement(ServiceWantedAD serviceWantedAD) {
        try {

            User userTwo=userRepository.findById(serviceWantedAD.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client not found."));
            Client client=(Client)userTwo;
            ServiceWantedAdvertisement advertisement = new ServiceWantedAdvertisement();

            advertisement.setServiceType(serviceWantedAD.getServiceType());
            advertisement.setRequiredDate(serviceWantedAD.getRequiredDate());
            advertisement.setClient(client);
            advertisement.setClientContactNumber(serviceWantedAD.getClientContactNumber());
            advertisement.setClientContactNumber(serviceWantedAD.getClientContactNumber());
            advertisement.setLocation(serviceWantedAD.getLocation());
            advertisement.setDescription(serviceWantedAD.getDescription());
            advertisement.setStatus("PENDING");


                serviceWantedAdvertisementRepository.save(advertisement);
                return "Advertisement request sent successfully.";

        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while saving advertisement.", e);
        }
    }


}
