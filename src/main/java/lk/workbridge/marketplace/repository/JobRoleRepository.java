package lk.workbridge.marketplace.repository;

import lk.workbridge.marketplace.entity.JobRole;
import lk.workbridge.marketplace.enums.WorkerJobRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobRoleRepository extends JpaRepository<JobRole,Long> {
    Optional<JobRole>

    findByRoleName(

            WorkerJobRole roleName

    );
    boolean existsByRoleName(WorkerJobRole roleName);

}
