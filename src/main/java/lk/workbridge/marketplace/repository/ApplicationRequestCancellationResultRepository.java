package lk.workbridge.marketplace.repository;

import lk.workbridge.marketplace.entity.ApplicationRequestCancellationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRequestCancellationResultRepository extends JpaRepository<ApplicationRequestCancellationResult, Long> {
}
