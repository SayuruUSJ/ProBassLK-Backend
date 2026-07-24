package lk.workbridge.marketplace.service.Impl;

import lk.workbridge.marketplace.dto.ServiceWantedAD;
import lk.workbridge.marketplace.dto.responses.ServiceWantedADResponse;
import lk.workbridge.marketplace.entity.Client;
import lk.workbridge.marketplace.entity.ServiceWantedAdvertisement;
import lk.workbridge.marketplace.entity.User;
import lk.workbridge.marketplace.repository.ApplicationForWantedADRepository;
import lk.workbridge.marketplace.repository.ServiceWantedAdvertisementRepository;
import lk.workbridge.marketplace.repository.UserRepository;
import lk.workbridge.marketplace.service.ServiceWantedAdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ServiceWantedAdvertisementServiceImpl implements ServiceWantedAdvertisementService {

    private final ServiceWantedAdvertisementRepository serviceWantedAdvertisementRepository;
    private final UserRepository userRepository;
    private final ApplicationForWantedADRepository applicationForWantedADRepository;

    @Override
    public String requestAdvertisement(ServiceWantedAD serviceWantedAD) {
        try {

            User userTwo = userRepository.findById(serviceWantedAD.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client not found."));
            Client client = (Client) userTwo;
            ServiceWantedAdvertisement advertisement = new ServiceWantedAdvertisement();

            if (!isValidStatus(serviceWantedAD.getStatus())) {
                throw new IllegalArgumentException("Invalid status: " + serviceWantedAD.getStatus() + ". Status must be 'VERIFIED' or 'REJECTED'");
            }
            advertisement.setClient(client);
            advertisement.setClientContactNumber(serviceWantedAD.getClientContactNumber());
            advertisement.setPreferredContactMethod(serviceWantedAD.getPreferredContactMethod());
            advertisement.setTitle(serviceWantedAD.getTitle());
            advertisement.setDescription(serviceWantedAD.getDescription());
            advertisement.setServiceType(serviceWantedAD.getServiceType());
            advertisement.setFullAddress(serviceWantedAD.getLocation());
            advertisement.setCity(serviceWantedAD.getCity());
            advertisement.setDistrict(serviceWantedAD.getDistrict());
            advertisement.setRequiredDate(serviceWantedAD.getRequiredDate());
            advertisement.setStartTime(serviceWantedAD.getStartTime());
            advertisement.setExpectedDuration(serviceWantedAD.getExpectedDuration());
            advertisement.setApplicationDeadline(serviceWantedAD.getApplicationDeadline());
            advertisement.setWorkDateFlexible(serviceWantedAD.getIsWorkDateFlexible());
            advertisement.setStatus(serviceWantedAD.getStatus());
            advertisement.setRequiredSkills(serviceWantedAD.getRequiredSkills());
            advertisement.setNoOfWorkersRequired(serviceWantedAD.getNoOfWorkersRequired());
            advertisement.setPaymentType(serviceWantedAD.getPaymentType());
            advertisement.setOfferedRate(serviceWantedAD.getOfferedRate());
            advertisement.setRateNegotiable(serviceWantedAD.getIsRateNegotiable());
            advertisement.setAdditionalInstructions(serviceWantedAD.getAdditionalInstructions());
            advertisement.setUrgent(serviceWantedAD.isUrgent());
            advertisement.setCreatedAt(LocalDate.now());
            advertisement.setUpdatedAt(LocalDate.now());

            serviceWantedAdvertisementRepository.save(advertisement);
            return "Advertisement request sent successfully.";

        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while saving advertisement.", e);
        }
    }

    private boolean isValidStatus(String status) {
        return "PENDING".equals(status);
    }

    @Override
    public Page<ServiceWantedADResponse> getAllAdvertisements(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return serviceWantedAdvertisementRepository
                .findAllVerifiedAdvertisements(pageable)
                .map(this::mapToResponse);

    }

    @Override
    public Page<ServiceWantedADResponse> getClientSpecificAdvertisements(String clientId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return serviceWantedAdvertisementRepository
                .findAllAdvertisementByClientId(clientId, pageable)
                .map(this::mapToResponse);

    }


    private ServiceWantedADResponse mapToResponse(ServiceWantedAdvertisement advertisement) {
        User user = userRepository.findById(advertisement.getClient().getId())
                .orElseThrow(() -> new RuntimeException("Client not found."));
        Client client = (Client) user;
        long countApplicantsRequest = applicationForWantedADRepository.countApplicantsRequests(advertisement.getAdvertisement_id());
        return new ServiceWantedADResponse(
                advertisement.getAdvertisement_id(),
                client.getFirstName(),
                client.getLastName(),
                advertisement.getTitle(),
                advertisement.getClientContactNumber(),
                advertisement.getDescription(),
                advertisement.getServiceType(),
                advertisement.getFullAddress(),
                advertisement.getRequiredDate(),
                advertisement.getStatus(),
                countApplicantsRequest,
                advertisement.getPreferredContactMethod(),
                advertisement.getCity(),
                advertisement.getDistrict(),
                advertisement.getStartTime(),
                advertisement.getExpectedDuration(),
                advertisement.getApplicationDeadline(),
                advertisement.isWorkDateFlexible(),
                advertisement.getRequiredSkills(),
                advertisement.getNoOfWorkersRequired(),
                advertisement.getPaymentType(),
                advertisement.getOfferedRate(),
                advertisement.isRateNegotiable(),
                advertisement.getAdditionalInstructions(),
                advertisement.isUrgent(),
                advertisement.getCreatedAt(),
                advertisement.getUpdatedAt(),
                advertisement.getClient().getProfileImageUrl()
        );


    }


}
