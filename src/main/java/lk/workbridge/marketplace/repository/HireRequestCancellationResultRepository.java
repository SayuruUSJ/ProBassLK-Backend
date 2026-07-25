package lk.workbridge.marketplace.repository;

import lk.workbridge.marketplace.entity.HireRequestCancellationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HireRequestCancellationResultRepository extends JpaRepository<HireRequestCancellationResult, Long> {
}
