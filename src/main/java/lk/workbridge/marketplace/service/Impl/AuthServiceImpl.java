package lk.workbridge.marketplace.service.Impl;

import lk.workbridge.marketplace.dto.RegisterRequest;
import lk.workbridge.marketplace.dto.VerificationRequest;
import lk.workbridge.marketplace.dto.WorkerSkillRequest;
import lk.workbridge.marketplace.entity.Client;
import lk.workbridge.marketplace.entity.JobRole;
import lk.workbridge.marketplace.entity.User;
import lk.workbridge.marketplace.entity.Worker;
import lk.workbridge.marketplace.entity.WorkerSkill;
import lk.workbridge.marketplace.enums.Role;
import lk.workbridge.marketplace.repository.JobRoleRepository;
import lk.workbridge.marketplace.repository.UserRepository;
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

import java.util.Collections;


@Service
@RequiredArgsConstructor

public class AuthServiceImpl implements AuthService, UserDetailsService {
    private final UserRepository repo;
    private final JobRoleRepository jobRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

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

            client.setPhoneNumber(
                    request.getPhoneNumber()
            );
            client.setOrganizationName(
                    request.getOrganizationName()
            );
            client.setVerificationStatus(
                    request.getVerificationStatus()
            );
            client.setAddress(request.getAddress());
            client.setDistrict(request.getDistrict());
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
           worker.setAvailable(request.getAvailable());
           worker.setDistrict(request.getDistrict());
           worker.setEmail(request.getEmail());
           worker.setFirstName(request.getFirstName());
           worker.setLastName(request.getLastName());
           worker.setUsername(request.getUsername());
          worker.setPassword(passwordEncoder.encode(request.getPassword()));
           worker.setPhoneNumber(request.getPhoneNumber());
           worker.setAddress(request.getAddress());
           worker.setRole(Role.WORKER);
            worker.setVerificationStatus(
                    request.getVerificationStatus()
            );
            for (WorkerSkillRequest skillRequest
                    : request.getSkills()) {

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

            boolean isTrue=emailService.sendVerificationEmail(
                    request.getEmail()
            );
            System.out.println(isTrue);
           if(isTrue==true){

               repo.save(worker);
               return "worker registered successfully";
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

}
