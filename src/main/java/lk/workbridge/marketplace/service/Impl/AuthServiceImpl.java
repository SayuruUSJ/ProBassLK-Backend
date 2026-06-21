package lk.workbridge.marketplace.service.Impl;

import lk.workbridge.marketplace.dto.RegisterRequest;
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

            repo.save(client);
            return "succuss";
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
           repo.save(worker);
           return "worker registered successfully";
        }
        return "";
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
             return repo.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
