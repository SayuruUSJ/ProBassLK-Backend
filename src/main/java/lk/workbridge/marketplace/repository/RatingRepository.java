package lk.workbridge.marketplace.repository;

import lk.workbridge.marketplace.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("SELECT AVG(r.stars) FROM Rating r WHERE r.worker.id = :workerId")
    Double getAverageStarsByWorkerId(@Param("workerId") String workerId);

}
