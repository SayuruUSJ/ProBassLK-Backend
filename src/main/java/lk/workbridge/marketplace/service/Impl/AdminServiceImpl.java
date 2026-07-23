package lk.workbridge.marketplace.service.Impl;

import lk.workbridge.marketplace.dto.responses.BookingRequestResponse;
import lk.workbridge.marketplace.dto.responses.ClientMoreInfo;
import lk.workbridge.marketplace.dto.responses.RatingResponse;
import lk.workbridge.marketplace.dto.responses.ServiceProviderADResponse;
import lk.workbridge.marketplace.dto.responses.ServiceWantedADResponse;
import lk.workbridge.marketplace.dto.responses.UserBasicInfo;
import lk.workbridge.marketplace.dto.responses.WorkerMoreInfo;
import lk.workbridge.marketplace.dto.responses.WorkerSkillResponse;
import lk.workbridge.marketplace.entity.Client;
import lk.workbridge.marketplace.entity.HireRequest;
import lk.workbridge.marketplace.entity.Rating;
import lk.workbridge.marketplace.entity.ServiceProviderAdvertisement;
import lk.workbridge.marketplace.entity.ServiceWantedAdvertisement;
import lk.workbridge.marketplace.entity.User;
import lk.workbridge.marketplace.entity.Worker;
import lk.workbridge.marketplace.repository.HireRequestRepository;
import lk.workbridge.marketplace.repository.RatingRepository;
import lk.workbridge.marketplace.repository.ServiceProviderAdvertisementRepository;
import lk.workbridge.marketplace.repository.ApplicationForWantedADRepository;
import lk.workbridge.marketplace.repository.ServiceWantedAdvertisementRepository;
import lk.workbridge.marketplace.repository.UserRepository;
import lk.workbridge.marketplace.repository.WorkerSkillRepository;
import lk.workbridge.marketplace.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final ServiceProviderAdvertisementRepository serviceProviderAdvertisementRepository;
    private final ServiceWantedAdvertisementRepository serviceWantedAdvertisementRepository;
    private final UserRepository userRepository;
    private final WorkerSkillRepository workerSkillRepository;
    private final RatingRepository ratingRepository;
    private final HireRequestRepository hireRequestRepository;
    private final ApplicationForWantedADRepository applicationForWantedADRepository;
    @Override
    public boolean acceptOrRejectServiceProviderAdvertisement(String serviceId, String status) {
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid status: " + status + ". Status must be 'PUBLISHED' or 'REJECTED'");
        }
      ServiceProviderAdvertisement providerAdvertisement= serviceProviderAdvertisementRepository.findById(serviceId)
        .orElseThrow(() -> new IllegalArgumentException("Advertisement not found with ID: " + serviceId));
        providerAdvertisement.setStatus(status);
        providerAdvertisement.setUpdatedAt(LocalDate.now());
        serviceProviderAdvertisementRepository.save(providerAdvertisement);
        return true;
    }

    @Override
    public boolean acceptOrRejectServiceWantedAdvertisement(String advertisementId, String status) {
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid status: " + status + ". Status must be 'PUBLISHED' or 'REJECTED'");
        }

        ServiceWantedAdvertisement wantedAdvertisement= serviceWantedAdvertisementRepository.findById(advertisementId)
            .orElseThrow(() -> new IllegalArgumentException("Advertisement not found with ID: " + advertisementId));
        wantedAdvertisement.setStatus(status);
        wantedAdvertisement.setUpdatedAt(LocalDate.now());
        serviceWantedAdvertisementRepository.save(wantedAdvertisement);
        return true;
    }

    private boolean isValidStatus(String status) {
        return "PUBLISHED".equals(status) || "REJECTED".equals(status);
    }

    @Override
    public Page<ServiceProviderADResponse> getAllPendingServiceProviderAdvertisements(int page, int size) {
        Pageable pageable= PageRequest.of(page,size);

        return serviceProviderAdvertisementRepository
                .findAllPendingAdvertisements(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<ServiceWantedADResponse> getAllPendingWantedAdvertisements(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return serviceWantedAdvertisementRepository
                .findAllPendingAdvertisements(pageable)
                .map(this::mapToServiceWantedResponse);
    }

    @Override
    public String deleteServiceProviderAdvertisement(String serviceId) {
        serviceProviderAdvertisementRepository.deleteById(serviceId);
        return "Advertisement deleted successfully.";
    }

    @Override
    public String deleteServiceWantedAdvertisement(String advertisementId) {
        serviceWantedAdvertisementRepository.deleteById(advertisementId);
        return "Advertisement deleted successfully.";
    }

    @Override
    public String deleteUser(String userId) {
        User user = userRepository.findByIdJPQL(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
        userRepository.flush();


        return "User deleted successfully.";
    }

  
    @Override
    public Page<UserBasicInfo> getAllUsersBaseInfo(int page , int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAllUsersWithBasicInfo(pageable);
        return users.map(this::convertToUserBaseInfo);
    }

    @Override
    public WorkerMoreInfo getWorkerMoreInfo(String workerId) {
        User user=userRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("User not found."));
        Worker worker=(Worker) user;
        return convertToWorkerMoreInfo(worker);
    }

    @Override
    public ClientMoreInfo getClientMoreInfo(String clientId) {
        User user=userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("User not found."));
        Client client=(Client) user;
        return convertToClientMoreInfo(client);
    }

    private ClientMoreInfo convertToClientMoreInfo(Client client) {

        List<HireRequest> bookingRequests =
                hireRequestRepository.findByClientId(client.getId());

        List<BookingRequestResponse> bookingRequestResponses = bookingRequests
                .stream()
                .map(request -> {
                    BookingRequestResponse response = new BookingRequestResponse(
                            request.getId(),
                            request.getStatus(),
                            request.getClient().getId(),
                            request.getClientName(),
                            request.getServiceProviderAdvertisement().getServiceId(),
                            request.getWorkerEmail(),
                            request.getRequestedService(),
                            request.getRequestedDate(),
                            request.getClientContactNumber(),
                            request.getLocation()
                    );
                    return response;
                })
                .toList();
        return new ClientMoreInfo(
                client.getOrganizationName(),
                bookingRequestResponses
        );
    }

    private WorkerMoreInfo convertToWorkerMoreInfo(Worker worker) {

        Optional<Worker> workerWithSkills = workerSkillRepository.findByIdWithSkills(worker.getId());

        List<WorkerSkillResponse> skills = workerWithSkills
                .map(Worker::getSkills)
                .orElse(Collections.emptySet())
                .stream()
                .map(skill -> new WorkerSkillResponse(
                        skill.getId(),
                        skill.getJobRole(),
                        skill.getRate(),
                        skill.getRateType(),
                        skill.getExperience(),
                        skill.getDescription(),
                        skill.getNegotiable()
                ))
                .toList();

        List<HireRequest> bookingRequests =
                hireRequestRepository.findByWorkerId(worker.getId());

        List<BookingRequestResponse> bookingRequestResponses = bookingRequests
                .stream()
                .map(request -> {
                    BookingRequestResponse response = new BookingRequestResponse(
                            request.getId(),
                            request.getStatus(),
                            request.getClient().getId(),
                            request.getClientName(),
                            request.getServiceProviderAdvertisement().getServiceId(),
                            request.getWorkerEmail(),
                            request.getRequestedService(),
                            request.getRequestedDate(),
                            request.getClientContactNumber(),
                            request.getLocation()
                    );
                    return response;
                })
                .toList();

        List<Rating> ratings = ratingRepository.findByWorker(worker);

        List<RatingResponse> ratingsResponses =ratings
                .stream()
                .map(rating -> {
                    RatingResponse response = new RatingResponse(
                            rating.getId(),
                            rating.getStars(),
                            rating.getComment(),
                            rating.getWorker() != null ? rating.getWorker().getId() : null,
                            rating.getWorker() != null ? rating.getWorker().getFirstName() : null,
                            rating.getClient() != null ? rating.getClient().getId() : null,
                            rating.getClient() != null ? rating.getClient().getFirstName() : null,
                            rating.getWorker().getTitle()
                    );
                    return response;
                })
                .toList();

        WorkerMoreInfo info = new WorkerMoreInfo(
                worker.getAvailable(),
                worker.getTitle(),
                bookingRequestResponses,
                skills,
                ratingsResponses,
                worker.getOverallExperience(),
                worker.getAbout(),
                worker.isEmergencyAvailable(),
                worker.getNIC(),
                worker.getWorkingDaysMask(),
                worker.getStartTime(),
                worker.getEndTime()
        );

        return info;
    }

    private UserBasicInfo convertToUserBaseInfo(User user) {
        return new UserBasicInfo(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getPrimaryPhoneNumber(),
                user.getSecondaryPhoneNumber(),
                user.getLandlineNumber(),
                user.getWhatsappNumber(),
                user.getFirstName(),
                user.getLastName(),
                user.getDistrict(),
                user.getAddress(),
                user.getRole(),
                user.getCreatedAt(),
                user.getVerificationStatus(),
                user.getProfileImageUrl()
        );
    }





    private ServiceWantedADResponse mapToServiceWantedResponse(ServiceWantedAdvertisement advertisement) {
        User user=userRepository.findById(advertisement.getClient().getId())
                .orElseThrow(() -> new RuntimeException("Client not found."));
        Client client=(Client) user;
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
                advertisement.isUrgent()
        );

    }

    private ServiceProviderADResponse mapToResponse(ServiceProviderAdvertisement advertisement) {

        User user = userRepository.findById(advertisement.getWorker().getId())
                .orElseThrow(() -> new RuntimeException("User not found."));

        Worker worker = (Worker) user;

        Double averageStars = ratingRepository.getAverageStarsByWorkerId(worker.getId());
        long completedJobs= hireRequestRepository.countCompletedBookings(worker.getId(), "COMPLETED");
        Optional<Worker> workerWithSkills =
                workerSkillRepository.findByIdWithSkills(worker.getId());
        Set<DayOfWeek> workingDays = worker.getWorkingDays();
        List<Rating> ratings = ratingRepository.findByWorker(worker);
        List<RatingResponse> ratingsResponses =ratings
                .stream()
                .map(rating -> {
                    RatingResponse response = new RatingResponse(
                            rating.getId(),
                            rating.getStars(),
                            rating.getComment(),
                            rating.getWorker() != null ? rating.getWorker().getId() : null,
                            rating.getWorker() != null ? rating.getWorker().getFirstName() : null,
                            rating.getClient() != null ? rating.getClient().getId() : null,
                            rating.getClient() != null ? rating.getClient().getFirstName() : null,
                            rating.getWorker().getTitle()
                    );
                    return response;
                })
                .toList();

        if (workerWithSkills.isEmpty()) {
            throw new RuntimeException("Please complete your profile.");
        }

        List<WorkerSkillResponse> skills = workerWithSkills
                .map(Worker::getSkills)
                .orElse(Collections.emptySet())
                .stream()
                .map(skill -> new WorkerSkillResponse(
                        skill.getId(),
                        skill.getJobRole(),
                        skill.getRate(),
                        skill.getRateType(),
                        skill.getExperience(),
                        skill.getDescription(),
                        skill.getNegotiable()
                ))
                .toList();
        Long count = ratingRepository.countByWorkerId(worker.getId());
       return new ServiceProviderADResponse(
                advertisement.getServiceId(),
                worker.getId(),
                worker.getEmail(),
                worker.getPrimaryPhoneNumber(),
                worker.getFirstName(),
                worker.getLastName(),
                worker.getDistrict(),
                worker.getTitle(),
                worker.getAvailable(),
                worker.getAddress(),
                advertisement.getStatus(),
                worker.getProfileImageUrl(),
                averageStars,
                completedJobs,
                skills,
               worker.getAbout(),
               worker.getOverallExperience(),
               workingDays,
               worker.getStartTime(),
               worker.getEndTime(),
               worker.isEmergencyAvailable(),
               ratingsResponses,
               count,
               worker.getProfileImageUrl(),
               worker.getCity()
        );

    }

}
