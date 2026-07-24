package lk.workbridge.marketplace.service.Impl;

import lk.workbridge.marketplace.dto.ServiceProviderAD;
import lk.workbridge.marketplace.dto.responses.RatingResponse;
import lk.workbridge.marketplace.dto.responses.ServiceProviderADResponse;
import lk.workbridge.marketplace.dto.responses.WorkerSkillResponse;
import lk.workbridge.marketplace.entity.Rating;
import lk.workbridge.marketplace.entity.ServiceProviderAdvertisement;
import lk.workbridge.marketplace.entity.User;
import lk.workbridge.marketplace.entity.Worker;

import lk.workbridge.marketplace.repository.ApplicationForWantedADRepository;
import lk.workbridge.marketplace.repository.HireRequestRepository;
import lk.workbridge.marketplace.repository.RatingRepository;
import lk.workbridge.marketplace.repository.ServiceProviderAdvertisementRepository;
import lk.workbridge.marketplace.repository.UserRepository;

import lk.workbridge.marketplace.repository.WorkerSkillRepository;
import lk.workbridge.marketplace.service.ServiceProviderAdvertisementService;
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
public class ServiceProviderAdvertisementServiceImpl implements ServiceProviderAdvertisementService {

    private final ServiceProviderAdvertisementRepository serviceProviderAdvertisementRepository;
    private final UserRepository userRepository;
    private final WorkerSkillRepository workerSkillRepository;
    private final RatingRepository ratingRepository;
    private final HireRequestRepository hireRequestRepository;
    private final ApplicationForWantedADRepository applicationForWantedADRepository;

    @Override
    public String createNewAdvertisement(ServiceProviderAD serviceProviderAD) {
        User user = userRepository.findById(serviceProviderAD.getWorkerId())
                .orElseThrow(() -> new RuntimeException("Worker not found."));

        Worker worker = (Worker) user;

        validateWorkerProfile(worker);


        Optional<Worker> workerWithSkills = workerSkillRepository.findByIdWithSkills(worker.getId());

        if (workerWithSkills.isEmpty() || workerWithSkills.get().getSkills().isEmpty()) {
            throw new RuntimeException("Please complete your profile.");
        }
        ServiceProviderAdvertisement serviceProviderAdvertisement = new ServiceProviderAdvertisement();
        serviceProviderAdvertisement.setWorker(worker);
        serviceProviderAdvertisement.setStatus(serviceProviderAD.getStatus());
        serviceProviderAdvertisement.setCreatedAt(LocalDate.now());
        serviceProviderAdvertisement.setUpdatedAt(LocalDate.now());
        serviceProviderAdvertisementRepository.save(serviceProviderAdvertisement);

        return "success send";
    }



    @Override
    public ServiceProviderADResponse getAdvertisementForSpecificWorker(String workerId) {

        ServiceProviderAdvertisement serviceProviderAdvertisement = serviceProviderAdvertisementRepository.findAdvertisementByWorker(workerId).orElseThrow(() -> new RuntimeException("Advertisement not found."));

        User user = userRepository.findById(serviceProviderAdvertisement.getWorker().getId()).orElseThrow(() -> new RuntimeException("User not found."));
        Worker worker = (Worker) user;
        validateWorkerProfile(worker);
        Double averageStars = ratingRepository.getAverageStarsByWorkerId(worker.getId());
        long completedJobs = hireRequestRepository.countCompletedBookings(worker.getId(), "COMPLETED");
        Optional<Worker> byIdWithSkills = workerSkillRepository.findByIdWithSkills(worker.getId());

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
                            rating.getWorker().getTitle(),
                            rating.getProviderReply(),
                            rating.getCreatedAt()
                    );
                    return response;
                })
                .toList();
        if (byIdWithSkills.isEmpty()) {
            throw new RuntimeException("Please complete your profile.");
        }

        List<WorkerSkillResponse> skills = byIdWithSkills
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

        Set<DayOfWeek> workingDays = worker.getWorkingDays();
        Long count = ratingRepository.countByWorkerId(workerId);
        return new ServiceProviderADResponse(
                serviceProviderAdvertisement.getServiceId(),
                serviceProviderAdvertisement.getWorker().getId(),
                serviceProviderAdvertisement.getWorker().getEmail(),
                serviceProviderAdvertisement.getWorker().getPrimaryPhoneNumber(),
                serviceProviderAdvertisement.getWorker().getFirstName(),
                serviceProviderAdvertisement.getWorker().getLastName(),
                serviceProviderAdvertisement.getWorker().getDistrict(),
                serviceProviderAdvertisement.getWorker().getTitle(),
                serviceProviderAdvertisement.getWorker().getAvailable(),
                serviceProviderAdvertisement.getWorker().getAddress(),
                serviceProviderAdvertisement.getStatus(),
                serviceProviderAdvertisement.getWorker().getProfileImageUrl(),
                averageStars,
                completedJobs,
                skills,
                serviceProviderAdvertisement.getWorker().getAbout(),
                serviceProviderAdvertisement.getWorker().getOverallExperience(),
                workingDays,
                serviceProviderAdvertisement.getWorker().getStartTime(),
                serviceProviderAdvertisement.getWorker().getEndTime(),
                serviceProviderAdvertisement.getWorker().isEmergencyAvailable(),
                ratingsResponses,
                count,
                serviceProviderAdvertisement.getWorker().getProfileImageUrl(),
                serviceProviderAdvertisement.getWorker().getCity()
        );


    }

    @Override
    public Page<ServiceProviderADResponse> getAllAdvertisements(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return serviceProviderAdvertisementRepository
                .findAllVerifiedAdvertisements(pageable)
                .map(this::mapToResponse);
    }


    private ServiceProviderADResponse mapToResponse(ServiceProviderAdvertisement advertisement) {

        User user = userRepository.findById(advertisement.getWorker().getId())
                .orElseThrow(() -> new RuntimeException("User not found."));

        Worker worker = (Worker) user;

        validateWorkerProfile(worker);

        Double averageStars = ratingRepository.getAverageStarsByWorkerId(worker.getId());
        long hireRequestCompletedJobs = hireRequestRepository.countCompletedBookings(worker.getId(), "COMPLETED");
        long applicationRequestCompleted= applicationForWantedADRepository.countCompletedBookings(worker.getId(),"COMPLETED");
      long completedJobs=hireRequestCompletedJobs+applicationRequestCompleted;
        Optional<Worker> workerWithSkills =
                workerSkillRepository.findByIdWithSkills(worker.getId());

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
                            rating.getWorker().getTitle(),
                            rating.getProviderReply(),
                            rating.getCreatedAt()
                    );
                    return response;
                })
                .toList();

        Set<DayOfWeek> workingDays = worker.getWorkingDays();
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
    //TODO CHECK VALIDATION

    private void validateWorkerProfile(Worker worker) {
        try {
            // Check User fields (from User entity)
            if (worker.getFirstName() == null || worker.getFirstName().isBlank() ||
                    worker.getLastName() == null || worker.getLastName().isBlank() ||
                    worker.getEmail() == null || worker.getEmail().isBlank() ||
                    worker.getPrimaryPhoneNumber() == null || worker.getPrimaryPhoneNumber().isBlank() ||
                    worker.getDistrict() == null || worker.getDistrict().isBlank() ||
                    worker.getAddress() == null || worker.getAddress().isBlank()) {

                throw new RuntimeException("Please complete your profile. Missing required user information.");
            }

            // Check Worker specific fields
            if (worker.getAvailable() == null) {
                throw new RuntimeException("Please complete your profile. Availability status is required.");
            }

            if (worker.getNIC() == null || worker.getNIC().isBlank()) {
                throw new RuntimeException("Please complete your profile. NIC is required.");
            }

            if (worker.getTitle() == null || worker.getTitle().isBlank()) {
                throw new RuntimeException("Please complete your profile. Title is required.");
            }

            // Check working days - use the workingDaysMask
            if (worker.getWorkingDays() == null || worker.getWorkingDays().isEmpty()) {
                throw new RuntimeException("Please complete your profile. Working days are required.");
            }

            // Check secondary phone (allow null but check if present)
            if (worker.getSecondaryPhoneNumber() != null && worker.getSecondaryPhoneNumber().isBlank()) {
                throw new RuntimeException("Please complete your profile. Secondary phone number cannot be empty.");
            }

            if (worker.getStartTime() == null) {
                throw new RuntimeException("Please complete your profile. Start time is required.");
            }

            if (worker.getEndTime() == null) {
                throw new RuntimeException("Please complete your profile. End time is required.");
            }

        } catch (NullPointerException e) {
            throw new RuntimeException("Please complete your profile. Missing required fields.", e);
        }

    }


}
