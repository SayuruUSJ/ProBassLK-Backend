package lk.workbridge.marketplace.service.Impl;

import lk.workbridge.marketplace.dto.ServiceProviderADResponse;
import lk.workbridge.marketplace.dto.ServiceWantedAD;
import lk.workbridge.marketplace.dto.ServiceWantedADResponse;
import lk.workbridge.marketplace.dto.WorkerSkillResponse;
import lk.workbridge.marketplace.entity.Client;
import lk.workbridge.marketplace.entity.ClientBookingRequestedAdvertisement;
import lk.workbridge.marketplace.entity.ServiceProviderAdvertisement;
import lk.workbridge.marketplace.entity.ServiceWantedAdvertisement;
import lk.workbridge.marketplace.entity.User;
import lk.workbridge.marketplace.entity.Worker;
import lk.workbridge.marketplace.repository.ServiceProviderRequestForWantedADRepository;
import lk.workbridge.marketplace.repository.ServiceWantedAdvertisementRepository;
import lk.workbridge.marketplace.repository.UserRepository;
import lk.workbridge.marketplace.service.ServiceWantedAdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceWantedAdvertisementServiceImpl implements ServiceWantedAdvertisementService {

    private final ServiceWantedAdvertisementRepository serviceWantedAdvertisementRepository;
    private final UserRepository userRepository;
    private final ServiceProviderRequestForWantedADRepository serviceProviderRequestForWantedADRepository;
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
            advertisement.setTitle(serviceWantedAD.getTitle());
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

    @Override
    public Page<ServiceWantedADResponse> getAllAdvertisements(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return serviceWantedAdvertisementRepository
                .findAll(pageable)
                .map(this::mapToResponse);

    }


    private ServiceWantedADResponse mapToResponse(ServiceWantedAdvertisement advertisement) {
        ServiceWantedADResponse response = new ServiceWantedADResponse();
        User user=userRepository.findById(advertisement.getClient().getId())
                .orElseThrow(() -> new RuntimeException("Client not found."));
        Client client=(Client) user;
    long countApplicantsRequest =serviceProviderRequestForWantedADRepository.countApplicantsRequests(advertisement.getAdvertisement_id());

        response.setFirstName(client.getFirstName());
        response.setLastName(client.getLastName());
        response.setTitle(advertisement.getTitle());
        response.setDescription(advertisement.getDescription());
        response.setStatus(advertisement.getStatus());
        response.setClientContactNumber(advertisement.getClientContactNumber());
        response.setServiceType(advertisement.getServiceType());
        response.setRequiredDate(advertisement.getRequiredDate());
        response.setLocation(advertisement.getLocation());
        response.setApplicationCount(countApplicantsRequest);

        return response;
    }


}
