package lk.workbridge.marketplace.service.Impl;

import lk.workbridge.marketplace.dto.ClientProfileUpdate;
import lk.workbridge.marketplace.dto.RegisterRequest;
import lk.workbridge.marketplace.dto.ServiceProviderProfileUpdate;
import lk.workbridge.marketplace.dto.VerificationRequest;
import lk.workbridge.marketplace.dto.WorkerSkillRequest;
import lk.workbridge.marketplace.dto.responses.ClientProfile;
import lk.workbridge.marketplace.dto.responses.ServiceProviderProfile;
import lk.workbridge.marketplace.dto.responses.WorkerSkillResponse;
import lk.workbridge.marketplace.entity.Admin;
import lk.workbridge.marketplace.entity.Client;
import lk.workbridge.marketplace.entity.JobRole;
import lk.workbridge.marketplace.entity.User;
import lk.workbridge.marketplace.entity.Worker;
import lk.workbridge.marketplace.entity.WorkerSkill;
import lk.workbridge.marketplace.enums.Role;
import lk.workbridge.marketplace.repository.JobRoleRepository;
import lk.workbridge.marketplace.repository.UserRepository;
import lk.workbridge.marketplace.repository.WorkerSkillRepository;
import lk.workbridge.marketplace.service.AuthService;
import lk.workbridge.marketplace.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService, UserDetailsService {
    private final UserRepository repo;
    private final JobRoleRepository jobRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final WorkerSkillRepository workerSkillRepository;

    @Override
    public String register(RegisterRequest request) {
        if (request.getRole() == Role.CLIENT) {
            Client client = new Client();
            client.setFirstName(
                    request.getFirstName()
            );
            client.setUsername(request.getUsername());
            client.setLastName(
                    request.getLastName()
            );

            client.setEmail(
                    request.getEmail()
            );

            client.setPassword(
                    passwordEncoder.encode(request.getPassword())
            );


            client.setVerificationStatus(
                    request.getVerificationStatus()
            );

            client.setRole(
                    Role.CLIENT
            );
            boolean isTrue = emailService.sendVerificationEmail(
                    request.getEmail()
            );
            System.out.println(isTrue);
            if (isTrue == true) {

                repo.save(client);
                return "succuss";
            }

        } else if (request.getRole() == Role.WORKER) {
            Worker worker = new Worker();

            worker.setEmail(request.getEmail());
            worker.setFirstName(request.getFirstName());
            worker.setLastName(request.getLastName());
            worker.setUsername(request.getUsername());
            worker.setPassword(passwordEncoder.encode(request.getPassword()));

            worker.setRole(Role.WORKER);
            worker.setVerificationStatus(
                    request.getVerificationStatus()
            );


            boolean isTrue = emailService.sendVerificationEmail(
                    request.getEmail()
            );
            System.out.println(isTrue);
            if (isTrue == true) {

                repo.save(worker);
                return "worker registered successfully";
            }

        } else if (request.getRole() == Role.ADMIN) {
            Admin admin = new Admin();
            admin.setFirstName(request.getFirstName());
            admin.setLastName(request.getLastName());
            admin.setUsername(request.getUsername());
            admin.setEmail(request.getEmail());
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
            admin.setVerificationStatus(request.getVerificationStatus());
            admin.setRole(Role.ADMIN);


            boolean isTrue = emailService.sendVerificationEmail(admin.getEmail());
            System.out.println(isTrue);
            if (isTrue == true) {
                repo.save(admin);
                return "admin registered successfully";
            }
        }
        return "Registration failed";
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));


        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                )
        );
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Integer isUserVerified = repo.isUserVerified(userDetails.getUsername());
        if (isUserVerified == 0) {
            throw new RuntimeException("User not verified");
        } else {
            return repo.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

    }


    @Override
    public boolean verifyUser(VerificationRequest request) {
        User user = repo.findByUsername(request.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + request.getUsername())
        );
        if (user != null) {
            user.setVerificationStatus(request.isVerificationStatus());
            repo.save(user);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public String clientProfileUpdate(ClientProfileUpdate clientProfileUpdate) {
        Optional<User> userOptional = Optional.of(repo.findById(clientProfileUpdate.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found")));

        User user = userOptional.get();
        Client client = (Client) user;
        client.setPrimaryPhoneNumber(
                clientProfileUpdate.getPhoneNumber()
        );
        client.setSecondaryPhoneNumber(
                clientProfileUpdate.getSecondaryPhoneNumber()
        );
        client.setLandlineNumber(
                clientProfileUpdate.getLandlineNumber()
        );
        client.setOrganizationName(
                clientProfileUpdate.getOrganizationName()
        );
        client.setAddress(clientProfileUpdate.getAddress());
        client.setDistrict(clientProfileUpdate.getDistrict());
        client.setCity(clientProfileUpdate.getCity());
        repo.save(client);

        return "Client Profile Updated Successfully";
    }
    @Transactional
    @Override
    public String serviceProviderProfileUpdate(ServiceProviderProfileUpdate serviceProviderProfileUpdate) {
        Optional<User> userOptional = Optional.of(repo.findById(serviceProviderProfileUpdate.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found")));

        User user = userOptional.get();
        Worker worker = (Worker) user;
        worker.setPrimaryPhoneNumber(
                serviceProviderProfileUpdate.getPhoneNumber()
        );
        worker.setSecondaryPhoneNumber(
                serviceProviderProfileUpdate.getSecondaryPhoneNumber()
        );
        worker.setLandlineNumber(
                serviceProviderProfileUpdate.getLandlineNumber()
        );
        worker.setAddress(serviceProviderProfileUpdate.getAddress());
        worker.setDistrict(serviceProviderProfileUpdate.getDistrict());
        worker.setCity(serviceProviderProfileUpdate.getCity());
        worker.setTitle(serviceProviderProfileUpdate.getTitle());
        worker.setWorkingDaysFromSet(serviceProviderProfileUpdate.getWorkingDays());

        worker.setStartTime(
                serviceProviderProfileUpdate.getStartTime()

        );
        worker.setEndTime(serviceProviderProfileUpdate.getEndTime());
        worker.setNIC(serviceProviderProfileUpdate.getNIC());
        worker.setEmergencyAvailable(serviceProviderProfileUpdate.isEmergencyAvailable());
        worker.setOverallExperience(serviceProviderProfileUpdate.getOverallExperience());
        worker.setAbout(serviceProviderProfileUpdate.getAbout());

        for (WorkerSkillRequest skillRequest
                : serviceProviderProfileUpdate.getSkills()) {

            JobRole jobRole =

                    jobRoleRepository

                            .findByRoleName(

                                    skillRequest.getRole()

                            )

                            .orElseThrow(

                                    () -> new RuntimeException(

                                            "Role not found"

                                    )

                            );

            WorkerSkill skill =
                    new WorkerSkill();

            skill.setWorker(
                    worker
            );

            skill.setJobRole(
                    jobRole
            );

            skill.setRate(

                    skillRequest.getDailyRate()

            );
            skill.setRateType(skillRequest.getRateType());
            skill.setExperience(skillRequest.getExperience());
            skill.setDescription(skill.getDescription());
            skill.setNegotiable(skill.isNegotiable());
            worker.getSkills()

                    .add(skill);

        }
        repo.save(worker);
        return "service provider profile updated succfully";
    }

    @Override
    public String uploadProfileImage(String profileImageUrl, String userId) {
        System.out.println("User ID received: [" + userId + "]");

        Optional<User> userOptional = repo.findById(userId);

        System.out.println("User found: " + userOptional.isPresent());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setProfileImageUrl(profileImageUrl);
            repo.save(user);
            return "Profile image uploaded successfully";
        }

        return "User not found with ID: " + userId;
    }


    @Override
    public ClientProfile getClientProfileInfo(String userId) {
        User user = repo.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));

        Client client = (Client) user;
        return new ClientProfile(
                client.getId(),
                client.getUsername(),
                client.getEmail(),
                client.getPrimaryPhoneNumber(),
                client.getSecondaryPhoneNumber(),
                client.getLandlineNumber(),
                client.getFirstName(),
                client.getLastName(),
                client.getDistrict(),
                client.getAddress(),
                client.getCity(),
                client.getRole(),
                client.getCreatedAt(),
                client.getVerificationStatus(),
                client.getProfileImageUrl(),
                client.getOrganizationName()

        );

    }

    @Override
    public ServiceProviderProfile getServiceProviderProfileInfo(String userId) {
        User user = repo.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));
        Worker worker = (Worker) user;
        Optional<Worker> workerWithSkills = workerSkillRepository.findByIdWithSkills(userId);
        List<WorkerSkillResponse> skills = workerWithSkills
                .map(Worker::getSkills)
                .orElse(Collections.emptySet())
                .stream()
                .map(skill -> new WorkerSkillResponse(
                        skill.getJobRole(),
                        skill.getRate(),
                        skill.getRateType(),
                        skill.getExperience(),
                        skill.getDescription(),
                        skill.isNegotiable()
                ))
                .toList();
        Set<DayOfWeek> workingDays = worker.getWorkingDays();
        return new ServiceProviderProfile(
                worker.getId(),
                worker.getUsername(),
                worker.getEmail(),
                worker.getPrimaryPhoneNumber(),
                worker.getSecondaryPhoneNumber(),
                worker.getLandlineNumber(),
                worker.getFirstName(),
                worker.getLastName(),
                worker.getDistrict(),
                worker.getAddress(),
                worker.getCity(),
                worker.getRole(),
                worker.getCreatedAt(),
                worker.getVerificationStatus(),
                worker.getProfileImageUrl(),
                  skills,
                worker.getAvailable() != null && worker.getAvailable(),
                worker.getTitle(),
                worker.getOverallExperience() > 0 ? worker.getOverallExperience() : null,
                worker.getAbout(),
                worker.isEmergencyAvailable(),
                worker.getNIC(),
                workingDays,
                worker.getStartTime(),
                worker.getEndTime()

        );
    }

}
