package lk.workbridge.marketplace.repository;

import lk.workbridge.marketplace.entity.Worker;
import lk.workbridge.marketplace.entity.WorkerSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface WorkerSkillRepository extends JpaRepository<WorkerSkill, Long> {

    @Query("""
                SELECT DISTINCT w
                FROM Worker w
                LEFT JOIN FETCH w.skills s
                LEFT JOIN FETCH s.jobRole
                WHERE w.id = :workerId
            """)
    Optional<Worker> findByIdWithSkills(@Param("workerId") String workerId);
}
