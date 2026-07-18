package lk.workbridge.marketplace.service.Impl;

import lk.workbridge.marketplace.dto.RegisterRequest;
import lk.workbridge.marketplace.dto.UpdateProfile;
import lk.workbridge.marketplace.dto.UserProfile;
import lk.workbridge.marketplace.dto.VerificationRequest;
import lk.workbridge.marketplace.dto.WorkerSkillRequest;
import lk.workbridge.marketplace.dto.WorkerSkillResponse;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;


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
        boolean isTrue=emailService.sendVerificationEmail(
                    request.getEmail()
            );
            System.out.println(isTrue);
if(isTrue==true){

   repo.save(client);
   return "succuss";
}

        } else if (request.getRole() == Role.WORKER) {
           Worker worker=new Worker();

           worker.setEmail(request.getEmail());
           worker.setFirstName(request.getFirstName());
           worker.setLastName(request.getLastName());
           worker.setUsername(request.getUsername());
          worker.setPassword(passwordEncoder.encode(request.getPassword()));

           worker.setRole(Role.WORKER);
            worker.setVerificationStatus(
                    request.getVerificationStatus()
            );


            boolean isTrue=emailService.sendVerificationEmail(
                    request.getEmail()
            );
            System.out.println(isTrue);
           if(isTrue==true){

               repo.save(worker);
               return "worker registered successfully";
           }

        }else if (request.getRole() == Role.ADMIN) {
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
       Integer isUserVerified= repo.isUserVerified(userDetails.getUsername());
       if(isUserVerified==0){
           throw new RuntimeException("User not verified");
       }else{
             return repo.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
       }

    }


    @Override
    public boolean verifyUser(VerificationRequest request) {
        User user=repo.findByUsername(request.getUsername()).orElseThrow(
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
    public String updateProfile(UpdateProfile updateProfile) {

        Optional<User> userOptional = repo.findById(updateProfile.getUserId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
          
            if(user.getRole()==Role.CLIENT){
                  Client client=(Client) user;
                client.setPhoneNumber(
                    updateProfile.getPhoneNumber()
            );
                client.setOrganizationName(
                    updateProfile.getOrganizationName()
            );
                client.setAddress(updateProfile.getAddress());
                client.setDistrict(updateProfile.getDistrict());
             repo.save(client);
            } else if (user.getRole()==Role.WORKER) {
                
                Worker worker=(Worker) user;
                worker.setPhoneNumber(
                    updateProfile.getPhoneNumber()
            );
                worker.setAddress(updateProfile.getAddress());
                worker.setDistrict(updateProfile.getDistrict());
                worker.setTitle(updateProfile.getTitle());
                for (WorkerSkillRequest skillRequest
                    : updateProfile.getSkills()) {

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

                skill.setDailyRate(

                        skillRequest.getDailyRate()

                );

                worker.getSkills()

                        .add(skill);

            }
                repo.save(worker);
            }
            return "Profile updated successfully";
        } else {
            return "User not found with ID: " + updateProfile.getUserId();
        }


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
    public UserProfile getProfileInfo(String userId) {
        UserProfile userProfile=new UserProfile();
        User user=repo.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));
        if(user.getRole()==Role.WORKER){
            Worker worker=(Worker) user;
            Optional<Worker> workerWithSkills = workerSkillRepository.findByIdWithSkills(userId);
            List<WorkerSkillResponse> skills = workerWithSkills
                    .map(Worker::getSkills)
                    .orElse(Collections.emptySet())
                    .stream()
                    .map(skill -> new WorkerSkillResponse(
                            skill.getJobRole(),
                            skill.getDailyRate()
                    ))
                    .toList();
            userProfile.setSkills(skills);
          userProfile.setId(worker.getId());
          userProfile.setUsername(worker.getUsername());
          userProfile.setEmail(worker.getEmail());
          userProfile.setPhoneNumber(worker.getPhoneNumber());
          userProfile.setFirstName(worker.getFirstName());
          userProfile.setLastName(worker.getLastName());
          userProfile.setAddress(worker.getAddress());
          userProfile.setDistrict(worker.getDistrict());
          userProfile.setProfileImageUrl(worker.getProfileImageUrl());
          userProfile.setVerificationStatus(worker.getVerificationStatus());
          userProfile.setRole(worker.getRole());


        }else   {
            Client client=(Client) user;
            userProfile.setId(client.getId());
            userProfile.setUsername(client.getUsername());
            userProfile.setEmail(client.getEmail());
            userProfile.setPhoneNumber(client.getPhoneNumber());
            userProfile.setFirstName(client.getFirstName());
            userProfile.setLastName(client.getLastName());
            userProfile.setAddress(client.getAddress());
            userProfile.setDistrict(client.getDistrict());
            userProfile.setProfileImageUrl(client.getProfileImageUrl());
            userProfile.setVerificationStatus(client.getVerificationStatus());
            userProfile.setRole(client.getRole());
            userProfile.setOrganizationName(client.getOrganizationName());
            userProfile.setSkills(Collections.emptyList());
        }


        return userProfile;
    }

}
