package lk.workbridge.marketplace.service;

import jakarta.annotation.PostConstruct;
import lk.workbridge.marketplace.entity.JobRole;
import lk.workbridge.marketplace.enums.WorkerJobRole;
import lk.workbridge.marketplace.repository.JobRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobRoleInitializerService {
    private final JobRoleRepository jobRoleRepository;

    @PostConstruct
    public void initRoles() {
        // All your categories from the image
        for (WorkerJobRole role : WorkerJobRole.values()) {
            addRoleIfNotExists(role);
        }
    }

    private void addRoleIfNotExists(WorkerJobRole role) {
        if (!jobRoleRepository.existsByRoleName(role)) {
            JobRole jobRole = new JobRole();
            jobRole.setRoleName(role);
            jobRoleRepository.save(jobRole);
            System.out.println("✅ Added job role: " + role);
        } else {
            System.out.println("⏭️ Job role already exists: " + role);
        }
    }

}
