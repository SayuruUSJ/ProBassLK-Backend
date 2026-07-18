package lk.workbridge.marketplace.service.Impl;

import lk.workbridge.marketplace.dto.ServiceProviderAD;
import lk.workbridge.marketplace.dto.ServiceProviderADResponse;
import lk.workbridge.marketplace.dto.WorkerSkillResponse;
import lk.workbridge.marketplace.entity.ServiceProviderAdvertisement;
import lk.workbridge.marketplace.entity.User;
import lk.workbridge.marketplace.entity.Worker;

import lk.workbridge.marketplace.entity.WorkerSkill;
import lk.workbridge.marketplace.repository.ClientBookingRequestAdvertisementRepository;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceProviderAdvertisementServiceImpl implements ServiceProviderAdvertisementService {

   private final ServiceProviderAdvertisementRepository serviceProviderAdvertisementRepository;
 private final UserRepository userRepository;
 private final WorkerSkillRepository workerSkillRepository;
 private final RatingRepository ratingRepository;
 private final ClientBookingRequestAdvertisementRepository clientBookingRequestAdvertisementRepository;
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
        ServiceProviderAdvertisement serviceProviderAdvertisement=new ServiceProviderAdvertisement();
        serviceProviderAdvertisement.setWorker(worker);
        serviceProviderAdvertisement.setStatus(serviceProviderAD.getStatus());
        serviceProviderAdvertisementRepository.save(serviceProviderAdvertisement);
        return "success send";
    }




    @Override
    public ServiceProviderADResponse getAdvertisementForSpecificWorker(String workerId) {

       ServiceProviderAdvertisement serviceProviderAdvertisement =serviceProviderAdvertisementRepository.findAdvertisementByWorker(workerId).orElseThrow(() -> new RuntimeException("Advertisement not found."));
        ServiceProviderADResponse response = new ServiceProviderADResponse();
        User user=userRepository.findById(serviceProviderAdvertisement.getWorker().getId()).orElseThrow(() -> new RuntimeException("User not found."));
        Worker worker=(Worker) user;
        validateWorkerProfile(worker);
        Double averageStars = ratingRepository.getAverageStarsByWorkerId(worker.getId());
        long completedJobs= clientBookingRequestAdvertisementRepository.countCompletedBookings(worker.getId(), "COMPLETED");

        response.setAverageStars(averageStars);
        response.setCompletedJobs(completedJobs);
        response.setJobTitle(worker.getTitle());
        response.setServiceId(serviceProviderAdvertisement.getServiceId());
        response.setStatus(serviceProviderAdvertisement.getStatus());
        response.setWorkerId(worker.getId());
        response.setFirstName(worker.getFirstName());
        response.setLastName(worker.getLastName());
        response.setEmail(worker.getEmail());
        response.setAvailable(worker.getAvailable());
        response.setPhoneNumber(worker.getPhoneNumber());
        response.setDistrict(worker.getDistrict());
        response.setAddress(worker.getAddress());
        response.setProfileUrl(worker.getProfileImageUrl());
        Optional<Worker> byIdWithSkills = workerSkillRepository.findByIdWithSkills(worker.getId());

        if (byIdWithSkills.isEmpty()) {
            throw new RuntimeException("Please complete your profile.");
        }

        List<WorkerSkillResponse> skills = byIdWithSkills
                .map(Worker::getSkills)
                .orElse(Collections.emptySet())
                .stream()
                .map(skill -> new WorkerSkillResponse(
                        skill.getJobRole(),
                        skill.getDailyRate()
                ))
                .toList();

        response.setSkills(skills);


        return response;


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
         long completedJobs= clientBookingRequestAdvertisementRepository.countCompletedBookings(worker.getId(), "COMPLETED");
        ServiceProviderADResponse response = new ServiceProviderADResponse();
        response.setServiceId(advertisement.getServiceId());
        response.setAverageStars(averageStars);
        response.setCompletedJobs(completedJobs);
        response.setJobTitle(worker.getTitle());
        response.setServiceId(advertisement.getServiceId());
        response.setStatus(advertisement.getStatus());
        response.setWorkerId(worker.getId());
        response.setFirstName(worker.getFirstName());
        response.setLastName(worker.getLastName());
        response.setEmail(worker.getEmail());
        response.setAvailable(worker.getAvailable());
        response.setPhoneNumber(worker.getPhoneNumber());
        response.setDistrict(worker.getDistrict());
        response.setAddress(worker.getAddress());

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
                        skill.getJobRole(),
                        skill.getDailyRate()
                ))
                .toList();

        response.setSkills(skills);

        return response;
    }

    private void validateWorkerProfile(Worker worker) {
        if (worker.getFirstName() == null || worker.getFirstName().isBlank() ||
                worker.getLastName() == null || worker.getLastName().isBlank() ||
                worker.getEmail() == null || worker.getEmail().isBlank() ||
                worker.getPhoneNumber() == null || worker.getPhoneNumber().isBlank() ||
                worker.getDistrict() == null || worker.getDistrict().isBlank() ||
                worker.getAddress() == null || worker.getAddress().isBlank() ||
                worker.getAvailable() == null ){


            throw new RuntimeException("Please complete your profile.");
        }
    }



}
